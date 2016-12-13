package com.Ease.Servlet.Profile;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class EditProfileColor
 */
@WebServlet("/EditProfileColor")
public class EditProfileColor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProfileColor() {
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
		
		try {
			sm.needToBeConnected();
			String color = sm.getServletParam("color", true);
			String profileId = sm.getServletParam("profileId", true);
			if (color == null || !Regex.isColor(color))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong color.");
			else if (profileId == null || profileId.isEmpty())
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong profileId.");
			user.getProfile(Integer.parseInt(profileId)).setColor(color, sm);
			sm.setResponse(ServletManager.Code.Success, "Color changed.");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (NumberFormatException e) {
			sm.setResponse(ServletManager.Code.ClientError, "Wrong numbers.");
		}
		sm.sendResponse();
	}
}
