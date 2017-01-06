package com.Ease.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
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
	
	protected String 	user;
	protected String	hashed_password;
	protected String	salt;
	protected String	keyServer;
	
	public static ServerKey loadServerKey(DataBaseConnection db) throws GeneralException, SQLException {
		ResultSet rs = db.get("SELECT login FROM serverKeys;");
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
			rs = db.get("SELECT * FROM serverKeys WHERE login='" + login + "';");
			if (rs.next()) {
				String hashed_password = rs.getString(Data.PASSWORD.ordinal());
				String salt = rs.getString(Data.SALT.ordinal());
				String crypted_keyUser = rs.getString(Data.KEYSERVER.ordinal());
				if (!Hashing.compare(password, hashed_password)) {
					throw new GeneralException(ServletManager.Code.InternError, "Wrong login or password (login : "+login+").");
				} else {
					String keyServer = AES.decryptUserKey(crypted_keyUser, password, salt);
					return new ServerKey(login, hashed_password, salt, keyServer);
				}
			} else {
				throw new GeneralException(ServletManager.Code.InternError, "Wrong login or password (login : "+login+").");
			}
		}
	}
	
	public static ServerKey createServerKey(String login, String password, String keyServer, DataBaseConnection db) throws GeneralException{
		String salt = AES.generateSalt();
		String hashed_password = Hashing.hash(password);
		db.set("INSERT INTO serverKeys VALUES ('"+login+"','"+ hashed_password +"', '"+salt+"', '"+AES.encryptUserKey(keyServer, password, salt)+"');");
		return new ServerKey(login, hashed_password, salt, keyServer);
	}
	
	public static void eraseServerKey(String login, String password, DataBaseConnection db) throws GeneralException{
		ResultSet rs = db.get("SELECT * FROM serverKeys WHERE login='" + login + "';");
		try {
			if (rs.next()) {
				String hashed_password = rs.getString(Data.PASSWORD.ordinal());
				if (!Hashing.compare(password, hashed_password)) {
					throw new GeneralException(ServletManager.Code.InternError, "Wrong login or password.");
				} else {
					rs = db.get("SELECT * FROM serverKeys");
					rs.next();
					if(!rs.next()){
						throw new GeneralException(ServletManager.Code.InternError, "Last admin. Don't erase it !");
					} else {
						db.set("DELETE FROM serverKeys WHERE login='"+login+"';");
					}
				}
			} else {
				throw new GeneralException(ServletManager.Code.InternError, "Wrong login or password.");
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
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
