package com.Ease.Dashboard.User;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.security.Key;
import java.util.*;

public class JWToken {

    private Integer id;
    private String user_id;
    private String connection_token;
    private String keyUser;
    private String user_email;
    private String user_name;
    private Date expiration_date;

    public static JWToken renewJWToken(Integer user_id, String keyUser, String user_email, String user_name, Key secret, DataBaseConnection db) throws HttpServletException {
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
            claims.put("email", user_email);
            claims.put("name", user_name);
            claims.put("tok", connection_token);
            String jwt = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
            String jwt_ciphered = AES.encrypt(jwt, keyUser);
            String keyUser_ciphered = AES.cipherKey(keyUser, connection_token, salt);
            DatabaseRequest request = db.prepareRequest("SELECT id FROM jsonWebTokens WHERE user_id = ?;");
            request.setInt(user_id);
            DatabaseResult rs = request.get();
            Integer id;
            if (rs.next()) {
                id = rs.getInt(1);
                request = db.prepareRequest("UPDATE jsonWebTokens SET connection_token_hash= ?, jwt_ciphered = ?, keyUser_cipher= ?, salt = ?, expiration_date = ? WHERE user_id = ?)");
                request.setString(Hashing.hash(connection_token));
                request.setString(jwt_ciphered);
                request.setString(keyUser_ciphered);
                request.setString(salt);
                request.setObject(expiration_date);
                request.setInt(user_id);
                request.set();
            } else {
                request = db.prepareRequest("INSERT INTO jsonWebTokens VALUES (NULL, ?, ?, ?, ?, ?, ?)");
                request.setString(Hashing.hash(connection_token));
                request.setString(jwt_ciphered);
                request.setString(keyUser_ciphered);
                request.setString(salt);
                request.setInt(user_id);
                request.setObject(expiration_date);
                id = request.set();
            }
            JWToken jwToken = new JWToken(id, user_email, user_name, new Date(expiration_date));
            jwToken.setConnection_token(connection_token);
            return jwToken;
        } catch (GeneralException e) {
             throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static JWToken loadJWToken(String connection_token, String user_email, String user_name, Long expiration_date, Key secret, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("SELECT jsonWebTokens.* FROM jsonWebTokens JOIN users ON jsonWebTokens.user_id = users.id WHERE users.email = ? AND users.firstName = ? AND jsonWebTokens.expiration_date = ?;");
            request.setString(user_email);
            request.setString(user_name);
            request.setObject(expiration_date);
            DatabaseResult rs = request.get();
            if (!rs.next())
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid token.");
            if (!Hashing.compare(connection_token, rs.getString("connection_token_hash")))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid token.");
            String keyUser = AES.decryptUserKey(rs.getString("keyUser_cipher"), connection_token, rs.getString("salt"));
            JWToken jwToken = new JWToken();
            jwToken.setKeyUser(keyUser);
            jwToken.setUser_id(rs.getString("user_id"));
            jwToken.setExpiration_date(new Date(expiration_date));
            jwToken.setUser_email(user_email);
            jwToken.setUser_name(user_name);
            jwToken.setConnection_token(connection_token);
            return jwToken;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError);
        }
    }

    public JWToken() {

    }

    public JWToken(Integer id, String user_email, String user_name, Date expiration_date) {
        this.id = id;
        this.user_email = user_email;
        this.user_name = user_name;
        this.expiration_date = expiration_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(String keyUser) {
        this.keyUser = keyUser;
    }

    public String getConnection_token() {
        return connection_token;
    }

    public void setConnection_token(String connection_token) {
        this.connection_token = connection_token;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getJwt(Key secret) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("exp", this.getExpiration_date().getTime());
        claims.put("email", this.getUser_email());
        claims.put("name", this.getUser_name());
        claims.put("tok", this.getConnection_token());
        return  Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public void checkJwt(Claims claims) throws HttpServletException {
        try {
            String user_name = (String) claims.get("name");
            String user_email = (String) claims.get("email");
            Long expiration_date = (Long) claims.get("exp");
            if (!user_name.equals(this.getUser_name()) || !user_email.equals(this.getUser_email()) || !expiration_date.equals(this.getExpiration_date().getTime()))
                throw new HttpServletException(HttpStatus.AccessDenied, "Invalid JWT");
        } catch (SignatureException e) {
            throw new HttpServletException(HttpStatus.AccessDenied, "Invalid JWT");
        }
    }
}
