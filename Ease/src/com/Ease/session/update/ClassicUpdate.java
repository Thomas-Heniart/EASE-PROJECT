package com.Ease.session.update;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.Ease.data.AES;
import com.Ease.data.RSA;
import com.Ease.session.User;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class ClassicUpdate extends Update {
	public enum ClassicUpdateData {
		NOTHING,
		ID,
		UPDATE_ID,
		PASSWORD
	}
	protected	String	cryptedPassword;
	
	public ClassicUpdate(String DBid, String websiteId, String login, String kPass, String knowId) {
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
	
	public void removeFromDB(ServletManager sm) throws GeneralException{
		DataBaseConnection db = sm.getDB();
		
		int transaction = db.startTransaction();
		db.set("DELETE FROM logwithUpdates WHERE update_id=" + this.DBid + ";");
		db.set("DELETE FROM Updates WHERE id=" + this.DBid + ";");
		db.set("INSERT INTO removedUpdate values(NULL, " + this.websiteId + ", " + this.login + ", NULL);");
		db.commitTransaction(transaction);
	}
	
	public static ClassicUpdate CreateClassicUpdate(String websiteId, String login, String kPass, User user, ServletManager sm) throws GeneralException {
		
		DataBaseConnection db = sm.getDB();
		String privateK = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIMD9dN2sYKTvReHaqpdUmlqE/iae8OX/jU/WgU22du4cDDFkKWc2t/D0NSDqXWwYoGbFtenwyoyu4Gu2WygwysryAz//prIEZBhOnIP9BBtTsjomL+3etoeIuf2d42r4WdCPBmyfI6ZAu2tSTPmGgA3+GDMhPjOW4Sd6rBte5M3AgMBAAECgYBukpYTPXZ9HNEXHYvRcueN3iAyzbaskgfSyr/f7LYbcWMGVodIrsQu3CXQZbpMgGvytICny4e0gpKr1XTc8CUmdi1DN249pt3iil0Q/PdpGpNnqjGlJlcMlp3KlrsfMttGIWCCHTz9d/j781/GD9S7WFMbi6aez8Xi3rzY0y6JQQJBAPHNlp3M7ZqhA09OhNZI9N64GBrFdMzgngsOgKwhSz57lCgiy0ZQKkcKlJcYMHCACNQBR6EVH1O1Sq7t+HJ0TFcCQQCKtS7gaY3wqqdeBewwOFQJf1SC7qUN1408D+wZYkxXZsrBBKlZH151+g01HDrnjY6P6zaX5u951zuxrlkzWqQhAkEA7Bd1KSwvDpyJs8SRlPx1AoUzG+iRq3zhMyB86BQ1+JMGzM10NnoNXYHqJUD8AswwUnfRbWlHRh8sBXLa8z20TwJAI0pJrOruJAcnIfLbzsDmEKyGsfFJqSXoVxmt9h9eUPZkK4umEni6rcV6ysJt8i+/z7oGX8tvrk4mb+Rt6XTsQQJBANTIeRpLnM9EEqrTXWY/o8nhksnuVzavY3D1DE8CCjmbkugc+SHs6NsqABRnzYz5+6AIKWG2zmZVJFEkC7xV5jY=";
		
		ClassicUpdate update;
		
		try {
			String pass = RSA.Decrypt(kPass, privateK);
			String cryptedPassword;
			if ((cryptedPassword = AES.encrypt(pass, user.getUserKey())) == null) {
				throw new GeneralException(ServletManager.Code.InternError, "Encrypt error.");
			}
			int transaction = db.startTransaction();
			String DBid = db.set("INSERT INTO updates VALUES (" + user.getId() + ", " + websiteId + ", " + login + ", 'classic');").toString();
			db.set("INSERT INTO classicUpdates VALUES (" + DBid + ", '" + cryptedPassword + "');");
			update = new ClassicUpdate(DBid, websiteId, login, cryptedPassword, user.getNextKnowId());
			db.commitTransaction(transaction);
			return update;
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}