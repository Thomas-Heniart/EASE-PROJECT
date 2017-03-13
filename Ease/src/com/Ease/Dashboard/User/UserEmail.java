package com.Ease.Dashboard.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.AccountInformation;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Mail.SendGridMail;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Mail;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Crypto.CodeGenerator;

public class UserEmail {
	
	public enum UserEmailData {
		NOTHING,
		ID,
		USER_ID,
		EMAIL,
		VERIFIED
	}
	
	public static Map<String, UserEmail> loadEmails(String user_id, User user, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		Map<String, UserEmail> emails = new HashMap<String, UserEmail>();
		DatabaseRequest request = db.prepareRequest("SELECT * FROM usersEmails WHERE user_id= ?;");
		request.setInt(user_id);
		DatabaseResult rs = request.get();
		while(rs.next()) {
			String db_id = rs.getString(UserEmailData.ID.ordinal());
			String email = rs.getString(UserEmailData.EMAIL.ordinal());
			boolean verified = rs.getBoolean(UserEmailData.VERIFIED.ordinal());
			emails.put(email, new UserEmail(db_id, email, verified, user));
		}
		return emails;
	}
	
	public static UserEmail createUserEmail(String email, User user, boolean verified, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("INSERT INTO usersEmails VALUES(NULL, ?, ?, ?);");
		request.setInt(user.getDBid());
		request.setString(email);
		request.setBoolean(verified);
		String db_id = request.set().toString();
		return new UserEmail(db_id, email, verified, user);
	}
	
	protected String db_id;
	protected String email;
	protected List<App> appsUsing;
	protected boolean verified;
	protected User user;
	
	public UserEmail(String db_id, String email, boolean verified, User user) {
		this.db_id = db_id;
		this.email = email;
		this.verified = verified;
		this.appsUsing = new LinkedList<App>();
		this.user = user;
	}
	
	public void removeFromDB(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		int transaction = db.startTransaction();
		DatabaseRequest request = db.prepareRequest("DELETE FROM usersEmailsPending WHERE userEmail_id = ?;");
		request.setInt(db_id);
		request.set();
		request = db.prepareRequest("DELETE FROM usersEmails where id = ?;");
		request.setInt(db_id);
		request.set();
		db.commitTransaction(transaction);
	}
	
	/*
	 * 
	 * Getter and Setter
	 * 
	 */
	
	public String getDBid() {
		return db_id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public boolean isVerified() {
		return verified;
	}
	
	/*
	 * 
	 * Utils
	 * 
	 */
	
	public boolean removeIfNotUsed(ServletManager sm) throws GeneralException {
		if (this.verified){
			return false;
		}
		if(user.getEmail().equals(this.email)){
			return false;
		}
		DataBaseConnection db = sm.getDB();
		for(App app : this.user.getDashboardManager().getApps()) {
			if (app.isClassicApp()) {
				List<AccountInformation> appInformations = ((ClassicApp) app).getAccount().getAccountInformations();
				for (AccountInformation accountInformation : appInformations) {
					if (accountInformation.getInformationName().toLowerCase().equals("login")) {
						if (this.email.equals(accountInformation.getInformationValue()))
							return false;
					}
				};
			}
				
		}
		DatabaseRequest request = db.prepareRequest("DELETE FROM usersEmails WHERE id = ?;");
		request.setInt(db_id);
		request.set();
		return true;
	}
	
	public void askForVerification(User user, boolean newUser, ServletManager sm) throws GeneralException{
		try {
			Mail mailToSend = new Mail();
			String code = CodeGenerator.generateNewCode();
			DataBaseConnection db = sm.getDB();
			int transaction = db.startTransaction();
			DatabaseRequest request = db.prepareRequest("SELECT * FROM usersEmailsPending WHERE userEmail_id = ?;");
			request.setInt(this.db_id);
			DatabaseResult rs = request.get();
			if (rs.next())
				code = rs.getString(3);
			else {
				request = db.prepareRequest("INSERT INTO usersEmailsPending VALUES(NULL, ?, ?)");
				request.setInt(db_id);
				request.setString(code);
				request.set();
			}
			if (this.email.equals(user.getEmail())) {
				if (newUser) {
					SendGridMail welcomeEmail = new SendGridMail("Agathe @Ease", "contact@ease.space");
					welcomeEmail.sendWelcomeEmail(user.getFirstName(), email, code);
				} else {
					mailToSend.sendVerificationMainEmail(this.email, code);
				}
			} else {
				mailToSend.sendVerificationEmail(this.email, user.getFirstName(), code);
			}
			db.commitTransaction(transaction);
		} catch (MessagingException e) {
			throw new GeneralException(ServletManager.Code.InternError, "Email not sended.");
		}
	}
	
	public void verifie(String code, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("SELECT * FROM usersEmailsPending WHERE userEmail_id= ?;");
		request.setInt(this.db_id);
		DatabaseResult rs = request.get();
		if (rs.next()) {
			String verificationCode = rs.getString(3);
			if (verificationCode.equals(code)) {
				int transaction = db.startTransaction();
				request = db.prepareRequest("DELETE FROM usersEmailsPending WHERE userEmail_id = ?;");
				request.setInt(db_id);
				request.set();
				this.beVerified(sm);
				db.commitTransaction(transaction);
			}
			else {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong verification code.");
			}
		} else {
			throw new GeneralException(ServletManager.Code.ClientWarning, "This email doesn't need to be verified.");
		}
	}
	
	public void beVerified(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		DatabaseRequest request = db.prepareRequest("UPDATE usersEmails SET verified = 1 WHERE id = ?;");
		request.setString(db_id);
		request.set();
		this.verified = true;
	}
}
