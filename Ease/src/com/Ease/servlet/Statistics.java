package com.Ease.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class Statistics
 */
@WebServlet("/Statistics")
public class Statistics extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Statistics() {
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
		User user = (User) session.getAttribute("User");
		ServletItem SI = new ServletItem(ServletItem.Type.AdminStats, request, response, user);
		if (!user.isAdmin(session.getServletContext())) {
			response.getWriter().print("error: You aint admin bro");
			return;
		}
		
		/*Connect db*/
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		db.connect();
		
		/* Get the date selected */
		
		/*java.util.Date utilDate = new java.util.Date();
	    java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    System.out.println(sdf.format(sqlDate));*/
	    String startDate = null;
	    startDate = SI.getServletParam("startDate");
	    String endDate = null;
	    endDate = SI.getServletParam("endDate");
	    String baseRequest = "SELECT count(distinct users.user_id) from stats join users on stats.user_id = users.user_id WHERE";
	    JSONObject jsonRes = new JSONObject();
	    /* Take date in account */
	    /*if (startDate != null || endDate != null)
	    	baseRequest += " WHERE";
	    if (startDate != null)
	    	baseRequest += " date >= '" + startDate + "'";
	    if (endDate != null) {
	    	if (startDate != null)
	    		baseRequest += " AND";
	    	baseRequest += " date <= '" + endDate + "'";
	    }*/
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date sDate = null;
	    Date eDate = null;
		try {
			sDate = sdf.parse(startDate);
			eDate = sdf.parse(endDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	    LocalDate start = sDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    LocalDate end = eDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
	    
	    String dbRequest = null;
	    String afterTuto = SI.getServletParam("registerAndTutoDone");
	    JSONObject registerAndTutoDone = new JSONObject();
	    ResultSet rs = null;
	    JSONArray registerAndTutoDoneArray = new JSONArray();
	    for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
	        dbRequest = baseRequest + " date >= '" + date.toString() + " 00:00:00' AND date <= '" + date.toString() + " 59:59:59'";
	        
	        if (afterTuto != null && afterTuto.equals("on")) {
				rs = db.get(dbRequest + " AND tuto = 1 AND msg = 'success';");
				try {
					while(rs.next()) {
						JSONObject tmpObj = new JSONObject();
						tmpObj.put("value", rs.getString(1));
						tmpObj.put("date", date.toString());
						registerAndTutoDoneArray.add(tmpObj);
					}
				} catch (SQLException e) {
					SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
					e.printStackTrace();
				}
			}
	    }
	    if (!registerAndTutoDoneArray.isEmpty())
	    	jsonRes.put("registerAndTutoDone", registerAndTutoDoneArray);
	    
		SI.setResponse(200, jsonRes.toString());
		SI.sendResponse();
	}

}
