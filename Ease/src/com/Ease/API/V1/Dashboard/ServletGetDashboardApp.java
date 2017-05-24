package com.Ease.API.V1.Dashboard;

import com.Ease.Dashboard.App.App;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 24/05/2017.
 */
@WebServlet("/api/v1/dashboard/GetDashboardApp")
public class ServletGetDashboardApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String app_id = sm.getServletParam("id", true);
            if (app_id == null || app_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "App is null");
            App app = sm.getUser().getDashboardManager().getAppWithID(Integer.parseInt(app_id));
            sm.setResponse(ServletManager.Code.Success, app.getJsonWithoutId().toString());
            sm.setLogResponse("GetDashboardApp done");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
