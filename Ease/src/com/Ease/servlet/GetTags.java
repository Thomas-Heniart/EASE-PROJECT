package com.Ease.servlet;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.Ease.context.SiteManager;
import com.Ease.context.Tag;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class AddApp
 */
@WebServlet("/getTags")
public class GetTags extends HttpServlet {

       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public GetTags() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.GetTags, request, response, user);
		
		// Get Parameters
		String websiteIdParam = SI.getServletParam("websiteId");
		// --
		
		String result = "[{";
		try {
			int websiteId = Integer.parseInt(websiteIdParam);

			List<Tag> tags = ((SiteManager)session.getServletContext().getAttribute("siteManager")).getSiteById(websiteId).getTags();
		
			for (Tag tag : tags){
				result += ("\"id\":" + tag.getId() + ", \"color\": \""+tag.getColor()+"\", \"name\": \""+tag.getName() +"\"},{");
			}
		
			result = result.substring(0, result.length()-2);
			result += "]";
			SI.setResponse(200, result);
		} catch (NumberFormatException e) {
			SI.setResponse(ServletItem.Code.BadParameters, "Bad numbers.");
		} catch (NullPointerException e) {
			SI.setResponse(ServletItem.Code.LogicError, ServletItem.getExceptionTrace(e));
		}
		SI.sendResponse();
	}
}
