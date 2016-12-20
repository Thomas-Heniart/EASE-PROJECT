package com.Ease.Servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class ClassicAppsTransfert
 */
@WebServlet("/ClassicAppsTransfert")
public class ClassicAppsTransfert extends HttpServlet {
	
	public enum AppData {
		NOTHING,
		APP_ID,
		ACCOUNT_ID,
		WEBSITE_ID,
		PROFILE_ID,
		POSITION,
		NAME,
		CUSTOM
	}
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClassicAppsTransfert() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		try {
			ResultSet appsRs = db.get("SELECT * FROM test.apps WHERE website_id IS NOT NULL AND account_id IS NOT NULL AND account_id IN (SELECT account_id FROM test.classicAccounts)");
			try {
				while (appsRs.next()) {
					int transaction = db.startTransaction();
					String name = appsRs.getString(AppData.NAME.ordinal());
					String app_info_id = db.set("INSERT INTO appsInformations VALUES (null, '" + name + "');").toString();
					String profile_id = appsRs.getString(AppData.PROFILE_ID.ordinal());
					String position = appsRs.getString(AppData.POSITION.ordinal());
					String custom = appsRs.getString(AppData.CUSTOM.ordinal());
					if (!(custom == null)) {
						switch(custom) {
							case "1":
							break;
						
							case "3":
							custom = "2";
							break;
							
							case "5":
							custom = "3";
							break;
							
							case "6":
							custom = "4";
							break;
							
							default:
							custom = String.valueOf(Integer.parseInt(custom) - 4);
							break;
						}
					} else {
						custom = "null";
					}
					String app_id = db.set("INSERT INTO ease.apps values (null, " + profile_id + ", " + position + ", default, 'websiteApp', " + app_info_id + ", " + custom + ");").toString();
					String website_id = appsRs.getString(AppData.WEBSITE_ID.ordinal());
					String website_app_id = db.set("INSERT INTO ease.websiteApps values (null, " + website_id + ", " + app_id + ", " + custom + ", 'classicApp');").toString();
					String account_id = appsRs.getString(AppData.ACCOUNT_ID.ordinal());
					ResultSet passwordRs = db.get("SELECT information_value FROM test.ClassicAccountsInformations WHERE account_id = " + account_id + " AND information_name = 'password';");
					passwordRs.next();
					String passwd = passwordRs.getString(1);
					String new_account_id = db.set("INSERT INTO ease.accounts values (null, '" + passwd + "', 0);").toString();
					ResultSet infoRs = db.get("SELECT information_name, information_value FROM test.ClassicAccountsInformations WHERE account_id = " + account_id + " AND information_name NOT LIKE 'password';");
					while (infoRs.next()) {
						String info_name = infoRs.getString(1);
						String info_value = infoRs.getString(2);
						db.set("INSERT INTO ease.accountsInformations values (null, " + new_account_id + ", '" + info_name +"', '" + info_value + "')");
					}
					db.set("INSERT INTO ease.classicApps values (null, " + website_app_id + ", " + new_account_id + ", null);");
					db.commitTransaction(transaction);
					sm.setResponse(ServletManager.Code.Success, "Successfully transfert classic apps");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new GeneralException(ServletManager.Code.InternError, e);
			}
			
		} catch (GeneralException e) {
			e.printStackTrace();
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
