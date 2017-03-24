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

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Tag;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class EditTag
 */
@WebServlet("/EditTag")
public class EditTag extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditTag() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		User user = sm.getUser();
		try {
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientWarning, "You ain't admin");
			String single_id = sm.getServletParam("single_id", true);
			String colorId = sm.getServletParam("colorId", true);
			String name = sm.getServletParam("name", true);
			String websiteIdsString = sm.getServletParam("websiteIds", true);
			if (single_id == null || single_id.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty single_id");
			if (colorId == null || colorId.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty colorId");
			if (name == null || name.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name");
			if (websiteIdsString == null || websiteIdsString.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty websiteIds");
			try {
				int id = Integer.parseInt(single_id);
				int color_id = Integer.parseInt(colorId);
				JSONParser parser = new JSONParser();
				JSONArray websiteIds = null;
				websiteIds = (JSONArray) parser.parse(StringEscapeUtils.unescapeHtml4(websiteIdsString));
				Catalog catalog = (Catalog) sm.getContextAttr("catalog");
				Tag tag = catalog.getTagWithSingleId(id);
				List<Website> newWebsites = new LinkedList<Website>();
				for (Object websiteId : websiteIds) {
					int website_id = Integer.parseInt((String) websiteId);
					newWebsites.add(catalog.getWebsiteWithSingleId(website_id));
				}
				tag.edit(name, color_id, newWebsites, sm);
				sm.setResponse(ServletManager.Code.Success, "Tag edited");
			} catch(NumberFormatException | ParseException e) {
				throw new GeneralException(ServletManager.Code.ClientError, e);
			}
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
