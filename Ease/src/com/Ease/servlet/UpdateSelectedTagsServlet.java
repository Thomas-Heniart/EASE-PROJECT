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

import org.json.simple.JSONArray;

import com.Ease.context.SiteManager;
import com.Ease.context.Tag;

/**
 * Servlet implementation class UpdateSelectedTagsServelt
 * 
 * @param <Tag>
 */
@WebServlet("/UpdateSelectedTagsServelt")
public class UpdateSelectedTagsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateSelectedTagsServlet() {
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
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String tagId = request.getParameter("tagId");
		SiteManager siteManager = (SiteManager) session.getServletContext().getAttribute("siteManager");
		List<Tag> tags = (List<Tag>) session.getServletContext().getAttribute("tags");
		siteManager.addTag(tags.get(Integer.parseInt(tagId) - 1));
		String res = siteManager.getJson().toString();
		response.getWriter().print(res);
	}

}
