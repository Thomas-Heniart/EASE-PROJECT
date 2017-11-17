package com.Ease.User;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
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

    public JsonWebToken createJsonWebToken(Integer user_id, String keyUser, Key secretKey) throws HttpServletException {
        try {
            String salt = AES.generateSalt();
            Map<String, Object> claims = new HashMap<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 3);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Long expiration_date = calendar.getTimeInMillis();
            calendar.clear();
            String connection_token = UUID.randomUUID().toString();
            claims.put("exp", expiration_date);
            claims.put("tok", connection_token);
            claims.put("id", user_id);
            String jwt = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secretKey).compact();
            String jwt_ciphered = AES.encrypt(jwt, keyUser);
            String keyUser_ciphered = AES.cipherKey(keyUser, connection_token, salt);
            JsonWebToken jsonWebToken = new JsonWebToken(Hashing.hash(connection_token), jwt_ciphered, keyUser_ciphered, salt);
            return jsonWebToken;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }
}
