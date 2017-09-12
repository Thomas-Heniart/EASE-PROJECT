package com.Ease.Servlet;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class EditPassword
 */
@WebServlet("/EditPassword")
public class EditPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditPassword() {
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

		String oldPassword = sm.getServletParam("oldPassword", false);
		String password = sm.getServletParam("password", false);
		String confirmPassword = sm.getServletParam("confirmPassword", false);
		
		try {
			sm.needToBeConnected();
            if (oldPassword == null) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong current password.");
			} else if (password == null || !Regex.isPassword(password)) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong new password.");
			} else if (confirmPassword == null || !confirmPassword.equals(password)) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Passwords doesn't match.");
			}
			user.getKeys().changePassword(password, sm);
			sm.setResponse(ServletManager.Code.Success, "Password changed.");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
