package com.Ease.API.V1.Dashboard;

import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.AccountFactory;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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
            sm.decipher(account_information);
            ClassicApp classicApp = (ClassicApp) app;
            String keyUser = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
            classicApp.decipher(keyUser);
            Account account = classicApp.getAccount();
            if (account == null || account.getAccountInformationSet().isEmpty()) {
                Map<String, String> accountInformation = classicApp.getWebsite().getInformationNeeded(account_information);
                account = AccountFactory.getInstance().createAccountFromMap(accountInformation, keyUser, 0);
                classicApp.setAccount(account);
            } else
                account.edit(account_information, sm.getHibernateQuery());
            app.getAppInformation().setName(name);
            sm.saveOrUpdate(app);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.APP, WebSocketMessageAction.CHANGED, app.getJson()));
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
