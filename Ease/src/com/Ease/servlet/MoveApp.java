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
import com.Ease.session.App;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class MoveApp
 */
@WebServlet("/moveApp")
public class MoveApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoveApp() {
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
			int appId = Integer.parseInt(request.getParameter("appId"));
			App app = null;
			
			int profileId = Integer.parseInt(request.getParameter("profileId"));
			int index = Integer.parseInt(request.getParameter("index"));
			Profile profile = null;
			
			user = (User)(session.getAttribute("User"));	
			
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.EditProfile, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else if ((app = user.getApp(appId)) == null){
				retMsg = "error: Bad appId";
			} else if ((profile = user.getProfile(profileId)) == null){
				retMsg = "error: Bad profileId";
			} else if (index < 0 || index > profile.getApps().size()){
				retMsg = "error: Bad index";
			} else {
				Profile profileBegin = user.getProfile(app.getProfileId());
				System.out.println(app.getIndex());
				profileBegin.getApps().remove(app.getIndex());
				//if (index > app.getIndex())
				//	index--;
				profile.getApps().add(index, app);
				profileBegin.updateIndex(session.getServletContext());
				profile.updateIndex(session.getServletContext());
				if (profile.getProfileId() != profileBegin.getProfileId())
					app.updateProfileIdnDB(session.getServletContext(), profile.getId(), profile.getProfileId());
				retMsg = "success";
			}
		} catch (SessionException e) {
			retMsg = "error :" + e.getMsg();
		} catch (NumberFormatException e) {
			retMsg = "error: Bad number.";
		}
		response.getWriter().print(retMsg);	
	}

}
