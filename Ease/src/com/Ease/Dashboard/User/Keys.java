package com.Ease.Dashboard.User;

import com.Ease.Context.Variables;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;

import java.util.Map;

public class Keys {

    enum Data {
        NOTHING,
        ID,
        PASSWORD,
        SALTEASE,
        SALTPERSO,
        KEYUSER,
        BACKUPKEY
    }

    public static Keys loadKeys(String id, String password, DataBaseConnection db) throws GeneralException, HttpServletException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM userKeys WHERE id= ?;");
        request.setInt(id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "This key does not exist");
        String db_id = rs.getString(Data.ID.ordinal());
        String hashed_password = rs.getString(Data.PASSWORD.ordinal());
        String saltEase = rs.getString(Data.SALTEASE.ordinal());
        String saltPerso = rs.getString(Data.SALTPERSO.ordinal());
        String crypted_keyUser = rs.getString(Data.KEYUSER.ordinal());
        String publicKey = rs.getString("publicKey");
        String ciphered_privateKey = rs.getString("privateKey");
        String privateKey = null;
        String keyUser;
        //-- Pour mettre à jour la crypto (nouveau hashage et nouveau salage.
        if (saltEase != null) {
            String hashedPass = Hashing.SHA(password, saltEase);
            if (hashedPass.equals(hashed_password) == false) {
                throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
            }
            keyUser = AES.oldDecryptUserKey(crypted_keyUser, password, saltPerso);
            String newSalt = AES.generateSalt();
            crypted_keyUser = AES.encryptUserKey(keyUser, password, newSalt);
            hashed_password = Hashing.hash(password);
            saltPerso = newSalt;
            request = db.prepareRequest("UPDATE userKeys SET password = ?, saltEase = null, saltPerso = ?, keyUser = ? WHERE id = ?;");
            request.setString(hashed_password);
            request.setString(newSalt);
            request.setString(crypted_keyUser);
            request.setInt(id);
            request.set();
        } else {
            //-- Ne garder que le else quand tout le monde sera à jour
            if (!Hashing.compare(password, hashed_password)) {
                throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
            }
            keyUser = AES.decryptUserKey(crypted_keyUser, password, saltPerso);
        }
        if (publicKey == null || publicKey.equals("") || publicKey.equals("NULL")) {
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            publicKey = publicAndPrivateKey.getKey();
            privateKey = publicAndPrivateKey.getValue();
            ciphered_privateKey = AES.encrypt(privateKey, keyUser);
            request = db.prepareRequest("UPDATE userKeys SET publicKey = ?, privateKey = ? WHERE id = ?;");
            request.setString(publicKey);
            request.setString(ciphered_privateKey);
            request.setInt(id);
            request.set();
        } else {
            if (!Hashing.compare(password, hashed_password))
                throw new GeneralException(ServletManager.Code.UserMiss, "Wrong email or password.");
            privateKey = AES.decrypt(ciphered_privateKey, keyUser);
        }
        return new Keys(db_id, hashed_password, saltPerso, keyUser, publicKey, privateKey);
    }

    public static Keys loadKeysWithoutPassword(String id, String keyUser, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM userKeys WHERE id= ?;");
        request.setInt(id);
        DatabaseResult rs = request.get();
        rs.next();
        String db_id = rs.getString(Data.ID.ordinal());
        String hashed_password = rs.getString(Data.PASSWORD.ordinal());
        String saltPerso = rs.getString(Data.SALTPERSO.ordinal());
        String publicKey = rs.getString("publicKey");
        String ciphered_privateKey = rs.getString("privateKey");
        String privateKey = null;
        if (publicKey == null || publicKey.equals("") || publicKey.equals("NULL")) {
            Map<String, String> publicAndPrivateKeys = RSA.generateKeys(1);
            for (Map.Entry<String, String> publicAndPrivateKey : publicAndPrivateKeys.entrySet()) {
                publicKey = publicAndPrivateKey.getKey();
                privateKey = publicAndPrivateKey.getValue();
            }
            ciphered_privateKey = AES.encrypt(privateKey, keyUser);
            request = db.prepareRequest("UPDATE userKeys SET publicKey = ?, privateKey = ? WHERE id = ?;");
            request.setString(publicKey);
            request.setString(ciphered_privateKey);
            request.setInt(id);
            request.set();
        } else {
            privateKey = AES.decrypt(ciphered_privateKey, keyUser);
        }
        return new Keys(db_id, hashed_password, saltPerso, keyUser, publicKey, privateKey);
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public static String getPublicKeyForUser(String user_id, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT publicKey FROM userKeys JOIN users ON users.keys_id = userKeys.id WHERE users.id = ?;");
        request.setInt(user_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "No keys");
        return rs.getString(1);
    }

    public static Keys createKeys(String password, DataBaseConnection db) throws GeneralException {
        String saltPerso = AES.generateSalt();
        String keyUser = AES.keyGenerator();
        String crypted_keyUser = AES.encryptUserKey(keyUser, password, saltPerso);
        String hashed_password = Hashing.hash(password);
        Map<String, String> publicAndPrivateKeys = RSA.generateKeys(1);
        String publicKey = null;
        String privateKey = null;
        for (Map.Entry<String, String> publicAndPrivateKey : publicAndPrivateKeys.entrySet()) {
            publicKey = publicAndPrivateKey.getKey();
            privateKey = publicAndPrivateKey.getValue();
        }
        String privateKey_ciphered = AES.encrypt(privateKey, keyUser);
        DatabaseRequest request = db.prepareRequest("INSERT INTO userKeys VALUES(NULL, ?, null, ?, ?, ?, ?);");
        request.setString(hashed_password);
        request.setString(saltPerso);
        request.setString(crypted_keyUser);
        request.setString(publicKey);
        request.setString(privateKey_ciphered);
        String db_id = request.set().toString();
        return new Keys(db_id, hashed_password, saltPerso, keyUser, publicKey, privateKey);
    }

    protected String db_id;
    protected String hashed_password;
    protected String saltPerso;
    protected String keyUser;
    protected String publicKey;
    protected String privateKey;

    public Keys(String db_id, String hashed_password, String saltPerso, String keyUser, String publicKey, String privateKey) {
        this.db_id = db_id;
        this.hashed_password = hashed_password;
        this.saltPerso = saltPerso;
        this.keyUser = keyUser;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public Keys(String db_id, String hashed_password, String saltPerso, String keyUser, String publicKey) {
        this.db_id = db_id;
        this.hashed_password = hashed_password;
        this.saltPerso = saltPerso;
        this.keyUser = keyUser;
        this.publicKey = publicKey;
    }

    public void removeFromDB(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("DELETE FROM userKeys WHERE id = ?;");
        request.setInt(this.db_id);
        request.set();
    }

	/*
     *
	 * Getter and Setter
	 * 
	 */

    public String getDBid() {
        return db_id;
    }

    public String getKeyUser() {
        return keyUser;
    }

    public String getPrivateKey() {
        return privateKey;
    }

	/*
     *
	 * Utils
	 * 
	 */

    public boolean isGoodPassword(String password) {
        return Hashing.compare(password, hashed_password);
    }

    public void changePassword(String new_password, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        String new_hashed_password = Hashing.hash(new_password);
        String new_crypted_keyUser = AES.encryptUserKey(keyUser, new_password, saltPerso);
        DatabaseRequest request = db.prepareRequest("UPDATE userKeys SET password = ?, saltEase=null, saltPerso = ?, keyUser = ? WHERE id = ?;");
        request.setString(new_hashed_password);
        request.setString(saltPerso);
        request.setString(new_crypted_keyUser);
        request.setInt(this.db_id);
        request.set();
        this.hashed_password = new_hashed_password;
    }

    public String encrypt(String data) throws GeneralException {
        return AES.encrypt(data, this.keyUser);
    }

    public String decrypt(String data) throws GeneralException {
        return AES.decrypt(data, this.keyUser);
    }

    public static void passwordLost(String email, String userId, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        String code = CodeGenerator.generateNewCode();
        String fName = null;
        DatabaseRequest request = db.prepareRequest("SELECT * FROM passwordLost WHERE user_id= ?;");
        request.setInt(userId);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            request = db.prepareRequest("UPDATE passwordLost SET linkCode = ?, dateOfRequest = NOW() WHERE user_id = ?;");
            request.setString(code);
            request.setInt(userId);
        } else {
            request = db.prepareRequest("INSERT INTO passwordLost values (?, ?, default);");
            request.setInt(userId);
            request.setString(code);
        }
        request.set();
        request = db.prepareRequest("SELECT firstName FROM users WHERE id = ?;");
        request.setInt(userId);
        rs = request.get();
        if (rs.next())
            fName = rs.getString(1);
        else
            throw new GeneralException(ServletManager.Code.ClientWarning, "This user does not exist");
        /*SendGridMail passwordLostEmail = new SendGridMail("Agathe @Ease", "contact@ease.space");
        passwordLostEmail.sendPasswordLostEmail(email, fName, code);*/
        db.commitTransaction(transaction);
        MailJetBuilder mailJetBuilder = new MailJetBuilder();
        mailJetBuilder.setTemplateId(178530);
        mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
        mailJetBuilder.addTo(email);
        mailJetBuilder.addVariable("link", Variables.URL_PATH + "resetPassword?email=" + email + "&code=" + code);
        mailJetBuilder.sendEmail();
    }

    public static void resetPassword(String userId, String newPassword, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM passwordLost WHERE user_id= ?;");
        DatabaseRequest request2;
        DatabaseResult rs2;
        request.setInt(userId);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            request = db.prepareRequest("SELECT * FROM profiles WHERE user_id= ?;");
            request.setInt(userId);
            rs = request.get();
            while (rs.next()) {
                request2 = db.prepareRequest("SELECT apps.* FROM apps JOIN profileAndAppMap ON apps.id = profileAndAppMap.app_id WHERE profile_id= ? AND apps.id NOT IN (SELECT pinned_app_id FROM sharedApps);");
                request2.setInt(rs.getString(Profile.Data.ID.ordinal()));
                rs2 = request2.get();
                while (rs2.next()) {
                    if (rs2.getString(App.Data.TYPE.ordinal()).equals("websiteApp"))
                        WebsiteApp.Empty(rs2.getString(App.Data.ID.ordinal()), sm);
                }
            }
            request = db.prepareRequest("SELECT key_id FROM users WHERE id= ?;");
            request.setInt(userId);
            rs = request.get();
            rs.next();
            String saltPerso = AES.generateSalt();
            String keyUser = AES.keyGenerator();
            String crypted_keyUser = AES.encryptUserKey(keyUser, newPassword, saltPerso);
            String hashed_password = Hashing.hash(newPassword);
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            String publicKey = publicAndPrivateKey.getKey();
            String privateKey = AES.encrypt(publicAndPrivateKey.getValue(), keyUser);
            DatabaseRequest request3 = db.prepareRequest("UPDATE userKeys SET password = ?, saltEase=null, saltPerso = ?, keyUser = ?, publicKey = ?, privateKey = ? WHERE id = ?;");
            request3.setString(hashed_password);
            request3.setString(saltPerso);
            request3.setString(crypted_keyUser);
            request3.setString(publicKey);
            request3.setString(privateKey);
            request3.setInt(rs.getString(1));
            request3.set();
        } else {
            throw new GeneralException(ServletManager.Code.ClientWarning, "You did not ask for password resetting.");
        }
        db.commitTransaction(transaction);
    }

    public static boolean checkCodeValidity(String userId, String code, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM passwordLost WHERE (NOW() <= DATE_ADD(dateOfRequest, INTERVAL 2 HOUR)) AND user_id = ? AND linkCode = ?;");
        request.setInt(userId);
        request.setString(code);
        DatabaseResult rs = request.get();
        return rs.next();
    }
}
