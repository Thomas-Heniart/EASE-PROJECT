package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Dashboard.User.User;
import com.Ease.Dashboard.User.UserEmail;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

import javax.servlet.annotation.WebServlet;


/**
 * Servlet implementation class AddApp
 */
@WebServlet("/addWebsite")
public class AddWebsite extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String UPLOAD_DIRECTORY = "resources/websites/";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddWebsite() {
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
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		Catalog catalog = (Catalog)session.getAttribute("catalog");
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		
		// Get Parameters
		String url = sm.getServletParam("siteUrl", false);
		String name = sm.getServletParam("siteName", true);
		String homePage = sm.getServletParam("homePage", false);
		String folder = sm.getServletParam("siteFolder", false);
		String haveLoginButtonString = sm.getServletParam("haveLoginButton", false);
		boolean haveLoginButton;
		if(haveLoginButtonString == "0"){
			haveLoginButton = false;
		} else {
			haveLoginButton = true;
		}
		String[] haveLoginWith = sm.getServletParamArray("haveLoginWith", false);
		// --
		
		DataBaseConnection db = sm.getDB();

		try {
			sm.needToBeConnected();
			if (!user.isAdmin()) {
				sm.setResponse(ServletManager.Code.ClientWarning, "You are not admin.");
			} else {
				catalog.addWebsite(url, name, homePage, folder, haveLoginButton, haveLoginWith, sm);
				sm.setResponse(ServletManager.Code.Success, "Success");
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
		
	}
}