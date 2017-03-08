package com.Ease.Servlet;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Mail;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class ContactUs
 */
@WebServlet("/ContactUs")
public class ContactUs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContactUs() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);

		String email = sm.getServletParam("email", true);
		String msg = sm.getServletParam("message", true);
		
		try {
			if (email == null || !Regex.isEmail(email)) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "This is not an email.");
			}
			if (msg == null || msg.equals("") == true) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Message empty.");
			}
			
			try {
				Mail mail = new Mail();
				boolean isUser = false;
				try {
					User.findDBid(email, sm);
					isUser = true;
				} catch (GeneralException e) {
					if (e.getCode() != ServletManager.Code.ClientError) {
						throw new GeneralException(ServletManager.Code.InternError, e);
					} 
				}
				mail.sendValidationContactEmail(email, msg);
				mail.sendContactEmail("contact@ease.space", user, isUser, email, msg);
			} catch (MessagingException e) {
				throw new GeneralException(ServletManager.Code.InternError, "Email not sended.");
			}
			sm.setResponse(ServletManager.Code.Success, "Email sent.");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
