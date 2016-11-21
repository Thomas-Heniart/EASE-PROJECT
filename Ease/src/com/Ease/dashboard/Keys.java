package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Keys {
	enum Data {
		NOTHING,
		ID,
		PASSWORD,
		SALTEASE,
		SALTPERSO,
		KEYUSER
	}
	public static Keys loadKeys(String id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM keys WHERE id=" + id + ";");
		try {
			String db_id = rs.getString(Data.ID.ordinal());
			String crypted_password = rs.getString(Data.PASSWORD.ordinal());
			String saltEase = rs.getString(Data.SALTEASE.ordinal());
			String saltPerso = rs.getString(Data.SALTPERSO.ordinal());
			String keyUser = rs.getString(Data.KEYUSER.ordinal());
			return new Keys(db_id, crypted_password, saltEase, saltPerso, keyUser);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	public static Keys createKeys(String password, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		//do your stuff here bro'
		
	}
	
	protected String 	db_id;
	protected String	crypted_password;
	protected String	saltEase;
	protected String	saltPerso;
	protected String	keyUser;
	
	public Keys(String db_id, String crypted_password, String saltEase, String saltPerso, String keyUser) {
		this.db_id = db_id;
		this.crypted_password = crypted_password;
		this.saltEase = saltEase;
		this.saltPerso = saltPerso;
		this.keyUser = keyUser;
	}
}
