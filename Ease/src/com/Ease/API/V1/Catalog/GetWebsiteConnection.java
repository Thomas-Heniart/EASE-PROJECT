package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/catalog/GetWebsiteConnection")
public class GetWebsiteConnection extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer website_id = sm.getIntParam("website_id", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getPublicWebsiteWithId(website_id, sm.getHibernateQuery(), sm.getUser().getTeams());
            JSONObject connection_json = new JSONObject();
            connection_json.put("website", website.getConnectionJson());
            connection_json.put("website_name", website.getName());
            connection_json.put("app_name", website.getName());
            connection_json.put("type", "ClassicApp");
            JSONArray res = new JSONArray();
            res.add(connection_json);
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
