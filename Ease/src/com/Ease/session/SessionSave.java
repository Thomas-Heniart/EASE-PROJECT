package com.Ease.session;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.apache.tomcat.util.codec.binary.Base64;

import com.Ease.context.DataBase;
import com.Ease.data.AES;
import com.Ease.data.Hashing;
import com.Ease.data.ServletItem;

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
	private String saltToken;
	private String saltKeyUser;
	private String token;
	private String sessionId;
	private String keyUser;
	private String userId;

	//Create a new session save
	public SessionSave(User user, ServletContext context) throws SessionException{
		DataBase db = (DataBase)context.getAttribute("DataBase");
		this.keyUser = user.getUserKey();
		this.userId = user.getId();
		String cryptedKeyUser;
		String hashedToken;
		token = tokenGenerator();
		sessionId = sessionIdGenerator();


		if ((saltToken = Hashing.generateSalt()) == null){
			throw new SessionException("Can't create salt.");
		} else if ((saltKeyUser = AES.generateSalt()) == null){
			throw new SessionException("Can't create salt.");
		} else if ((cryptedKeyUser = AES.encryptUserKey(keyUser, token, saltKeyUser)) == null) {
			throw new SessionException("Can't encrypt key.");
		} else if ((hashedToken = Hashing.SHA(token, saltToken)) == null) {
			throw new SessionException("Can't hash token.");
		} 
		try {
			db.set("INSERT INTO savedSessions VALUES (NULL, '" + sessionId + "', '" + hashedToken + "', '" + saltToken + "', '" + cryptedKeyUser + "', '" + saltKeyUser + "', '" + userId + "', DEFAULT);");
		} catch (SQLException e) {
			throw new SessionException("Impossible to insert new sessionSave in data base.");
		}
		context.setAttribute("SessionSave", this);
	}

	//Load session save and update it
	public  SessionSave(ResultSet rs, String oldToken, ServletContext context) throws SessionException{
		DataBase db = (DataBase)context.getAttribute("DataBase");
		try {
			String oldSessionId = rs.getString(SessionSaveData.SESSIONID.ordinal());
			String oldSaltToken = rs.getString(SessionSaveData.SALTTOKEN.ordinal());
			String oldHashedToken = rs.getString(SessionSaveData.TOKEN.ordinal());			
			String oldSaltUser = rs.getString(SessionSaveData.SALTUSER.ordinal());
			String cryptedKeyUser = rs.getString(SessionSaveData.KEYUSER.ordinal());			
			userId = rs.getString(SessionSaveData.USER.ordinal());
			String hashedToken;
			token = tokenGenerator();
			sessionId = sessionIdGenerator();

			if (!oldHashedToken.equals(Hashing.SHA(oldToken, oldSaltToken))) {
				throw new SessionException("Wrong token.");
			} else if((keyUser = AES.decryptUserKey(cryptedKeyUser, oldToken, oldSaltUser)) == null){
				throw new SessionException("Can't decrypt key user.");
			} else if ((saltToken = Hashing.generateSalt()) == null){
				throw new SessionException("Can't create salt.");
			} else if ((saltKeyUser = AES.generateSalt()) == null){
				throw new SessionException("Can't create salt.");
			} else if ((cryptedKeyUser = AES.encryptUserKey(keyUser, token, saltKeyUser)) == null) {
				throw new SessionException("Can't encrypt key.");
			} else if ((hashedToken = Hashing.SHA(token, saltToken)) == null) {
				throw new SessionException("Can't hash token.");
			}
			db.set("DELETE FROM savedSessions WHERE sessionId = '"+ oldSessionId +"';");
			db.set("INSERT INTO savedSessions VALUES (NULL, '" + sessionId + "', '" + hashedToken + "', '" + saltToken + "', '" + cryptedKeyUser + "', '" + saltKeyUser + "', '" + userId + "', DEFAULT);");
		} catch (SQLException e) {
			throw new SessionException("Database error.");
		}
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

	public void erase(ServletContext context) throws SessionException{
		DataBase db = (DataBase) context.getAttribute("DataBase");
		try {
			db.set("DELETE FROM savedSessions WHERE sessionId = '"+ sessionId +"';");
		} catch (SQLException e) {
			throw new SessionException("Can't delete session.");
		}
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
