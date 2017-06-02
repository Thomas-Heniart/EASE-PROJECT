package com.Ease.API.V1.Dashboard;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by thomas on 24/05/2017.
 */
@WebServlet("/api/v1/dashboard/SearchDashboardApps")
public class ServletSearchDashboardApps extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String search = sm.getParam("q", true);
            List<ClassicApp> classicAppList = sm.getUser().getDashboardManager().getClassicApps();
            JSONArray jsonArray = new JSONArray();
            if (search != null && !search.equals("")) {
                for (ClassicApp classicApp : classicAppList) {
                    Website website = classicApp.getSite();
                    if (website.getName().toLowerCase().startsWith(search.toLowerCase()) && website.work())
                        jsonArray.add(classicApp.getSearchJson());
                }
            }
            sm.setSuccess(jsonArray);
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
