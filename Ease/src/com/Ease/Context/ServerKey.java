package com.Ease.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;

public class ServerKey {
	public enum Data{
		NOTHING,
		LOGIN,
		PASSWORD,
		SALTEASE,
		SALTPERSO,
		KEYSERVER
	}
	
	protected String	hashed_password;
	protected String	saltEase;
	protected String	saltPerso;
	protected String	keyServer;
	
	public ServerKey(DataBaseConnection db) throws GeneralException, SQLException {
		/*String password;
		String login;
		this.keyServer = null;
		Scanner scan = new Scanner(System.in);
		while (keyServer == null) {
			System.out.println("Login:");
			login = scan.next();
			System.out.println("Password:");
			password = scan.next();
			ResultSet rs = db.get("SELECT * FROM serverKeys WHERE login='" + login + "';");
			if (rs.next()) {
				String hashed_password = rs.getString(Data.PASSWORD.ordinal());
				String saltEase = rs.getString(Data.SALTEASE.ordinal());
				String saltPerso = rs.getString(Data.SALTPERSO.ordinal());
				String crypted_keyUser = rs.getString(Data.KEYSERVER.ordinal());
				String hashedPass = Hashing.SHA(password, saltEase);
				if (hashedPass.equals(hashed_password) == false) {
					System.out.println("Wrong login or password.");
				} else {
					keyServer = AES.decryptUserKey(crypted_keyUser, password, saltPerso);
				}
			} else {
				System.out.println("Wrong login or password.");
			}
		}
		scan.close();
		
		String saltEase = AES.generateSalt();
		String saltPerso = AES.generateSalt();
		String keyUser = AES.keyGenerator();
		String crypted_keyUser = AES.encryptUserKey(keyUser, "lala", saltPerso);
		String hashed_password = Hashing.SHA("lala", saltEase);
		db.set("INSERT INTO serverKeys VALUES('lala', '" + hashed_password + "', '" + saltEase + "', '" + saltPerso + "', '" + crypted_keyUser + "');");*/
	}
	
	public String getKeyServer() {
		return keyServer;
	}
}
