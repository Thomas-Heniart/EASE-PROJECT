package com.Ease.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import com.Ease.Utils.*;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;

public class ServerKey {
	public enum Data{
		NOTHING,
		LOGIN,
		PASSWORD,
		SALT,
		KEYSERVER
	}

	private String 	user;
	private String	hashed_password;
	private String	salt;
	private String	keyServer;

	private static ServerKey loadServerKey(String login, String password, DataBaseConnection db) throws GeneralException, HttpServletException {
		DatabaseRequest request = db.prepareRequest("SELECT * FROM serverKeys WHERE login= ?;");
		request.setString(login);
		DatabaseResult rs = request.get();
		if (rs.next()) {
			String hashed_password = rs.getString(Data.PASSWORD.ordinal());
			String salt = rs.getString(Data.SALT.ordinal());
			String crypted_keyUser = rs.getString(Data.KEYSERVER.ordinal());
			if (!Hashing.compare(password, hashed_password)) {
				throw new GeneralException(ServletManager.Code.UserMiss, "Wrong login or password (login : "+login+").");
			} else {
				String keyServer = AES.decryptUserKey(crypted_keyUser, password, salt);
				return new ServerKey(login, hashed_password, salt, keyServer);
			}
		} else {
			throw new GeneralException(ServletManager.Code.UserMiss, "Wrong login or password (login : "+login+").");
		}
	}

	public static ServerKey loadServerKey(DataBaseConnection db) throws GeneralException, SQLException, HttpServletException {
		DatabaseResult rs = db.prepareRequest("SELECT login FROM serverKeys;").get();
		if(!rs.next()){ //S'il  n'y a pas encore de serverKey dans la BDD, on crée la première, qui a pour login/passwd : root/root
			System.out.println("Initializing the server key.");
			System.out.println("Creating root user.");
			return createServerKey("root","root", AES.keyGenerator(), db);
		} else {
			String login, password;
			if(Variables.LOCAL){
				login = "root";
				password = "root";
			} else {
				String[] loginFileContent = getLoginFileContent();
				login = loginFileContent[0];
				password = loginFileContent[1];
				cleanLoginFileContent();
			}
			return loadServerKey(login, password, db);
		}
	}

	private static ServerKey createServerKey(String login, String password, String keyServer, DataBaseConnection db) throws GeneralException{
		String salt = AES.generateSalt();
		String hashed_password = Hashing.hash(password);
		DatabaseRequest request = db.prepareRequest("INSERT INTO serverKeys VALUES (?, ?, ?, ?);");
		request.setString(login);
		request.setString(hashed_password);
		request.setString(salt);
		request.setString(AES.encryptUserKey(keyServer, password, salt));
		request.set();
		return new ServerKey(login, hashed_password, salt, keyServer);
	}

	public static ServerKey createServerKey(String newLogin, String newPassword, String login, String password, DataBaseConnection db) throws GeneralException, HttpServletException {
		ServerKey sK = loadServerKey(login, password, db);
		String serverKey = sK.getKeyServer();
		String salt = AES.generateSalt();
		String hashed_password = Hashing.hash(newPassword);
		DatabaseRequest request = db.prepareRequest("INSERT INTO serverKeys VALUES (?, ?, ?, ?);");
		request.setString(newLogin);
		request.setString(hashed_password);
		request.setString(salt);
		request.setString(AES.encryptUserKey(serverKey, newPassword, salt));
		request.set();
		return new ServerKey(newLogin, hashed_password, salt, serverKey);
	}

	public static void eraseServerKey(String login, String password, DataBaseConnection db) throws GeneralException{
		DatabaseRequest request = db.prepareRequest("SELECT * FROM serverKeys WHERE login= ?;");
		request.setString(login);
		DatabaseResult rs = request.get();
		if (rs.next()) {
			String hashed_password = rs.getString(Data.PASSWORD.ordinal());
			if (!Hashing.compare(password, hashed_password)) {
				throw new GeneralException(ServletManager.Code.UserMiss, "Wrong login or password.");
			} else {
				rs = db.prepareRequest("SELECT * FROM serverKeys").get();
				rs.next();
				if(!rs.next()){
					throw new GeneralException(ServletManager.Code.UserMiss, "Last admin. Don't erase it !");
				} else {
					request = db.prepareRequest("DELETE FROM serverKeys WHERE login = ?;");
					request.setString(login);
					request.set();
				}
			}
		} else {
			throw new GeneralException(ServletManager.Code.UserMiss, "Wrong login or password.");
		}
	}

	public ServerKey(String login, String hashed_pass, String salt, String keyServer){
		this.user = login;
		this.hashed_password = hashed_pass;
		this.salt = salt;
		this.keyServer = keyServer;
	}

	public String getKeyServer() {
		return keyServer;
	}

	private static void cleanLoginFileContent() throws GeneralException{
		try {
			PrintWriter writer = new PrintWriter(Variables.SERVER_LOGIN_PATH);
			writer.close();
			return;
		} catch (FileNotFoundException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}

	}

	private static String[] getLoginFileContent() throws GeneralException {
		String ligne ;
		String[] res = {"",""};
		try {
			BufferedReader fichier = new BufferedReader(new FileReader(Variables.SERVER_LOGIN_PATH));
			if ((ligne = fichier.readLine()) != null) {
				res[0] = ligne;
				if ((ligne = fichier.readLine()) != null) {
					System.out.println(ligne);
					res[1] = ligne;
				}
			}
			fichier.close();
			return res;
		} catch (NumberFormatException | IOException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}
