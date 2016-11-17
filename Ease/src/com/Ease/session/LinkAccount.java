package com.Ease.session;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;


public class LinkAccount extends Account{

	enum LinkAccountData {
		NOTHING,
		LINK,
		ACCOUNT_ID
	}
	private String	link;
	
	//Use this to create a new account and set it in database
		public LinkAccount(String link, User user, ServletContext context) throws SessionException {
			DataBase db = (DataBase)context.getAttribute("DataBase");			
			try {
				db.set("INSERT INTO accounts VALUES (NULL);");
				db.set("INSERT INTO linkAccounts VALUES ('"+ link +"',  LAST_INSERT_ID());");

				ResultSet rs = db.get("SELECT LAST_INSERT_ID();");

				rs.next();
				this.id = rs.getString(1);
				this.link = link;
				this.type = "LinkAccount";
			} catch (SQLException e) {
				throw new SessionException("Impossible to insert new link account in data base. (no str1)");
			}	

		}

		//Use this to load account with a ResultSet from database
		public LinkAccount(ResultSet rs, User user, ServletContext context) throws SessionException {
			try {
				type = "LinkAccount";
				id = rs.getString(LinkAccountData.ACCOUNT_ID.ordinal());
				link = rs.getString(LinkAccountData.LINK.ordinal());
			} catch (SQLException e) {
				throw new SessionException("Impossible to get all classic account info.");
			}
		}
		
		// GETTER

		public String getLink() {
			return link;
		}
		// SETTER

		public void setLink(String link) {
			this.link = link;
		}

		// UTILS

		public void updateInDB(ServletContext context, String keyUser) throws SessionException {
			DataBase db = (DataBase)context.getAttribute("DataBase");
			
			try {
				db.set("UPDATE linkAccounts SET `link`='"+ link + "' WHERE account_id=" + id + ";");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SessionException("Impossible to update link account in data base.");
			}
		}

		public void updateInDB(ServletContext context) throws SessionException {
			DataBase db = (DataBase)context.getAttribute("DataBase");
			try {
				db.set("UPDATE classicAccounts SET link='" + link + "' WHERE account_id=" + id + ";");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SessionException("Impossible to update link account in data base.");
			}
		}

		public void deleteFromDB(ServletContext context) throws SessionException {
			DataBase db = (DataBase)context.getAttribute("DataBase");
			try {
				db.set("DELETE FROM linkAccounts WHERE account_id=" + id + ";");
				db.set("DELETE FROM accounts WHERE account_id=" + id + ";");
			} catch (SQLException e) {
				throw new SessionException("Impossible to delete account in data base.");
			}
		}

		@Override
		public Map<String, String> getVisibleInformations() {
			return new HashMap<String, String>();
		}

		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return null;
		}
}
