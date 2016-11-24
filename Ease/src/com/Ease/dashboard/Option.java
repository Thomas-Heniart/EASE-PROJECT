package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Option {
	public enum OptionData {
		NOTHING,
		ID,
		BACKGROUND_PICKED,
		INFINITE_SESSION
	}
	
	public static Option createOption(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int db_id = db.set("INSERT INTO options values (null, 1, 0);");
		return new Option(String.valueOf(db_id), true, false);
	}

	public static Option loadOption(String db_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT * FROM options WHERE id=" + db_id + ";");
		try {
			if (rs.next())
				return new Option(db_id, rs.getBoolean(OptionData.BACKGROUND_PICKED.ordinal()), rs.getBoolean(OptionData.INFINITE_SESSION.ordinal()));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);	
		}
		return null;
	}
	
	protected String db_id;
	protected boolean background_picked;
	protected boolean infinite_session;

	public Option(String db_id, boolean background_picked, boolean infinite_session) {
		this.db_id = db_id;
		this.background_picked = background_picked;
		this.infinite_session = infinite_session;
	}

	public String getDb_id() {
		return this.db_id;
	}

	public boolean isBackground_picked() {
		return this.background_picked;
	}

	public void setBackground_picked(boolean b, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE options SET background_picked = " + (b ? 1 : 0) + ";");
		this.background_picked = b;
	}

	public boolean isInfinite_session() {
		return this.infinite_session;
	}

	public void setInfinite_session(boolean b, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		db.set("UPDATE options SET infinite_session = " + (b ? 1 : 0) + ";");
		this.infinite_session = b;
	}
}
