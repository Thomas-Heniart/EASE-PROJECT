package com.Ease.Utils;

import java.util.LinkedList;
import java.util.List;

import javax.mail.MessagingException;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.Crypto.CodeGenerator;

public class Invitation {

	public static List<Group> verifyInvitation(String email, String invitationCode, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		List<Group> groups = new LinkedList<Group>();
		GroupManager groupManager = (GroupManager) sm.getContextAttr("groupManager");
		DatabaseRequest request = db.prepareRequest("SELECT id FROM invitations WHERE email= ? AND linkCode= ?;");
		request.setString(email);
		request.setString(invitationCode);
		DatabaseResult rs = request.get();
		if (rs.next()) {
			String id = rs.getString(1);
			request = db.prepareRequest("SELECT group_id FROM invitationsAndGroupsMap WHERE invitation_id= ?;");
			request.setInt(id);
			rs = request.get();
			while (rs.next()) {
				groups.add(groupManager.getGroupFromDBid(rs.getString(1)));
			}
			int transaction = db.startTransaction();
			request = db.prepareRequest("DELETE FROM invitationsAndGroupsMap WHERE invitation_id = ?;");
			request.setInt(id);
			request.set();
			request = db.prepareRequest("DELETE FROM invitations WHERE id = ?;");
			request.setInt(id);
			request.set();
			db.commitTransaction(transaction);
		}
		System.out.println("NBR GROUP: " + groups.size());
		return groups;
	}

	public static void sendInvitation(String email, String name, Group group, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		String invitationCode;
		DatabaseRequest request = db.prepareRequest("SELECT * FROM invitations WHERE email= ?;");
		request.setString(email);
		DatabaseResult rs = request.get();
		if (rs.next())
			invitationCode = rs.getString(4);
		else {
			invitationCode = CodeGenerator.generateNewCode();
			request = db.prepareRequest("INSERT INTO invitations values(NULL, ?, ?, ?);");
			request.setString(name);
			request.setString(email);
			request.setString(invitationCode);
			
			String db_id = request.set().toString();
			if (group != null) {
				request = db.prepareRequest("INSERT INTO invitationsAndGroupsMap values(NULL, ?, ?);");
				request.setInt(db_id);
				request.setInt(group.getDBid());
				request.set();
			}
		}
		try {
			Mail mailToSend;
			mailToSend = new Mail();
			mailToSend.sendInvitationEmail(email, name, invitationCode);
		} catch (MessagingException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public static boolean checkEmail(String email, String name, ServletManager sm) throws GeneralException {
		if (email.endsWith("@ieseg.fr")) {
			GroupManager groupManager = (GroupManager) sm.getContextAttr("groupManager");
			Group iesegStudentGroup = groupManager.getGroupFromDBid("4");
			sendInvitation(email, name, iesegStudentGroup, sm);
			return true;
		}
		if (email.endsWith("@edhec.com")) {
			GroupManager groupManager = (GroupManager) sm.getContextAttr("groupManager");
			Group edhecStudentGroup = groupManager.getGroupFromDBid("22");
			sendInvitation(email, name, edhecStudentGroup, sm);
			return true;
		}
			return false;
	}

	public static void sendFriendInvitation(String email, String friendName, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		try {
			String invitationCode;
			DatabaseRequest request = db.prepareRequest("SELECT * FROM invitations WHERE email= ?;");
			request.setString(email);
			DatabaseResult rs = request.get();
			if (rs.next())
				invitationCode = rs.getString(3);
			else {
				invitationCode = CodeGenerator.generateNewCode();
				request = db.prepareRequest("INSERT INTO invitations values(NULL, ?, ?);");
				request.setString(email);
				request.setString(invitationCode);
				String db_id = request.set().toString();
			}
			Mail mailToSend;
			mailToSend = new Mail();
			mailToSend.sendFriendInvitationEmail(email, friendName, user, invitationCode);
		} catch (MessagingException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}
