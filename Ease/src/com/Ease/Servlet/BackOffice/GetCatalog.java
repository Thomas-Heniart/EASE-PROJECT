package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Variables;
import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class GetCatalog
 */
@WebServlet("/GetCatalog")
public class GetCatalog extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetCatalog() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		User user = sm.getUser();
		try {
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "You are not an admin");
			Catalog catalog = (Catalog) sm.getContextAttr("catalog");
			GroupManager groupManager = (GroupManager) sm.getContextAttr("groupManager");
			JSONArray res = new JSONArray();
			for(Website website : catalog.getWebsitesAlphabetically()) {
				JSONObject tmp = new JSONObject();
				tmp.put("single_id", website.getSingleId());
				tmp.put("imgSrc", Variables.URL_PATH + website.getFolder() + "logo.png");
				String name = website.getName();
				List<String> infraNames = new LinkedList<String>();
				for (String groupId : website.getGroupIds()) {
					String infraName = groupManager.getGroupFromDBid(groupId).getInfra().getName();
					if (infraNames.contains(infraName))
						continue;
					infraNames.add(infraName);
				}
				for (String infraName : infraNames) {
					int idx = infraNames.indexOf(infraName);
					if (idx == 0)
						name += " (";
					name += infraName;
					if (idx != infraNames.size() - 1)
						name += ", ";
					else
						name += ")";
				}
				tmp.put("name", name);
				tmp.put("isTagged", catalog.isWebsiteTagged(website));
				res.add(tmp);
			}
			sm.setResponse(ServletManager.Code.Success, res.toString());
			sm.setLogResponse("GetCatalog done");
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
