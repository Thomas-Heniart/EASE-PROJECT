package com.Ease.Servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class ConnectionServlet
 */
@WebServlet("/connection")
public class ConnectionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int max_attempts = 10;
	private static final long expiration_time = 5; // 5 minutes
	private static final long ONE_MINUTE_IN_MILLIS = 60000;

	public ConnectionServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		// Get Parameters
		String email = sm.getServletParam("email", true);
		String password = sm.getServletParam("password", false);
		// --

		String client_ip = getIpAddr(request);

		// Put current ip in db
		try {
			addIpInDataBase(client_ip, db);
			if (canConnect(client_ip, db)) {
				if (email == null || Regex.isEmail(email) == false)
					sm.setResponse(ServletManager.Code.ClientWarning, "Wrong email");
				else if (password == null)
					sm.setResponse(ServletManager.Code.ClientWarning, "Wrong password");
				else {
					User user = User.loadUser(email, password, sm);
					session.setAttribute("user", user);
					sm.setResponse(ServletManager.Code.Success, "Successfully connected");
				}
			} else {
				throw new GeneralException(ServletManager.Code.UserMiss, "Too much attempts to connect. Please retry in 5 minutes.");
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (null != ip && !"".equals(ip.trim()) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (null != ip && !"".equals(ip.trim()) && !"unknown".equalsIgnoreCase(ip)) {
			// get first ip from proxy ip
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}

	public void addIpInDataBase(String client_ip, DataBaseConnection db) throws GeneralException {
		try {
			ResultSet rs = db.get("SELECT * FROM askingIps WHERE ip='" + client_ip + "';");
			if (rs.next())
				return;
			db.set("INSERT INTO askingIps values (NULL, '" + client_ip + "', 0, '" + getCurrentTime() + "', '"
					+ getExpirationTime() + "');");
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	public String getCurrentTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public String getExpirationTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(new Date(date.getTime() + (expiration_time * ONE_MINUTE_IN_MILLIS)));
	}

	public void removeIpFromDataBase(String client_ip, DataBaseConnection db) throws GeneralException {
		db.set("DELETE FROM askingIps WHERE ip = '" + client_ip + "';");
	}

	public int incrementAttempts(String client_ip, DataBaseConnection db) throws GeneralException {
		System.out.println(getExpirationTime());
		db.set("UPDATE askingIps SET attempts = attempts + 1, attemptDate = '" + getCurrentTime()
				+ "', expirationDate = '" + getExpirationTime() + "' WHERE ip = '" + client_ip + "';");
		ResultSet rs = db.get("select attempts from askingIps where ip='" + client_ip + "';");
		try {
			rs.next();
			return Integer.parseInt(rs.getString(1));
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public boolean canConnect(String client_ip, DataBaseConnection db) throws GeneralException {
		ResultSet rs = db.get("SELECT attempts, expirationDate FROM askingIps WHERE ip='" + client_ip + "';");
		int attempts = 0;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date expirationDate = new Date();
		try {
			if (rs.next()) {
				attempts = Integer.parseInt(rs.getString(1));
				expirationDate = dateFormat.parse(rs.getString(2));
			}
		} catch (Exception e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return attempts < max_attempts || expirationDate.compareTo(new Date()) <= 0;
	}
}