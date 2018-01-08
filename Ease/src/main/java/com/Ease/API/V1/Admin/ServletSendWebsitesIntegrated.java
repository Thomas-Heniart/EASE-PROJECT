package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Context.Variables;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/SendWebsitesIntegrated")
public class ServletSendWebsitesIntegrated extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            JSONObject emailAndWebsiteIds = sm.getJsonParam("emailAndWebsiteIds", false, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            for (Object object : emailAndWebsiteIds.keySet()) {
                String email = (String) object;
                JSONArray websiteIds = emailAndWebsiteIds.getJSONArray(email);
                StringBuilder website_names = new StringBuilder();
                for (int i = 0; i < websiteIds.length(); i++) {
                    Integer id = websiteIds.getInt(i);
                    website_names.append(catalog.getWebsiteWithId(id, sm.getHibernateQuery()).getName()).append(", ");
                }
                website_names = new StringBuilder(website_names.substring(0, website_names.length() - 2));
                MailJetBuilder mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setFrom("contact@ease.space", "Agathe @Ease");
                mailJetBuilder.addTo(email);
                mailJetBuilder.setTemplateId(265363);
                mailJetBuilder.addVariable("first_name", website_names.toString());
                mailJetBuilder.addVariable("last_name", Variables.URL_PATH + "#/main/catalog");
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
