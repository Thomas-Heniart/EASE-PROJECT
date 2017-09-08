package com.Ease.API.V1.Common;

import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ServletContactUs")
public class ServletContactUs extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String message = sm.getStringParam("message", false);
            String email = sm.getStringParam("email", true);
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom("contact@ease.space", "Agathe @Ease");
            mailJetBuilder.addTo("benjamin@ease.space");
            mailJetBuilder.addCc(email);
            mailJetBuilder.setTemplateId(0);
            mailJetBuilder.addVariable("message", message);
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
