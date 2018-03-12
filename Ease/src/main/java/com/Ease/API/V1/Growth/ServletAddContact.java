package com.Ease.API.V1.Growth;

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
import java.util.Map;

@WebServlet("/addContactToList")
public class ServletAddContact extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Map<String, String> args = sm.getArgs();
            String listId = args.get("listId");
            if (listId == null || listId.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "No referer");
            System.out.println(listId);
            MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
            mailjetContactWrapper.addContactToList(this.getContactDataFromArgs(args), Long.valueOf(listId));
            sm.setSuccess("Finish");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private JSONObject getContactDataFromArgs(Map<String, String> args) throws HttpServletException {
        JSONObject contactData = new JSONObject();
        JSONObject contactProperties = new JSONObject();
        System.out.println(args.isEmpty());
        for (Map.Entry<String, String> entry : args.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(value);
            if (value == null || value.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "Missing value");
            if (key.contains("email"))
                contactData.put("Email", value);
            else if (key.contains("company"))
                contactProperties.put("company_name", value);
            else if (key.contains("phone_number"))
                contactProperties.put("phone_number", value);
            else if (key.contains("firstName") && key.contains("lastName"))
                contactProperties.put("full_name", value);
            contactData.put("Properties", contactProperties);
        }
        return contactData;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
