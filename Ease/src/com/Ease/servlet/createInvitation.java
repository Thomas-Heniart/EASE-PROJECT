package com.Ease.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.context.DataBase;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

@WebServlet("/createInvitation")
public class createInvitation extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public createInvitation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.CreateInvitation, request, response, user);
		
		// Get Parameters
		String			email = SI.getServletParam("email");
		String			group = SI.getServletParam("groupId");
		if(email!=null){
			email.replaceAll(" ", "");
			email.replaceAll("\r", "");
			email.replaceAll("\n", "");
			email.replaceAll("\t", "");
		}
		if(group!=null){
			group.replaceAll(" ", "");
			group.replaceAll("\r", "");
			group.replaceAll("\n", "");
			group.replaceAll("\t", "");
		}
		// --
		
		String			alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
		String			invitationCode = "";
		Properties props = new Properties();
		Connection 		con;
		ResultSet		rs;
		Random r = new Random();
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}
		if(user == null || !user.isAdmin(session.getServletContext())){
			SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
		} else {
			try {
				if (email == null || Regex.isEmail(email) == false){
					SI.setResponse(ServletItem.Code.BadParameters, "Wrong email.");
				} else {					
					rs = db.get("select * from users where email='" + email + "';");
					if (rs.next()) {
						if (group != null){
							int groupId = Integer.parseInt(group);
							ResultSet rs2 = db.get("select * from groups where id=" + groupId + ";");
							if (rs2.next()){
								db.set("insert into GroupAndUserMap values (NULL, " + groupId + ", " + rs.getString(1) + ");");
								SI.setResponse(200, "User already exist, setting a group.");
							} else {
								SI.setResponse(ServletItem.Code.BadParameters, "This group dosen't exist.");
							}
						} else {
							SI.setResponse(199, "This user already exist.");
						}
					} else {
						rs = db.get("select * from invitations where email='" + email + "';");
						if (rs.next()) {
							if (group != null){
								int groupId = Integer.parseInt(group);
								ResultSet rs2 = db.get("select * from groups where id=" + groupId + ";");
								if (rs2.next()){
									db.set("update invitations set group_id=" + groupId + " where email='" + email + "';");
									SI.setResponse(200, "Change invitation with group.");
								} else {
									SI.setResponse(ServletItem.Code.BadParameters, "This group dosen't exist.");
								}
							} else {
								SI.setResponse(199, "This user already exist.");
							}
						} else {
							for (int i = 0;i < 126 ; ++i) {
								invitationCode += alphabet.charAt(r.nextInt(alphabet.length()));			
							}
							if (group == null){
								db.set("insert into invitations values ('" + email + "', '" + invitationCode + "', NULL);");
								SI.setResponse(200, "Invitation send.");
							} else {
								int groupId = Integer.parseInt(group);
								rs = db.get("select * from groups where id=" + groupId + ";");
								if (rs.next()){
									db.set("insert into invitations values ('" + email + "', '" + invitationCode + "', " + groupId + ");");
									SI.setResponse(200, "Invitation send with group.");
								} else {
									SI.setResponse(ServletItem.Code.BadParameters, "This group dosen't exist.");
								}
							}
						}
					}
				}
			} catch (SQLException e) {
				SI.setResponse(ServletItem.Code.LogicError, "SQL Exception");
			} catch (NumberFormatException e) {
				SI.setResponse(ServletItem.Code.BadParameters, "Wrong numbers.");
			}
		}
		SI.sendResponse();
	}
}