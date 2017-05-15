package com.Ease.API.V1.Catalog;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 15/05/2017.
 */
@WebServlet("/api/v1/catalog/searchWebsite")
public class ServletSearchWebsite extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            String search = sm.getServletParam("search", true);
            JSONArray jsonArray = new JSONArray();
            if (search != null && !search.equals("")) {
                for (Website website : catalog.getWebsites()) {
                    if (website.getName().toLowerCase().startsWith(search.toLowerCase()))
                        jsonArray.add(website.getJSON());
                }
            }
            sm.setResponse(ServletManager.Code.Success, jsonArray.toString());
            sm.setLogResponse("Search done");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
