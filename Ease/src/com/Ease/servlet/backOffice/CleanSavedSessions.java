package com.Ease.servlet.backOffice;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.SessionException;
import com.Ease.session.SessionSave;
import com.Ease.session.User;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/cleanSavedSessions")
public class CleanSavedSessions extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public CleanSavedSessions() {
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
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		ServletItem SI = new ServletItem(ServletItem.Type.CleanSavedSessions, request, response, user);
		String retMsg;

		if(user == null){
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
		} else if(!user.isAdmin(session.getServletContext())){
			SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
		} else {
			if(db.set("DELETE FROM savedSessions WHERE datetime < SUBTIME(CURRENT_TIMESTAMP, '2 0:0:0.0');") != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "Error when deleting sessions.");
			} else {
				SI.setResponse(200, "Sessions saved clean");
			}
		}
		 SI.sendResponse();
	}

}
