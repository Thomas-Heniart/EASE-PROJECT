package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.Ease.data.Mail;
import com.Ease.utils.CodeGenerator;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Invitation {
	
	public static Group verifyInvitation(String email, String invitationCode, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Map<String, Group> groups = (Map<String, Group>) sm.getContextAttr("groups");
		ResultSet rs = db.get("SELECT group_id FROM invitations WHERE email='" + email + "' AND invitationCode='" + invitationCode + "'");
		try {
			if (rs.next()) {
				String groupId = rs.getString(1);
				db.set("DELETE FROM invisitation WHERE id=" + groupId + ";");
				return groups.get(groupId);
			}
				
			else {
				return null;
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static void sendInvitation(String email, String group_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String invitationCode = CodeGenerator.generateNewCode();
		db.set("UPDATE invitations SET linkCode='" + invitationCode + "' WHERE email='" + email + "'");
		Mail mailToSend = new Mail();
		mailToSend.sendInvitationEmail(email, invitationCode);
	}
}
