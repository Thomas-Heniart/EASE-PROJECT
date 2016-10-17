package com.Ease.servlet;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.data.ServletItem;
import com.Ease.session.SessionException;
import com.Ease.session.SessionSave;
import com.Ease.session.User;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/logout")
public class Logout extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("logout.jsp");
		rd.forward(request, response);
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		SessionSave sessionSave = (SessionSave)(session.getAttribute("SessionSave"));
		String retMsg;
		try {
			sessionSave.erase(session.getServletContext());
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
			retMsg = "Logged out.";
		} catch (SessionException e) {
			retMsg = "Error when deleting session from db.";
		}
		ServletItem SI = new ServletItem(ServletItem.Type.Logout, request, response, user);
		 session.invalidate();
		 SI.setResponse(200, retMsg);
		 SI.sendResponse();
	}

}
