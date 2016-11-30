package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.mail.MessagingException;

import com.Ease.data.Mail;
import com.Ease.utils.CodeGenerator;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Invitation {

	public static Group verifyInvitation(String email, String invitationCode, ServletManager sm)
			throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Map<String, Group> groups = (Map<String, Group>) sm.getContextAttr("groups");
		ResultSet rs = db.get("SELECT group_id FROM invitations WHERE email='" + email + "' AND linkCode='" + invitationCode + "';");
		try {
			if (rs.next()) {
				String groupId = rs.getString(1);
				db.set("DELETE FROM invitations WHERE id=" + groupId + ";");
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
		Map<String, Group> groups = (Map<String, Group>) sm.getContextAttr("groups");
		String infraName = groups.get(group_id).getInfra().getName();
		String invitationCode = CodeGenerator.generateNewCode();
		db.set("UPDATE invitations SET linkCode='" + invitationCode + "' WHERE email='" + email + "'");

		Mail mailToSend;
		try {
			mailToSend = new Mail();
			mailToSend.sendInvitationEmail(email, infraName, invitationCode);
		} catch (MessagingException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}

	}
}
