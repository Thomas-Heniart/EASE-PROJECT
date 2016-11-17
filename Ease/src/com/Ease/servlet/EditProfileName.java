package com.Ease.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;

/**
 * Servlet implementation class EditProfileName
 */
@WebServlet("/editProfileName")
public class EditProfileName extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProfileName() {
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
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.EditProfileName, request, response, user);
		
		// Get Parameters
		String name = SI.getServletParam("name");
		String indexParam = SI.getServletParam("index");
		// --
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		Profile profile = null;
		
		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}
		
		try {
			int index = Integer.parseInt(indexParam);
					
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if ((profile = user.getProfile(index)) == null){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad profileId");
			} else if (name == null || name == ""){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad name");
			} else {
				if (profile.havePerm(Profile.ProfilePerm.RENAME, session.getServletContext())){
					profile.setName(name);
					profile.updateInDB(session.getServletContext());
					SI.setResponse(200, "Profile's name changed.");
				} else {
					SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
				}
			}
		} catch (SessionException e) {
			SI.setResponse(ServletItem.Code.LogicError, ServletItem.getExceptionTrace(e));
		} catch (NumberFormatException e) {
			SI.setResponse(ServletItem.Code.BadParameters, "Bad numbers.");
		}
		SI.sendResponse();
	}
}
