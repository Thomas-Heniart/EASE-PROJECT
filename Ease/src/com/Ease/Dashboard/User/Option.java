package com.Ease.Dashboard.User;

import com.Ease.Utils.*;

public class Option {
	public enum OptionData {
		NOTHING,
		ID,
		BACKGROUND_PICKED,
		INFINITE_SESSION,
		HOMEPAGE_STATE
	}
	
	public static Option createOption(DataBaseConnection db) throws GeneralException {
		int db_id = db.prepareRequest("INSERT INTO options values (null, 0, 0, 0);").set();
		return new Option(String.valueOf(db_id), false, false);
	}

	public static Option loadOption(String db_id, DataBaseConnection db) throws GeneralException {
		DatabaseRequest request = db.prepareRequest("SELECT * FROM options WHERE id= ?;");
		request.setInt(db_id);
		DatabaseResult rs = request.get();
		if (rs.next())
			return new Option(db_id, rs.getBoolean(OptionData.BACKGROUND_PICKED.ordinal()), rs.getBoolean(OptionData.INFINITE_SESSION.ordinal()));
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

    public void removeFromDB(DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("DELETE FROM options where id = ?;");
		request.setInt(db_id);
		request.set();
	}

	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDb_id() {
		return this.db_id;
	}

	public boolean isBackground_picked() {
		return this.background_picked;
	}

	public void setBackground_picked(boolean b, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("UPDATE options SET background_picked = ? WHERE id = ?;");
		request.setBoolean(b);
		request.setInt(db_id);
		request.set();
		this.background_picked = b;
	}

	public boolean isInfinite_session() {
		return this.infinite_session;
	}

	public void setInfinite_session(boolean b, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("UPDATE options SET infinite_session = ? WHERE id = ?;");
		request.setBoolean(b);
		request.setInt(db_id);
		request.set();
		this.infinite_session = b;
	}

	public void setHomepageState(boolean state, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("UPDATE options SET homepage_state = ? WHERE id = ?;");
		request.setBoolean(state);
		request.setInt(db_id);
		request.set();
	}
}
