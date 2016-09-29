package com.Ease.servlet.backOffice;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class RemoveMember
 */
@WebServlet("/RemoveMember")
public class RemoveMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveMember() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String retMsg;
		User user = null;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		try {
			int groupId = Integer.parseInt(request.getParameter("groupId"));
			String email = request.getParameter("user");
			
			user = (User)(session.getAttribute("User"));	
			
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.EditProfile, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else if (user.isAdmin(session.getServletContext()) == null){
				retMsg = "error: You have not the permission";
			} else {
				ResultSet rs = db.get("select * from users where email=" + email + ";");
				if (rs.next()) {
					String target = rs.getString(1);
					db.set("delete from GroupAndUserMap where user_id=" + target + " AND group_id=" + groupId + "");
					retMsg = "success";
				} else {
					retMsg = "This member do not exist!";
				}
			}
		} catch (NumberFormatException e) {
			retMsg = "error: Bad number.";
		} catch (SQLException e) {
			retMsg = "error: Bad sqlrequest.";
		}
		response.getWriter().print(retMsg);	
	}

}
