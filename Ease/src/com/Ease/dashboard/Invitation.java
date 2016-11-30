package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Invitation {
	
	public static Group verifyInformations(String email, String invitationCode, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Map<String, Group> groups = (Map<String, Group>) sm.getContextAttr("groups");
		ResultSet rs = db.get("SELECT group_id FROM invitations WHERE email='" + email + "' AND invitationCode='" + invitationCode + "'");
		try {
			if (rs.next())
				return groups.get(rs.getString(1));
			else {
				return null;
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}
