package com.Ease.Update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class UpdateNewClassicApp extends UpdateNewAccount {

	public enum Data{
		NOTHING,
		ID,
		UPDATE_NEW_ACCOUNT_ID,
		PASSWORD
	}
	
	public static Update loadUpdateNewClassicApp(String update_id, String update_new_account_id, User user, Website website, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		IdGenerator idGenerator = (IdGenerator) sm.getContextAttr("idGenerator");
		ResultSet rs = db.get("SELECT * FROM updateNewClassicApp WHERE update_new_account_id = " + update_new_account_id + ";");
		try {
			rs.next();
			String db_id = rs.getString(Data.ID.ordinal());
			String password = rs.getString(Data.PASSWORD.ordinal());
			Map<String, String> updateInformations = ClassicUpdateInformation.loadClassicUpdateInformations(db_id, db);
			return new UpdateNewClassicApp(update_id, website, password, updateInformations, idGenerator.getNextId());
		} catch(SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	protected Map<String, String> updateInformations;
	protected String password;
	
	public UpdateNewClassicApp(String db_id, Website website, String password, Map<String, String> updateInformations, int single_id) {
		super(db_id, website, single_id);
		this.password = password;
		this.updateInformations = updateInformations;
	}
	
	public String getInformation(String information_name) {
		return this.updateInformations.get(information_name);
	}
	
	public String getPassword() {
		return this.password;
	}

}
