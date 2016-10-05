package com.Ease.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;

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
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.EditProfileColor, request, response, user);
		
		// Get Parameters
		String color = SI.getServletParam("color");
		String indexParam = SI.getServletParam("index");
		// --
		
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		Profile profile = null;
		try {
			int index = Integer.parseInt(indexParam);
						
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else if ((profile = user.getProfile(index)) == null){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad profileId.");
			} else if (color == null || Regex.isColor(color) == false){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad color.");
			} else {
				if (profile.havePerm(Profile.ProfilePerm.COLOR, session.getServletContext())){
					profile.setColor(color);
					profile.updateInDB(session.getServletContext());
					SI.setResponse(200, "Profile's color changed.");
				} else {
					SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
				}
			}
		} catch (SessionException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (NumberFormatException e) {
			SI.setResponse(ServletItem.Code.BadParameters, "Bad numbers.");
		}
		SI.sendResponse();
	}

}
