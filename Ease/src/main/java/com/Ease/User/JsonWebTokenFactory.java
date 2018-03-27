package com.Ease.User;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.HttpServletException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.*;

public class JsonWebTokenFactory {
    private static JsonWebTokenFactory ourInstance = new JsonWebTokenFactory();

    public static JsonWebTokenFactory getInstance() {
        return ourInstance;
    }

    private JsonWebTokenFactory() {
    }

    public JsonWebToken createJsonWebToken(Integer userId, Integer connectionLifetime, String keyUser, Key secretKey) throws HttpServletException {
        String salt = AES.generateSalt();
        Map<String, Object> claims = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, connectionLifetime);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Long expirationDate = calendar.getTimeInMillis();
        calendar.clear();
        String connectionToken = UUID.randomUUID().toString();
        claims.put("exp", expirationDate);
        claims.put("tok", connectionToken);
        claims.put("id", userId);
        String jwt = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secretKey).compact();
        String jwtCiphered = AES.encrypt(jwt, keyUser);
        String keyUserCiphered = AES.cipherKey(keyUser, connectionToken, salt);
        return new JsonWebToken(Hashing.hash(connectionToken), jwtCiphered, keyUserCiphered, salt, connectionLifetime);
    }
}
