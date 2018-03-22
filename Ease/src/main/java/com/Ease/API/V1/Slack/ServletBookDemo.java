package com.Ease.API.V1.Slack;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.Utils.Slack.SlackAPIWrapper;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/slack/BookDemo")
public class ServletBookDemo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String firstName = sm.getStringParam("et_pb_contact_prénom_1", true, true);
            String lastName = sm.getStringParam("et_pb_contact_nom_1", true, false);
            //String name = sm.getStringParam("name", true, true);
            /* if (name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty name"); */
            String email = sm.getStringParam("et_pb_contact_votre_email_1", true, false);
            if (!Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid email");
            String phoneNumber = sm.getStringParam("et_pb_contact_phone_1", true, false);
            if (!Regex.isPhoneNumber(phoneNumber))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid phone number");
            String enterprise = sm.getStringParam("et_pb_contact_company_1", true, false);
            if (enterprise.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty enterprise");
            String s = "Name: " +
                    firstName + " " + lastName +
                    "\nEmail: " +
                    email +
                    "\nPhone: " +
                    phoneNumber +
                    "\nCompany: " +
                    enterprise +
                    "\n=======\n=======\n=======";
            SlackAPIWrapper.getInstance().postMessage("C9U41M52Q", s);
            sm.setSuccess("Send");
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
