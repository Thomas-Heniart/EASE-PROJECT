package com.Ease.API.V1.Catalog;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Context.Catalog.WebsiteInformation;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/catalog/GetWebsites")
public class GetWebsites extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            JSONObject res = new JSONObject();
            JSONArray websites = new JSONArray();
            for (Website website : catalog.getWebsites()) {
                JSONObject tmp = new JSONObject();
                tmp.put("id", website.getDb_id());
                tmp.put("name", website.getName());
                tmp.put("logo", website.getLogo());
                JSONObject information = new JSONObject();
                for (WebsiteInformation websiteInformation : website.getInformations())
                    information.put(websiteInformation.getInformationName(), websiteInformation.getInformationJson());
                tmp.put("information", information);
                tmp.put("category_id", website.getDb_id() % 2 + 1);
                websites.add(tmp);
            }
            res.put("websites", websites);
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
