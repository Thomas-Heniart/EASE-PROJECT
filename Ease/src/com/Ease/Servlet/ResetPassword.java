package com.Ease.Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.Keys;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class ResetPassword
 */
@WebServlet("/ResetPassword")
public class ResetPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetPassword() {
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
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);

		String email = sm.getServletParam("email", true);
		String code = sm.getServletParam("code", true);
		String password = sm.getServletParam("password", false);
		String confirmPassword = sm.getServletParam("confirmPassword", false);
		
		try {
			if (user != null) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "You are logged on Ease.");
			} else if (email == null || email.equals("")) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong email.");
			} else if (code == null || code.equals("")) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong code.");
			} else if (password == null || Regex.isPassword(password)) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong password.");
			} else if (confirmPassword == null || !confirmPassword.equals(password)) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Passwords are not same.");
			}
			String userId = User.findDBid(email, sm);
			Keys.resetPassword(userId, password, sm);
			sm.setResponse(ServletManager.Code.Success, "Account trunced and password set.");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
