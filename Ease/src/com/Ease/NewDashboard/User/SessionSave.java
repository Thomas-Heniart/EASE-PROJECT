package com.Ease.NewDashboard.User;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.Session;

import javax.persistence.*;
import java.security.SecureRandom;

/**
 * Created by thomas on 26/04/2017.
 */
@Entity
@Table(name = "SavedSessions")
public class SessionSave {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "sessionId")
    protected String sessionId;

    @Column(name = "sessionToken")
    protected String sessionToken;

    @Column(name = "keyUser")
    protected String keyUser;

    @Column(name = "saltUser")
    protected String saltUser;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    @Transient
    protected String decipheredKeyUser;

    public SessionSave(String sessionId, String sessionToken, String keyUser, String saltUser, User user) {
        this.sessionId = sessionId;
        this.sessionToken = sessionToken;
        this.keyUser = keyUser;
        this.saltUser = saltUser;
        this.user = user;
    }

    public SessionSave(String sessionId, String sessionToken, String keyUser, String saltUser) {
        this.sessionId = sessionId;
        this.sessionToken = sessionToken;
        this.keyUser = keyUser;
        this.saltUser = saltUser;
    }

    public SessionSave() {
    }

    public SessionSave(String decipheredKeyUser, User user) {
        this.decipheredKeyUser = decipheredKeyUser;
        this.sessionId = this.randomBytes();
        String token = this.randomBytes();
        this.sessionToken = Hashing.hash(token);
        this.saltUser = AES.generateSalt();
        this.keyUser = AES.encryptUserKey(this.decipheredKeyUser, token, this.saltUser);
        this.user = user;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getKeyUser() {
        return keyUser;
    }

    public void setKeyUser(String keyUser) {
        this.keyUser = keyUser;
    }

    public String getSaltUser() {
        return saltUser;
    }

    public void setSaltUser(String saltUser) {
        this.saltUser = saltUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDecipheredKeyUser() {
        return decipheredKeyUser;
    }

    public void decryptKeyUser(String sessionToken) throws GeneralException {
        if (!Hashing.compare(sessionToken, this.sessionToken)) {
            throw new GeneralException(ServletManager.Code.ClientError, "Wrong token.");
        } else if((this.decipheredKeyUser = AES.decryptUserKey(this.keyUser, sessionToken, this.saltUser)) == null){
            throw new GeneralException(ServletManager.Code.InternError, "Can't decrypt key user.");
        }
    }

    public String randomBytes() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[42];
        random.nextBytes(bytes);
        return new Base64().encodeToString(bytes);
    }
}
