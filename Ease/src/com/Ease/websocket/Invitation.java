package com.Ease.websocket;

import com.Ease.utils.CodeGenerator;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class Invitation {

	public static void createInvitation(String email, String groupId, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String invitationCode = CodeGenerator.generateNewCode();
		int transaction = db.startTransaction();
		
		db.commitTransaction(transaction);
	}
	
}
