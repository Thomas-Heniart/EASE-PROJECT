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
		KEYUSER,
		SALTUSER,
		USER,
		DATE
	}
	
	public static SessionSave loadSessionSave(String sessionId, String token, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet rs = db.get("select * from savedSessions where sessionId = '" + sessionId + "';");
			if (rs.next()){
				String hashedToken = rs.getString(SessionSaveData.TOKEN.ordinal());			
				String saltKeyUser = rs.getString(SessionSaveData.SALTUSER.ordinal());
				String cryptedKeyUser = rs.getString(SessionSaveData.KEYUSER.ordinal());			
				String userId = rs.getString(SessionSaveData.USER.ordinal());

				String keyUser;

				if (Hashing.compare(hashedToken,token)) {
					throw new GeneralException(ServletManager.Code.ClientError, "Wrong token.");
				} else if((keyUser = AES.decryptUserKey(cryptedKeyUser, token, saltKeyUser)) == null){
					throw new GeneralException(ServletManager.Code.InternError, "Can't decrypt key user.");
				}

				SessionSave sessionSave = new SessionSave(saltKeyUser, token, sessionId, keyUser, userId);
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
		String saltKeyUser;
		if ((saltKeyUser = AES.generateSalt()) == null){
			throw new GeneralException(ServletManager.Code.InternError, "Can't create salt.");
		} else if ((cryptedKeyUser = AES.encryptUserKey(keyUser, token, saltKeyUser)) == null) {
			throw new GeneralException(ServletManager.Code.InternError, "Can't encrypt key.");
		} else if ((hashedToken = Hashing.hash(token)) == null) {
			throw new GeneralException(ServletManager.Code.InternError, "Can't hash token.");
		} 
		db.set("INSERT INTO savedSessions VALUES (NULL, '" + sessionId + "', '" + hashedToken + "', '" + cryptedKeyUser + "', '" + saltKeyUser + "', '" + userId + "', DEFAULT);");
		SessionSave sessionSave = new SessionSave(saltKeyUser, token, sessionId, keyUser, userId);
		return sessionSave;
	}
	
	private String saltKeyUser;
	private String token;
	private String sessionId;
	private String keyUser;
	private String userId;

	//Create a new session save
	public SessionSave(String saltKeyUser, String token, String sessionId, String keyUser, String userId){
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
