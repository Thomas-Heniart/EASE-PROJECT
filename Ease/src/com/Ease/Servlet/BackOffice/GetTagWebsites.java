package com.Ease.Servlet.BackOffice;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Variables;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class GetWebsiteTags
 */
@WebServlet("/GetTagWebsites")
public class GetTagWebsites extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTagWebsites() {
        super();
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
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		User user = sm.getUser();
		try {
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "Not an admin");
			String single_id = sm.getServletParam("single_id", true);
			try {
				int id = Integer.parseInt(single_id);
				JSONArray res = new JSONArray();
				Catalog catalog = (Catalog) sm.getContextAttr("catalog");
				catalog.getTagWithSingleId(id).getWebsites().forEach((website) -> {
					JSONObject tmp = new JSONObject();
					tmp.put("single_id", website.getDb_id());
					tmp.put("imgSrc", Variables.URL_PATH + website.getFolder() + "logo.png");
					tmp.put("name", website.getName());
					res.add(tmp);
				});;
				sm.setResponse(ServletManager.Code.Success, res.toString());
				sm.setLogResponse("GetTagWebsites done");
			} catch(NumberFormatException e) {
				throw new GeneralException(ServletManager.Code.ClientError, e);
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
