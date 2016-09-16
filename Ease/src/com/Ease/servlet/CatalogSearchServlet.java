package com.Ease.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.SiteManager;
import com.Ease.context.Tag;

/**
 * Servlet implementation class CatalogSearchServlet
 */
@WebServlet("/searchInCatalog")
public class CatalogSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CatalogSearchServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String search = request.getParameter("search");
		String ids = request.getParameter("tagIds");
		String tagIds;
		if (ids == null)
			tagIds = "";
		else
			tagIds = ids.substring(1, ids.length() - 1);
		String[] tagIdsArray;
		if (!tagIds.isEmpty())
			tagIdsArray = tagIds.split(",");
		else
			tagIdsArray = new String[] {};
		SiteManager siteManager = (SiteManager) session.getServletContext().getAttribute("siteManager");
		String res;
		res = siteManager.getSitesListJsonWithSearchAndTags(search, tagIdsArray).toString();
		response.getWriter().print(res);
	}

}
