package com.Ease.dashboard;

import com.Ease.utils.CodeGenerator;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Invitation {

	public static void createInvitation(String email, String groupId, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String invitationCode = CodeGenerator.generateNewCode();
		int transaction = db.startTransaction();
		db.set("INSERT INTO invitations values (null, '" + email + "', ");
		User.createUser(email, null, null, null, sm);
		db.commitTransaction(transaction);
	}
	
}
