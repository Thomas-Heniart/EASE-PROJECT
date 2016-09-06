package com.Ease.servlet;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.context.SiteManager;
import com.Ease.session.Account;
import com.Ease.session.App;
import com.Ease.session.LogWith;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class AskInfo
 */

public class UpdateWebsites extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateWebsites() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String retMsg;
		
		HttpSession session = request.getSession();
		SiteManager sites = ((SiteManager)session.getServletContext().getAttribute("Sites"));
		
		    try {
		    	DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		    	ResultSet rs = db.get("SELECT * FROM websites;");
		    	sites.clear();
		    	while (rs.next()) {
		    		sites.add(new Site(rs));
		    	}
		    } catch (SQLException e) {
		    	System.out.println("Fail to load websites.");
		    	return ;
		    }
		
		retMsg = "success";
		response.getWriter().print(retMsg);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
