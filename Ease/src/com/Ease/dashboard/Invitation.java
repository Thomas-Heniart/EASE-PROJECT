package com.Ease.dashboard;

import java.sql.ResultSet;
import java.util.Map;

import com.Ease.data.Mail;
import com.Ease.utils.CodeGenerator;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Invitation {

	public static void createInvitation(String email, String groupId, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String invitationCode;
		Map<String, Group> groups = (Map<String, Group>)sm.getContextAttr("groups");
		int transaction = db.startTransaction();
		ResultSet rs = db.get("SELECT invitationCode, group_id FROM invitations WHERE email='" + email + "'");
		if (rs.next()) {
			invitationCode = rs.getString(1);
			if (!(groupId.equals(rs.getString(2))))
				throw new GeneralException(ServletManager.Code.InternError, "Bad group id");
		} else {
			invitationCode = CodeGenerator.generateNewCode();
			db.set("INSERT INTO invitations values (null, '" + email + "', '" + invitationCode + "', " + ((groupId == null) ? "null" :  groupId) + ");");
			User.createUser(email, groups.get(groupId), sm);
		}
		
		db.commitTransaction(transaction);
		Mail invitationMail = new Mail();
		invitationMail.sendTheFamilyInvitation(email, invitationCode);
	}
	
}
