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
	
	public GrowthHackingSender(List<String> GMailEmails, List<String> GMailPasswords, List<String> emails,int length) {
		this.GMailEmails = GMailEmails;
		this.GMailPasswords = GMailPasswords;
		this.emails = emails;
		this.j = 0;
		this.threshold = length / GMailEmails.size();
		if (threshold == 0)
			threshold = length;
		this.k = threshold;
		this.r = length - (threshold * GMailEmails.size());
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
			while (j < emails.size()) {
				for (String GMailEmail : GMailEmails) {
					String pass = GMailPasswords.get(GMailEmails.indexOf(GMailEmail));
					Mail testMail = new Mail(GMailEmail, pass);
					email = emails.get(j);
					DatabaseRequest db_request = db.prepareRequest("INSERT INTO testingEmails values(null, ?);");
					db_request.setString(email);
					db_request.set();
					testMail.sendTestEmail(email);
					System.out.println("Email " + (j+1) + "/" + emails.size() + " sent from: " + GMailEmail + " to: " + email);
					j++;
					if (j == emails.size())
						return;
				}
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
