package com.Ease.Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Mail;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class PricingContactUs
 */
@WebServlet("/PricingContactUs")
public class PricingContactUs extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PricingContactUs() {
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
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		try {
			String email = sm.getServletParam("email", true);
			String name = sm.getServletParam("name", true);
			String phoneNumber = sm.getServletParam("phone", true);
			String jobPosition = sm.getServletParam("jobPosition", true);
			String company = sm.getServletParam("company", true);
			String collaboratorNumber = sm.getServletParam("teamSize", true);
			String needs = sm.getServletParam("needs", true);
			if (email == null || email.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty email");
			if (!Regex.isEmail(email))
				throw new GeneralException(ServletManager.Code.ClientWarning, "This is not an email");
			if (name == null || name.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name");
			if (phoneNumber == null || phoneNumber.equals(""))
				phoneNumber = "";
			if (phoneNumber != "" && !Regex.isPhoneNumber(phoneNumber))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Incorrect phone number");
			if (jobPosition == null || jobPosition.equals(""))
				jobPosition = "";
			if (company == null || company.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty company name");
			if (collaboratorNumber == null || collaboratorNumber.equals(""))
				collaboratorNumber = "0";
			if (needs == null || needs.equals(""))
				needs = "";
			try {
				int collaborators = Integer.parseInt(collaboratorNumber);
				Mail mail = new Mail("contact@ease.space", "Fdb00.6Y6.space", "Agathe the money");
				mail.sendPricingContact(email, name, phoneNumber, jobPosition, company, collaborators, needs);
				DatabaseRequest db_request = sm.getDB().prepareRequest("INSERT INTO pricingContacts values(?, ?, ?, ?, ?, ?, ?, ?, default)");
				db_request.setNull();
				db_request.setString(email);
				db_request.setString(name);
				db_request.setString(phoneNumber);
				db_request.setString(company);
				db_request.setString(jobPosition);
				db_request.setInt(collaborators);
				db_request.setString(needs);
				db_request.set();
				sm.setResponse(ServletManager.Code.Success, "PricingContactUs done");
			} catch(NumberFormatException e) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Collaborators must be a number");
			}
			
		} catch(GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
