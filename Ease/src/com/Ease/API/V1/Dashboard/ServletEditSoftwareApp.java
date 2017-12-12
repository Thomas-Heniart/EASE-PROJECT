package com.Ease.API.V1.Dashboard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.AccountFactory;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.SoftwareApp;
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
import java.util.Map;

@WebServlet("/api/v1/dashboard/EditSoftwareApp")
public class ServletEditSoftwareApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer app_id = sm.getIntParam("app_id", true, false);
            User user = sm.getUser();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            App app = user.getApp(app_id, hibernateQuery);
            if (!app.isSoftwareApp())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot edit this app");
            app.decipher(sm.getKeyUser(), null);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            SoftwareApp softwareApp = (SoftwareApp) app;
            Account account = softwareApp.getAccount();
            if (account == null || account.getAccountInformationSet().isEmpty()) {
                Map<String, String> accountInformation = softwareApp.getSoftware().getInformationNeeded(account_information);
                account = AccountFactory.getInstance().createAccountFromMap(accountInformation, sm.getKeyUser(), 0);
                softwareApp.setAccount(account);
            } else
                account.edit(account_information, hibernateQuery);
            softwareApp.getAppInformation().setName(name);
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
