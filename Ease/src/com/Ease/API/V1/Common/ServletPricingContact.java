package com.Ease.API.V1.Common;

import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/PricingContact")
public class ServletPricingContact extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String name = sm.getStringParam("name", true, true);
            String role = sm.getStringParam("role", true, true);
            String phoneNumber = sm.getStringParam("phoneNumber", true, true);
            String enterprise = sm.getStringParam("enterprise", true, true);
            String message = sm.getStringParam("message", false, true);
            String email = sm.getStringParam("email", true, true);
            String collaboratorsString = sm.getStringParam("collaborators", true, true);
            Integer collaborators;
            if (email == null || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "Please provide us a valid email.");
            if (name == null || name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Please provide us your name.");
            if (message == null || message.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty message.");
            if (role == null)
                role = "";
            if (phoneNumber == null)
                phoneNumber = "";
            if (enterprise == null)
                enterprise = "";
            if (collaboratorsString == null || collaboratorsString.equals(""))
                collaborators = 0;
            else {
                try {
                    collaborators = Integer.parseInt(collaboratorsString);
                } catch (NumberFormatException e) {
                    throw new HttpServletException(HttpStatus.BadRequest, "Collaborators must be a number.");
                }
            }
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom("contact@ease.space", "Agathe @Ease");
            mailJetBuilder.addTo("benjamin@ease.space");
            mailJetBuilder.addCc("victor@ease.space");
            mailJetBuilder.addCc("sergii@ease.space");
            mailJetBuilder.addCc("thomas@ease.space");
            mailJetBuilder.setTemplateId(209268);
            mailJetBuilder.addVariable("message", message);
            mailJetBuilder.addVariable("role", role);
            mailJetBuilder.addVariable("enterprise", enterprise);
            mailJetBuilder.addVariable("collaborators", collaborators);
            mailJetBuilder.addVariable("phoneNumber", phoneNumber);
            mailJetBuilder.addVariable("name", name);
            mailJetBuilder.addVariable("email", email);
            mailJetBuilder.sendEmail();
            sm.setSuccess("Email sent");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
