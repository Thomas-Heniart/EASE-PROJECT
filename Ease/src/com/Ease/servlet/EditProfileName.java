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
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class EditProfileName
 */
@WebServlet("/EditProfileName")
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
		
		String name = request.getParameter("name");
		HttpSession session = request.getSession();
		String retMsg;
		User user = null;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		Profile profile = null;
		try {
			int index = Integer.parseInt(request.getParameter("index"));
			
			user = (User)(session.getAttribute("User"));	
			
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.EditProfile, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else if ((profile = user.getProfile(index)) == null){
				retMsg = "error: Bad id.";
			} else if (name == null || name == ""){
				retMsg = "error: Bad profile's name.";
			} else {
				profile.setName(name);
				profile.updateInDB(session.getServletContext());
				retMsg = "success";
			}
		} catch (SessionException e) {
			retMsg = "error :" + e.getMsg();
		} catch (NumberFormatException e) {
			retMsg = "error: Bad profile's index.";
		}
		//Stats.saveAction(session.getServletContext(), user, Stats.Action.EditProfile, retMsg);
		response.getWriter().print(retMsg);
	}

}
