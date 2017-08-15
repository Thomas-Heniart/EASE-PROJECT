package com.Ease.NewDashboard.User;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.*;

import javax.persistence.*;

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

    public Keys(String password, String saltEase, String saltPerso, String keyUser) {
        this.password = password;
        this.saltEase = saltEase;
        this.saltPerso = saltPerso;
        this.keyUser = keyUser;
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

    public boolean isGoodPassword(String password, ServletManagerHibernate sm) throws GeneralException {
        if (saltEase != null) {
            System.out.println("reset keys");
            String hashedPass = Hashing.SHA(password, saltEase);
            if (hashedPass.equals(this.password) == false) {
                throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
            }
            this.decipheredKeyUser = AES.oldDecryptUserKey(this.keyUser, password, saltPerso);
            String newSalt = AES.generateSalt();
            this.keyUser = AES.encryptUserKey(this.decipheredKeyUser, password, newSalt);
            this.password = Hashing.hash(password);
            this.saltEase = null;
            this.saltPerso = newSalt;
            DatabaseRequest request = sm.getDB().prepareRequest("UPDATE userKeys SET password = ?, saltEase = null, saltPerso = ?, keyUser = ?, backUpKey = ? WHERE id = ?;");
            request.setString(this.password);
            request.setString(newSalt);
            request.setString(this.keyUser);
            request.setString("");
            request.setInt(this.db_id);
            request.set();
        }
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
        return AES.encrypt(data, this.decipheredKeyUser);
    }

    public String decrypt(String data) throws GeneralException {
        return AES.decrypt(data, this.decipheredKeyUser);
    }

    public static Keys createKeys(String password, ServletManagerHibernate sm) throws GeneralException {
        String saltPerso = AES.generateSalt();
        String keyUser = AES.keyGenerator();
        String crypted_keyUser = AES.encryptUserKey(keyUser, password, saltPerso);
        String hashed_password = Hashing.hash(password);
        return new Keys(hashed_password, null, saltPerso, crypted_keyUser);
    }

    public void setDecipheredKeyUser(String decipheredKeyUser) {
        this.decipheredKeyUser = decipheredKeyUser;
    }

    public void decryptUserKey(String password) throws GeneralException, HttpServletException {
        this.decipheredKeyUser = AES.decryptUserKey(this.keyUser, password, this.saltPerso);
    }

    public String getDecipheredKeyUser() {
        return this.decipheredKeyUser;
    }
}
