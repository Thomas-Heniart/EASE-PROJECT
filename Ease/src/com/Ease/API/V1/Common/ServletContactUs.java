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

@WebServlet("/api/v1/common/ContactUs")
public class ServletContactUs extends HttpServlet {

    private static final String[] demand_types = {
            "À propos du produit",
            "À propos de la sécurité",
            "Jobs",
            "Presse",
            "Autre demande"
    };

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String name = sm.getStringParam("name", true);
            String role = sm.getStringParam("role", true);
            String phoneNumber = sm.getStringParam("phoneNumber", true);
            String enterprise = sm.getStringParam("enterprise", true);
            String demandType = sm.getStringParam("demand_type", true);
            String message = sm.getStringParam("message", false);
            String email = sm.getStringParam("email", true);
            if (email == null || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "Please provide us a valid email.");
            if (demandType == null || demandType.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Please provide us a demand type.");
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
            try {
                demandType = demand_types[Integer.parseInt(demandType)];
            } catch (Exception e) {
                throw new HttpServletException(HttpStatus.BadRequest, "You must provide a valid demand type.");
            }
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom("contact@ease.space", "Agathe @Ease");
            mailJetBuilder.addTo("benjamin@ease.space");
            mailJetBuilder.addCc("victor@ease.space");
            mailJetBuilder.addCc("sergii@ease.space");
            mailJetBuilder.addCc("thomas@ease.space");
            mailJetBuilder.setTemplateId(209265);
            mailJetBuilder.addVariable("message", message);
            mailJetBuilder.addVariable("role", role);
            mailJetBuilder.addVariable("enterprise", enterprise);
            mailJetBuilder.addVariable("phoneNumber", phoneNumber);
            mailJetBuilder.addVariable("demandType", demandType);
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
