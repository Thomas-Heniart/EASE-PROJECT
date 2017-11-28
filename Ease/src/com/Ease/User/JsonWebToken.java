package com.Ease.User;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.HttpServletException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.persistence.*;
import java.security.Key;
import java.util.*;

@Entity
@Table(name = "jsonWebTokens")
public class JsonWebToken {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "connection_token_hash")
    private String connection_token_hash;

    @Column(name = "jwt_ciphered")
    private String jwt_ciphered;

    @Column(name = "keyUser_cipher")
    private String keyUser_ciphered;

    @Column(name = "salt")
    private String salt;

    @Column(name = "expiration_date")
    private Long expiration_date;

    public JsonWebToken() {

    }

    public JsonWebToken(String connection_token_hash, String jwt_ciphered, String keyUser_ciphered, String salt) {
        this.connection_token_hash = connection_token_hash;
        this.jwt_ciphered = jwt_ciphered;
        this.keyUser_ciphered = keyUser_ciphered;
        this.salt = salt;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.expiration_date = calendar.getTimeInMillis();
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getConnection_token_hash() {
        return connection_token_hash;
    }

    public void setConnection_token_hash(String connection_token_hash) {
        this.connection_token_hash = connection_token_hash;
    }

    public String getJwt_ciphered() {
        return jwt_ciphered;
    }

    public void setJwt_ciphered(String jwt_ciphered) {
        this.jwt_ciphered = jwt_ciphered;
    }

    public String getKeyUser_ciphered() {
        return keyUser_ciphered;
    }

    public void setKeyUser_ciphered(String keyUser_ciphered) {
        this.keyUser_ciphered = keyUser_ciphered;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Long expiration_date) {
        this.expiration_date = expiration_date;
    }

    public boolean isGoodConnectionToken(String connection_token) {
        return Hashing.compare(connection_token, this.getConnection_token_hash());
    }

    public String getKeyUser(String jwt, Key secret) throws HttpServletException {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(jwt).getBody();
        String connection_token = (String) claims.get("tok");
        return AES.decryptUserKey(this.getKeyUser_ciphered(), connection_token, this.getSalt());
    }

    public void renew(String keyUser, Integer user_id, Key secret) throws HttpServletException {
        this.setSalt(AES.generateSalt());
        Map<String, Object> claims = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.setExpiration_date(calendar.getTimeInMillis());
        calendar.clear();
        String connection_token = UUID.randomUUID().toString();
        claims.put("exp", this.getExpiration_date());
        claims.put("tok", connection_token);
        claims.put("id", user_id);
        String jwt = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        this.setJwt_ciphered(AES.encrypt(jwt, keyUser));
        this.setKeyUser_ciphered(AES.cipherKey(keyUser, connection_token, this.getSalt()));
        this.setConnection_token_hash(Hashing.hash(connection_token));
    }

    public String getJwt(String keyUser) {
        return AES.decrypt(this.getJwt_ciphered(), keyUser);
    }
}