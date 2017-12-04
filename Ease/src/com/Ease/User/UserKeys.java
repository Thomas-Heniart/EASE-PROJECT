package com.Ease.User;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "userKeys")
public class UserKeys {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "password")
    private String hashed_password;

    @Column(name = "saltEase")
    private String saltEase;

    @Column(name = "saltPerso")
    private String saltPerso;

    @Column(name = "keyUser")
    private String keyUser;

    @Column(name = "publicKey")
    private String publicKey;

    @Column(name = "privateKey")
    private String privateKey;

    public UserKeys() {

    }

    public UserKeys(String hashed_password, String saltPerso, String keyUser, String publicKey, String privateKey) {
        this.hashed_password = hashed_password;
        this.saltPerso = saltPerso;
        this.keyUser = keyUser;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(String hashed_password) {
        this.hashed_password = hashed_password;
    }

    public String getSaltEase() {
        return saltEase;
    }

    public void setSaltEase(String saltEase) {
        this.saltEase = saltEase;
    }

    public String getSaltPerso() {
        return saltPerso;
    }

    public void setSaltPerso(String saltPerso) {
        this.saltPerso = saltPerso;
    }

    public String getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(String keyUser) {
        this.keyUser = keyUser;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getDecipheredKeyUser(String password) throws HttpServletException {
        if (!isGoodPassword(password))
            throw new HttpServletException(HttpStatus.BadRequest, "Wrong password.");
        return AES.decryptUserKey(this.getKeyUser(), password, this.getSaltPerso());
    }

    public String getDecipheredPrivateKey(String keyUser) {
        return AES.decrypt(this.getPrivateKey(), keyUser);
    }

    public boolean isGoodPassword(String password) {
        return Hashing.compare(password, this.getHashed_password());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserKeys userKeys = (UserKeys) o;

        return db_id.equals(userKeys.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }

    public void changePassword(String new_password, String keyUser) {
        this.setHashed_password(Hashing.hash(new_password));
        this.setKeyUser(AES.encryptUserKey(keyUser, new_password, this.getSaltPerso()));
    }

    public String generatePublicAndPrivateKey(String keyUser) throws HttpServletException {
        Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
        this.setPublicKey(publicAndPrivateKey.getKey());
        this.setPrivateKey(AES.encrypt(publicAndPrivateKey.getValue(), keyUser));
        return publicAndPrivateKey.getValue();
    }
}