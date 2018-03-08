package com.Ease.API.V1.Growth;

import com.Ease.Context.Variables;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Mail.MailjetContactWrapper;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/addContactToList")
public class ServletAddContact extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            JSONObject params = sm.getParams();
            Long listId = params.optLong("listId");
            if (listId.equals(0L))
                throw new HttpServletException(HttpStatus.BadRequest, "No referer");
            MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
            mailjetContactWrapper.addContactToList(this.getContactDataFromArgs(params), listId);
            if (listId.equals(36759L)) {
                new MailjetContactWrapper().addEmailToList(params.getString("Email"), 36180L);
                MailJetBuilder mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setFrom("benjamin@ease.space", "Benjamin Prigent");
                mailJetBuilder.addTo(params.getString("Email"));
                mailJetBuilder.setTemplateId(324084);
                mailJetBuilder.addVariable("url", Variables.URL_PATH + "resources/rgpd.pdf");
                mailJetBuilder.sendEmail();
                sm.setSuccess("Done");
            }
            sm.setSuccess("Finish");
        } catch (Exception e) {
            e.printStackTrace();
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private JSONObject getContactDataFromArgs(JSONObject params) throws HttpServletException {
        JSONObject contactData = new JSONObject();
        JSONObject contactProperties = new JSONObject();
        for (Object object : params.keySet()) {
            String key = (String) object;
            Object val = params.get(key);
            if (!(val instanceof String))
                continue;
            String value = params.getString(key);
            if (value.isEmpty() && key.contains("email"))
                throw new HttpServletException(HttpStatus.BadRequest, "Missing email");
            if (value.isEmpty())
                continue;
            if (key.contains("email"))
                contactData.put("Email", value);
            else if (key.contains("company"))
                contactProperties.put("company_name", value);
            else if (key.contains("phone_number"))
                contactProperties.put("_phone_", value);
            else if (key.contains("firstname") && key.contains("lastname"))
                contactProperties.put("full_name", value);
        }
        contactData.put("Properties", contactProperties);
        if (!contactData.has("Email"))
            throw new HttpServletException(HttpStatus.BadRequest, "Missing email");
        return contactData;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
