package com.Ease.Dashboard.User;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tomcat.util.codec.binary.Base64;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;

public class SessionSave {
	public enum SessionSaveData {
		NOTHING,
		ID,
		SESSIONID,
		TOKEN,
		SALTTOKEN,
		KEYUSER,
		SALTUSER,
		USER,
		DATE
	}
	
	public static SessionSave loadSessionSave(String sessionId, String oldToken, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("select * from savedSessions where sessionId = '" + sessionId + "';");
			if (rs.next()){
				String oldSessionId = rs.getString(SessionSaveData.SESSIONID.ordinal());
				String oldSaltToken = rs.getString(SessionSaveData.SALTTOKEN.ordinal());
				String oldHashedToken = rs.getString(SessionSaveData.TOKEN.ordinal());			
				String oldSaltUser = rs.getString(SessionSaveData.SALTUSER.ordinal());
				String cryptedKeyUser = rs.getString(SessionSaveData.KEYUSER.ordinal());			
				String userId = rs.getString(SessionSaveData.USER.ordinal());
				String hashedToken;
				String token = tokenGenerator();
				sessionId = sessionIdGenerator();
				String keyUser;
				String saltToken;
				String saltKeyUser;

				if (!oldHashedToken.equals(Hashing.SHA(oldToken, oldSaltToken))) {
					throw new GeneralException(ServletManager.Code.ClientError, "Wrong token.");
				} else if((keyUser = AES.decryptUserKey(cryptedKeyUser, oldToken, oldSaltUser)) == null){
					throw new GeneralException(ServletManager.Code.InternError, "Can't decrypt key user.");
				} else if ((saltToken = Hashing.generateSalt()) == null){
					throw new GeneralException(ServletManager.Code.InternError, "Can't create salt.");
				} else if ((saltKeyUser = AES.generateSalt()) == null){
					throw new GeneralException(ServletManager.Code.InternError, "Can't create salt.");
				} else if ((cryptedKeyUser = AES.encryptUserKey(keyUser, token, saltKeyUser)) == null) {
					throw new GeneralException(ServletManager.Code.InternError,"Can't encrypt key.");
				} else if ((hashedToken = Hashing.SHA(token, saltToken)) == null) {
					throw new GeneralException(ServletManager.Code.InternError,"Can't hash token.");
				}
				int transaction = db.startTransaction();
				db.set("DELETE FROM savedSessions WHERE sessionId = '"+ oldSessionId +"';");
				db.set("INSERT INTO savedSessions VALUES (NULL, '" + sessionId + "', '" + hashedToken + "', '" + saltToken + "', '" + cryptedKeyUser + "', '" + saltKeyUser + "', '" + userId + "', DEFAULT);");
				db.commitTransaction(transaction);
				SessionSave sessionSave = new SessionSave(saltToken, saltKeyUser, token, sessionId, keyUser, userId);
				return sessionSave;
			} else {
				throw new GeneralException(ServletManager.Code.ClientError, "Wrong session id.");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}

	}
	
	public static SessionSave createSessionSave(String keyUser, String userId, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String cryptedKeyUser;
		String hashedToken;
		String token = tokenGenerator();
		String sessionId = sessionIdGenerator();
		String saltToken;
		String saltKeyUser;
		if ((saltToken = Hashing.generateSalt()) == null){
			throw new GeneralException(ServletManager.Code.InternError, "Can't create salt.");
		} else if ((saltKeyUser = AES.generateSalt()) == null){
			throw new GeneralException(ServletManager.Code.InternError, "Can't create salt.");
		} else if ((cryptedKeyUser = AES.encryptUserKey(keyUser, token, saltKeyUser)) == null) {
			throw new GeneralException(ServletManager.Code.InternError, "Can't encrypt key.");
		} else if ((hashedToken = Hashing.SHA(token, saltToken)) == null) {
			throw new GeneralException(ServletManager.Code.InternError, "Can't hash token.");
		} 
		db.set("INSERT INTO savedSessions VALUES (NULL, '" + sessionId + "', '" + hashedToken + "', '" + saltToken + "', '" + cryptedKeyUser + "', '" + saltKeyUser + "', '" + userId + "', DEFAULT);");
		SessionSave sessionSave = new SessionSave(saltToken, saltKeyUser, token, sessionId, keyUser, userId);
		return sessionSave;
	}
	
	private String saltToken;
	private String saltKeyUser;
	private String token;
	private String sessionId;
	private String keyUser;
	private String userId;

	//Create a new session save
	public SessionSave(String saltToken, String saltKeyUser, String token, String sessionId, String keyUser, String userId){
		this.saltToken = saltToken;
		this.saltKeyUser = saltKeyUser;
		this.token = token;
		this.sessionId = sessionId;
		this.keyUser = keyUser;
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public String getKeyUser(){
		return keyUser;
	}

	public String getToken(){
		return token;
	}

	public String getSessionId(){
		return sessionId;
	}

	public void eraseFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("DELETE FROM savedSessions WHERE sessionId = '"+ sessionId +"';");
	}

	public static String tokenGenerator(){
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[42];
		random.nextBytes(bytes);
		return new Base64().encodeToString(bytes);
	}

	public static String sessionIdGenerator(){
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[42];
		random.nextBytes(bytes);
		return new Base64().encodeToString(bytes);
	}

}
