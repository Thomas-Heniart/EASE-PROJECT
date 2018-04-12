package com.Ease.User;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import io.jsonwebtoken.Claims;

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
        UserKeys userKeys = new UserKeys();
        userKeys.setHashed_password(Hashing.hash(password));
        userKeys.setKeyUser(AES.encryptUserKey(keyUser, password, saltPerso));
        userKeys.setPublicKey(publicAndPrivateKey.getKey());
        userKeys.setPrivateKey(AES.encrypt(publicAndPrivateKey.getValue(), keyUser));
        userKeys.setSaltPerso(saltPerso);
        User user = new User(username, email, userKeys, new Options(), new UserStatus());
        user.getPersonalInformation().setFirst_name(first_name);
        user.getPersonalInformation().setLast_name(last_name);
        user.getPersonalInformation().setPhone_number(phone_number);
        user.getUserStatus().setOnboardingStep(1);
        user.getUserStatus().setRegistered(true);
        return user;
    }

    public User createUser(String email, String access_code, String username) throws HttpServletException {
        Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
        String saltPerso = AES.generateSalt();
        String keyUser = AES.keyGenerator();
        UserKeys userKeys = new UserKeys(Hashing.hash(access_code), saltPerso, AES.encryptUserKey(keyUser, access_code, saltPerso), publicAndPrivateKey.getKey(), AES.encrypt(publicAndPrivateKey.getValue(), keyUser));
        return new User(username, email, userKeys, new Options(), new UserStatus());
    }

    public User loadUserFromJwt(Claims claims, HibernateQuery hibernateQuery) throws HttpServletException {
        String connectionToken = (String) claims.get("tok");
        Long expirationDate = (Long) claims.get("exp");
        Integer userId = (Integer) claims.get("id");
        User user = this.loadUser(userId, hibernateQuery);
        if (user == null)
            return null;
        /* This block of code seems to be useless */
        /* JsonWebToken jsonWebToken = user.getJsonWebToken();
        if (jsonWebToken == null || expirationDate > jsonWebToken.getExpiration_date() || !jsonWebToken.isGoodConnectionToken(connectionToken))
            throw new HttpServletException(HttpStatus.AccessDenied, "Invalid JWT"); */
        return user;
    }

    public User loadUser(Integer user_id, HibernateQuery hibernateQuery) {
        return (User) hibernateQuery.get(User.class, user_id);
    }
}
