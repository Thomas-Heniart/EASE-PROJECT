package com.Ease.User;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

import java.security.Key;
import java.util.Map;

public class UserFactory {
    private static UserFactory ourInstance = new UserFactory();

    public static UserFactory getInstance() {
        return ourInstance;
    }

    private UserFactory() {
    }

    public User createUser(String email, String username, String password, String first_name, String last_name, String phone_number) throws HttpServletException {
        Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
        String saltPerso = AES.generateSalt();
        String keyUser = AES.keyGenerator();
        UserKeys userKeys = new UserKeys(Hashing.hash(password), saltPerso, AES.encryptUserKey(keyUser, password, saltPerso), publicAndPrivateKey.getKey(), AES.encrypt(publicAndPrivateKey.getValue(), keyUser));
        User user = new User(username, email, userKeys, new Options(), new UserStatus());
        user.getPersonalInformation().setFirst_name(first_name);
        user.getPersonalInformation().setLast_name(last_name);
        user.getPersonalInformation().setPhone_number(phone_number);
        user.getUserStatus().setNew_feature_seen(true);
        return user;
    }

    public User createUser(String email, String access_code, String username) throws HttpServletException {
        Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
        String saltPerso = AES.generateSalt();
        String keyUser = AES.keyGenerator();
        UserKeys userKeys = new UserKeys(Hashing.hash(access_code), saltPerso, AES.encryptUserKey(keyUser, access_code, saltPerso), publicAndPrivateKey.getKey(), AES.encrypt(publicAndPrivateKey.getValue(), keyUser));
        User user = new User(username, email, userKeys, new Options(), new UserStatus());
        user.getUserStatus().setNew_feature_seen(true);
        return user;
    }

    public User loadUserFromJwt(String jwt, Key secretKey, HibernateQuery hibernateQuery) throws HttpServletException {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody();
        } catch (MalformedJwtException e) {
            return null;
        }
        String connection_token = (String) claims.get("tok");
        Long expiration_date = (Long) claims.get("exp");
        Integer user_id = (Integer) claims.get("id");
        User user = this.loadUser(user_id, hibernateQuery);
        if (user == null)
            return null;
        JsonWebToken jsonWebToken = user.getJsonWebToken();
        if (jsonWebToken == null || expiration_date > jsonWebToken.getExpiration_date() || !jsonWebToken.isGoodConnectionToken(connection_token))
            throw new HttpServletException(HttpStatus.AccessDenied, "Invalid JWT");
        return user;
    }

    public User loadUser(Integer user_id, HibernateQuery hibernateQuery) {
        return (User) hibernateQuery.get(User.class, user_id);
    }
}
