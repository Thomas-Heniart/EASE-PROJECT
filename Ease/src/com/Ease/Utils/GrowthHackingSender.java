package com.Ease.Utils;

import java.sql.SQLException;
import java.util.List;

public class GrowthHackingSender implements Runnable {

	protected List<String> GMailEmails;
	protected List<String> GMailPasswords;
	protected List<String> emails;
	protected int j;
	protected int k;
	protected int length;
	protected int threshold;
	protected int r;
	protected DataBaseConnection db;
	
	public GrowthHackingSender(List<String> GMailEmails, List<String> GMailPasswords, List<String> emails, int j, int k, int length, int threshold, int r) {
		this.GMailEmails = GMailEmails;
		this.GMailPasswords = GMailPasswords;
		this.emails = emails;
		this.j = j;
		this.k = k;
		this.length = length;
		this.threshold = threshold;
		this.r = r;
		try {
			this.db = new DataBaseConnection(DataBase.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String email = null;
		try {
			System.out.println("Start to send emails");
			for (String GMailEmail : GMailEmails) {
				String pass = GMailPasswords.get(GMailEmails.indexOf(GMailEmail));
				Mail testMail = new Mail(GMailEmail, pass);
				for (j = j; j < k && j < emails.size() ; j++) {
					email = emails.get(j);
					DatabaseRequest db_request = db.prepareRequest("INSERT INTO testingEmails values(null, ?);");
					db_request.setString(email);
					db_request.set();
					testMail.sendTestEmail(email);
					System.out.println("Email " + (j+1) + "/" + emails.size() + " sent from: " + GMailEmail + " to: " + email);
				}
				if (k >= length - r) {
					for (j=j; j < length; j++) {
						email = emails.get(j);
						DatabaseRequest db_request = db.prepareRequest("INSERT INTO testingEmails values(null, ?);");
						db_request.setString(email);
						db_request.set();
						testMail.sendTestEmail(email);
						System.out.println("Email " + (j+1) + "/" + emails.size() + " sent from: " + GMailEmail + " to: " + email);					}
					break;
				}
					
				k = k + threshold;
			}
			System.out.println("Emails sent");
			Mail doneMail = new Mail("thomas@ease.space", "azeqsdwxc1008!!//", "Thomas @Ease");
			doneMail.sendGrowthHackingDoneEmail();
			db.close();
		} catch(GeneralException e) {
			e.printStackTrace();
			if (j < emails.size() - 1 && email != null) {
				try {
					Mail failMail = new Mail("thomas@ease.space", "azeqsdwxc1008!!//", "Thomas @Ease");
					failMail.sendGrowthHackingFailEmail(email);
				} catch (GeneralException e1) {
					e1.printStackTrace();
				}
			}
			db.close();
		}
	}

}
