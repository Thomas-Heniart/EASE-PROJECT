package com.Ease.Servlet.BackOffice;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class GetUnregisteredEmails
 */
@WebServlet("/GetUnregisteredEmails")
public class GetUnregisteredEmails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUnregisteredEmails() {
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
			/*String date = sm.getServletParam("date", true);
			if (date == null || date.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Choose a date");
			*/
			JSONArray res = new JSONArray();
			DatabaseRequest db_request = db.prepareRequest("SELECT args, DATE(date) FROM logs WHERE servlet_name LIKE ? AND code = ? AND date >= ?");
			DatabaseRequest db_request2 = db.prepareRequest("SELECT id FROM users WHERE email = ?");
			db_request.setString("%CheckInvitation");
			db_request.setInt(200);
			//db_request.setString(date);
			db_request.setString("2017-03-19");
			DatabaseResult rs = db_request.get();
			while(rs.next()) {
				String arg = rs.getString(1);
				arg = arg.replace("%40", "@");
				arg = arg.replace("%3E", "");
				arg = arg.replace("%3C", "");
				arg = arg.replace("%3A", " ");
				arg = arg.split(" ")[2];
				db_request2.setString(arg);
				if (db_request2.get().next())
					continue;
				JSONObject tmp = new JSONObject();
				tmp.put("email", arg);
				tmp.put("date", rs.getString(2));
				res.add(tmp);
			}
			sm.setResponse(ServletManager.Code.Success, res.toString());
			sm.setLogResponse("Get unregistred emails done");
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
