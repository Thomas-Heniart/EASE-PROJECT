package com.Ease.Dashboard.User;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import java.math.BigInteger;
import java.security.Key;
import java.util.*;

public class JWToken {

    private Integer id;
    private String jwt;
    private String user_id;
    private String connection_token;
    private String keyUser;
    private String user_email;
    private String user_name;
    private Date expiration_date;

    public static JWToken createJWTokenForUser(User user, Key secret, DataBaseConnection db) throws HttpServletException {
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
            claims.put("email", user.getEmail());
            claims.put("name", user.getFirstName());
            claims.put("tok", connection_token);
            String jwt = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
            String jwt_ciphered = user.getKeys().encrypt(jwt);
            String keyUser_ciphered = AES.cipherKey(user.getKeys().getKeyUser(), connection_token, salt);
            DatabaseRequest request = db.prepareRequest("INSERT INTO jsonWebTokens VALUES (NULL, ?, ?, ?, ?, ?, ?)");
            request.setString(Hashing.hash(connection_token));
            request.setString(jwt_ciphered);
            request.setString(keyUser_ciphered);
            request.setString(salt);
            request.setInt(user.getDBid());
            request.setObject(expiration_date);
            Integer id = request.set();
            JWToken jwToken = new JWToken(id, jwt, user.getEmail(), user.getFirstName(), new Date(expiration_date));
            jwToken.setConnection_token(connection_token);
            return jwToken;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static JWToken loadJWTokenWithKeyUser(Integer id, String user_email, String user_name, String keyUser, Key secret, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("SELECT jwt_ciphered, expiration_date FROM jsonWebTokens WHERE id = ?");
            request.setInt(id);
            DatabaseResult rs = request.get();
            rs.next();
            String jwt = AES.decrypt(rs.getString(1), keyUser);
            Claims claimsJws = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody();
            String connection_token = (String) claimsJws.get("tok");
            Long expiration_date = ((BigInteger) rs.getObject(2)).longValueExact();
            if (expiration_date <= new Date().getTime() || !claimsJws.get("name").equals(user_name)) {
                String salt = AES.generateSalt();
                Map<String, Object> claims = new HashMap<>();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                calendar.set(Calendar.HOUR_OF_DAY, 3);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                expiration_date = calendar.getTimeInMillis();
                calendar.clear();
                connection_token = UUID.randomUUID().toString();
                claims.put("exp", expiration_date);
                claims.put("email", user_email);
                claims.put("name", user_name);
                claims.put("tok", connection_token);
                jwt = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
                String jwt_ciphered = AES.encrypt(jwt, keyUser);
                String keyUser_ciphered = AES.cipherKey(keyUser, connection_token, salt);
                request = db.prepareRequest("UPDATE jsonWebTokens SET jwt_ciphered = ?, expiration_date = ?, connection_token_hash = ?, keyUser_cipher = ?, salt = ? WHERE id = ?;");
                request.setString(jwt_ciphered);
                request.setObject(expiration_date);
                request.setString(Hashing.hash(connection_token));
                request.setString(keyUser_ciphered);
                request.setString(salt);
                request.setInt(id);
                request.set();
            }
            JWToken jwToken = new JWToken(id, jwt, user_email, user_name, new Date(expiration_date));
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

    public JWToken(Integer id, String jwt, String user_email, String user_name, Date expiration_date) {
        this.id = id;
        this.jwt = jwt;
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

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
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
