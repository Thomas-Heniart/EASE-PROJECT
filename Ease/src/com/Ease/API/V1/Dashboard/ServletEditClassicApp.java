package com.Ease.API.V1.Dashboard;

import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/EditClassicApp")
public class ServletEditClassicApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer app_id = sm.getIntParam("app_id", true, false);
            User user = sm.getUser();
            App app = user.getApp(app_id, sm.getHibernateQuery());
            if (!app.isClassicApp())
                throw new HttpServletException(HttpStatus.Forbidden);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            /* String private_key = (String) sm.getContextAttr("privateKey");
            for (Object object : account_information.entrySet()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) object;
                account_information.put(entry.getKey(), RSA.Decrypt(entry.getValue(), private_key));
            } */
            ClassicApp classicApp = (ClassicApp) app;
            classicApp.getAccount().edit(account_information);
            app.getAppInformation().setName(name);
            sm.saveOrUpdate(app);
            sm.setSuccess(app.getJson());
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
