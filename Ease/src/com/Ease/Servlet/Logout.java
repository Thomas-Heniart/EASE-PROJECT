package com.Ease.Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.SessionSave;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
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
		User user = (User)session.getAttribute("user");
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		String retMsg;
		try {
			user.getSessionSave().eraseFromDB(sm);
			Cookie 	cookie = null;
			Cookie 	cookies[] = request.getCookies();
			if (cookies != null){
				for (int i = 0;i < cookies.length ; i++) {
					cookie = cookies[i];
					if((cookie.getName()).compareTo("sId") == 0){
						cookie.setValue("");
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					} else if((cookie.getName()).compareTo("sTk") == 0){
						cookie.setValue("");
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					}
				}
			}
			user.deconnect(sm);
			session.invalidate();
			retMsg = "Logged out.";
			sm.setResponse(ServletManager.Code.Success, retMsg);
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
