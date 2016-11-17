package com.Ease.session.update;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.data.AES;
import com.Ease.data.RSA;
import com.Ease.session.SessionException;
import com.Ease.session.User;

public class ClassicUpdate extends Update {
	public enum ClassicUpdateData {
		NOTHING,
		ID,
		UPDATE_ID,
		PASSWORD
	}
	protected	String	cryptedPassword;
	
	public ClassicUpdate(String DBid, String websiteId, String login, String kPass, String knowId)  throws SessionException {
		this.DBid = DBid;
		this.knowId = knowId;
		this.websiteId = websiteId;
		this.login = login;
		this.type = "classic";
		this.cryptedPassword = kPass;		
	}
	
	public String getCryptedPassword() {
		return cryptedPassword;
	}
	
	public void removeFromDB(ServletContext context) {
		DataBase db = (DataBase) context.getAttribute("DataBase");
		
		boolean transaction = false;
		try {
			transaction = db.start();
			db.set("DELETE FROM logwithUpdates WHERE update_id=" + this.DBid + ";");
			db.set("DELETE FROM Updates WHERE id=" + this.DBid + ";");
			db.set("INSERT INTO removedUpdate values(NULL, " + this.websiteId + ", " + this.login + ", NULL);");
			db.commit(transaction);
		} catch (SQLException e) {
			try {
				db.cancel(transaction);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static ClassicUpdate CreateClassicUpdate(String websiteId, String login, String kPass, User user, ServletContext context) throws SessionException {
		
		DataBase db = (DataBase) context.getAttribute("DataBase");
		String privateK = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIMD9dN2sYKTvReHaqpdUmlqE/iae8OX/jU/WgU22du4cDDFkKWc2t/D0NSDqXWwYoGbFtenwyoyu4Gu2WygwysryAz//prIEZBhOnIP9BBtTsjomL+3etoeIuf2d42r4WdCPBmyfI6ZAu2tSTPmGgA3+GDMhPjOW4Sd6rBte5M3AgMBAAECgYBukpYTPXZ9HNEXHYvRcueN3iAyzbaskgfSyr/f7LYbcWMGVodIrsQu3CXQZbpMgGvytICny4e0gpKr1XTc8CUmdi1DN249pt3iil0Q/PdpGpNnqjGlJlcMlp3KlrsfMttGIWCCHTz9d/j781/GD9S7WFMbi6aez8Xi3rzY0y6JQQJBAPHNlp3M7ZqhA09OhNZI9N64GBrFdMzgngsOgKwhSz57lCgiy0ZQKkcKlJcYMHCACNQBR6EVH1O1Sq7t+HJ0TFcCQQCKtS7gaY3wqqdeBewwOFQJf1SC7qUN1408D+wZYkxXZsrBBKlZH151+g01HDrnjY6P6zaX5u951zuxrlkzWqQhAkEA7Bd1KSwvDpyJs8SRlPx1AoUzG+iRq3zhMyB86BQ1+JMGzM10NnoNXYHqJUD8AswwUnfRbWlHRh8sBXLa8z20TwJAI0pJrOruJAcnIfLbzsDmEKyGsfFJqSXoVxmt9h9eUPZkK4umEni6rcV6ysJt8i+/z7oGX8tvrk4mb+Rt6XTsQQJBANTIeRpLnM9EEqrTXWY/o8nhksnuVzavY3D1DE8CCjmbkugc+SHs6NsqABRnzYz5+6AIKWG2zmZVJFEkC7xV5jY=";
		
		ClassicUpdate update;
		boolean transaction = false;
		
		try {
			String pass = RSA.Decrypt(kPass, privateK);
			String cryptedPassword;
			if ((cryptedPassword = AES.encrypt(pass, user.getUserKey())) == null) {
				throw new SessionException("encrypt error");
			}
			transaction = db.start();
			String DBid = db.set("INSERT INTO updates VALUES (" + user.getId() + ", " + websiteId + ", " + login + ", 'classic');").toString();
			db.set("INSERT INTO classicUpdates VALUES (" + DBid + ", '" + cryptedPassword + "');");
			update = new ClassicUpdate(DBid, websiteId, login, cryptedPassword, user.getNextKnowId());
			db.commit(transaction);
			return update;
		} catch (SQLException e) {
			try {
				db.cancel(transaction);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new SessionException("db error");
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			try {
				db.cancel(transaction);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new SessionException("decrypt error");
		}
	}
}