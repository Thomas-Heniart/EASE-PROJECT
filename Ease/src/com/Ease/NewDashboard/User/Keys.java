package com.Ease.NewDashboard.User;

import com.Ease.Context.ServerKey;
import com.Ease.Utils.*;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;

import javax.persistence.*;
import javax.servlet.Servlet;

/**
 * Created by thomas on 21/04/2017.
 */
@Entity
@Table(name = "userKeys")
public class Keys {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "password")
    protected String password;

    @Column(name = "saltEase")
    protected String saltEase;

    @Column(name = "saltPerso")
    protected String saltPerso;

    @Column(name = "keyUser")
    protected String keyUser;

    @Transient
    protected String decipheredKeyUser;

    @Column(name = "backUpKey")
    protected String backUpKey;

    public Keys(String password, String saltEase, String saltPerso, String keyUser, String backUpKey) {
        this.password = password;
        this.saltEase = saltEase;
        this.saltPerso = saltPerso;
        this.keyUser = keyUser;
        this.backUpKey = backUpKey;
    }

    public Keys() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isGoodPassword(String password) throws GeneralException {
        if (Hashing.compare(password, this.password))
            return true;
        else
            throw new GeneralException(ServletManager.Code.ClientWarning, "Password does not match");
    }

    public void changePassword(String new_password, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        String new_hashed_password = Hashing.hash(new_password);
        this.keyUser = AES.encryptUserKey(this.decipheredKeyUser, new_password, this.saltPerso);
        this.password = new_hashed_password;
    }

    public String encrypt(String data) throws GeneralException {
        return AES.encrypt(data, this.keyUser);
    }

    public String decrypt(String data) throws GeneralException {
        return AES.decrypt(data, this.keyUser);
    }

    public static Keys createKeys(String password, ServletManagerHibernate sm) throws GeneralException {
        String saltPerso = AES.generateSalt();
        String keyUser = AES.keyGenerator();
        String crypted_keyUser = AES.encryptUserKey(keyUser, password, saltPerso);
        String hashed_password = Hashing.hash(password);
        ServerKey serverKey = (ServerKey) sm.getContextAttr("serverKey");
        String backUpKey = AES.encrypt(keyUser, serverKey.getKeyServer());
        return new Keys(hashed_password, null, saltPerso, crypted_keyUser, backUpKey);
    }

    public void decryptUserKey(String password) throws GeneralException {
        this.decipheredKeyUser = AES.decryptUserKey(this.keyUser, password, this.saltPerso);
    }
}
