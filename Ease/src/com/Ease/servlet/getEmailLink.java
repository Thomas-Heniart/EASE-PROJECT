package com.Ease.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Mail;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

@WebServlet("/getEmailLink")
public class getEmailLink extends HttpServlet {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public getEmailLink() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.GetMailLink, request, response, user);
		
		// Get Parameters
		String email = SI.getServletParam("email");
		// --
		email.replaceAll(" ", "");
		email.replaceAll("\r", "");
		email.replaceAll("\n", "");
		email.replaceAll("\t", "");
		
		String invitationCode = null;
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}
		
		try {
			if (email == null || Regex.isEmail(email) == false){
				SI.setResponse(ServletItem.Code.BadParameters, "This is not an email.");
			} else {		
				ResultSet rs;
				rs = db.get("select * from users where email ='" + email + "';");
				if (rs.next()) {
					SI.setResponse(ServletItem.Code.BadParameters, "An account already exist with this email, to claim it, please email : benjamin@ease-app.co");
				} else {
					if ((rs = db.get("select * from invitations where email ='" + email + "';")) == null || !(rs.next())){
						SI.setResponse(ServletItem.Code.BadParameters, "Sorry you are not on the list. Try with your IESEG mail or contact us at victor@ease-app.co");
					} else {
						invitationCode = rs.getString(2);
						Mail newEmail = new Mail();
						newEmail.getEmailLinkMail(email, invitationCode);
						SI.setResponse(200, "Please, go check your email at "+email+" ;)");
					}
				}
			}
		} catch (SQLException e){
			e.printStackTrace();
			SI.setResponse(ServletItem.Code.LogicError, ServletItem.getExceptionTrace(e));
		} catch (MessagingException e) {
			e.printStackTrace();
			SI.setResponse(ServletItem.Code.EMailNotSended, ServletItem.getExceptionTrace(e));
		}
		SI.sendResponse();
	}
}