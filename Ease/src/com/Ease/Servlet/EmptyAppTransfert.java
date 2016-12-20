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
 * Servlet implementation class EmptyAppTransfert
 */
@WebServlet("/EmptyAppTransfert")
public class EmptyAppTransfert extends HttpServlet {
	
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
    public EmptyAppTransfert() {
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
			ResultSet appsRs = db.get("SELECT * FROM test.apps WHERE website_id IS NOT NULL AND account_id IS NULL AND custom IS NOT NULL;");
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
							
							case "24":
							throw new GeneralException(ServletManager.Code.InternError, "WTF biatch");
							
							default:
							custom = String.valueOf(Integer.parseInt(custom) - 5);
							break;
						}
					} else {
						custom = "null";
					}
					String app_id = appsRs.getString(AppData.APP_ID.ordinal());
					db.set("INSERT INTO ease.apps values (" + app_id + ", " + profile_id + ", " + position + ", default, 'websiteApp', " + app_info_id + ", " + custom + ");");
					String website_id = appsRs.getString(AppData.WEBSITE_ID.ordinal());
					db.set("INSERT INTO ease.websiteApps values (null, " + website_id + ", " + app_id + ", " + custom + ", 'websiteApp');").toString();
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
