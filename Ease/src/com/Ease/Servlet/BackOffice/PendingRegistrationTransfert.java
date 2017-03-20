package com.Ease.Servlet.BackOffice;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class PendingRegistrationTransfert
 */
@WebServlet("/PendingRegistrationTransfert")
public class PendingRegistrationTransfert extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PendingRegistrationTransfert() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		User user = sm.getUser();
		DataBaseConnection db = sm.getDB();
		try {
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "You ain't admin dude");
			DatabaseRequest db_request = db.prepareRequest("SELECT args, date FROM logs WHERE code = ? AND servlet_name LIKE ? ORDER BY date;");
			DatabaseRequest db_request2;
			db_request.setInt(200);
			db_request.setString("%CheckInvitation");
			DatabaseResult rs = db_request.get();
			while (rs.next()) {
				String arg = rs.getString(1);
				arg = arg.replace("%40", "@");
				arg = arg.replace("%3E", "");
				arg = arg.replace("%3C", "");
				arg = arg.replace("%3A", " ");
				String[] split = arg.split(" ");
				arg = split[split.length - 1];
				db_request2 = db.prepareRequest("SELECT id FROM users WHERE email = ?;");
				db_request2.setString(arg);
				if (db_request2.get().next())
					continue;
				db_request2 = db.prepareRequest("INSERT INTO pendingRegistrations values(?, ?, ?);");
				db_request2.setNull();
				db_request2.setString(arg);
				db_request2.setString(rs.getString(2));
				db_request2.set();
			}
			sm.setResponse(ServletManager.Code.Success, "Transfert done.");
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
