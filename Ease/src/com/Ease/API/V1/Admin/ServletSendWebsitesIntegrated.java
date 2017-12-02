package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Context.Variables;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/admin/SendWebsitesIntegrated")
public class ServletSendWebsitesIntegrated extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            JSONObject emailAndWebsiteIds = sm.getJsonParam("emailAndWebsiteIds", false, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            for (Object object : emailAndWebsiteIds.entrySet()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) object;
                String email = entry.getKey();
                JSONArray websiteIds = (JSONArray) entry.getValue();
                String website_names = "";
                for (Object object1 : websiteIds) {
                    Integer id = Math.toIntExact((Long) object1);
                    website_names += catalog.getWebsiteWithId(id, sm.getHibernateQuery()).getName() + ", ";
                }
                website_names = website_names.substring(0, website_names.length() - 2);
                MailJetBuilder mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setFrom("contact@ease.space", "Agathe @Ease");
                mailJetBuilder.addTo(email);
                mailJetBuilder.setTemplateId(265363);
                mailJetBuilder.addVariable("app_name", website_names);
                mailJetBuilder.addVariable("url", Variables.URL_PATH + "#/main/catalog");
                mailJetBuilder.sendEmail();
            }
            sm.setSuccess("Done");
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
