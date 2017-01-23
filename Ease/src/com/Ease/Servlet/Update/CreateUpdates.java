package com.Ease.Servlet.Update;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class CreateUpdates
 */
@WebServlet("/CreateUpdates")
public class CreateUpdates extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateUpdates() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);

		String sessionId = sm.getServletParam("sessionId", true);
		String updates = sm.getServletParam("updates", true);
		
		try {
			Map<String, User> sIdUserMap = (Map<String, User>) sm.getContextAttr("sIdUserMap");
			if ((user = sIdUserMap.get(sessionId)) == null) {
				sm.setResponse(ServletManager.Code.Success, "1 Please stock update.");
			} else {
				if (updates == null) {
					throw new GeneralException(ServletManager.Code.ClientError, "Empty scrap.");
				}
				user.getUpdateManager().addUpdatesFromJsonConnected(updates, sm);
				sm.setResponse(ServletManager.Code.Success, "2 Update sended.");
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
