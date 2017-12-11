package com.Ease.API.V1.Dashboard;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Catalog.WebsiteFactory;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.*;
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

@WebServlet("/api/v1/dashboard/EditAnyApp")
public class ServletEditAnyApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer app_id = sm.getIntParam("app_id", true, false);
            User user = sm.getUser();
            String name = sm.getStringParam("name", true, false);
            String url = sm.getStringParam("url", false, false);
            if (url.equals("") || url.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            JSONObject account_information = sm.getJsonParam("account_information", true, false);
            //sm.decipher(account_information);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            App app = user.getApp(app_id, hibernateQuery);
            if (!app.isAnyApp() || app.getTeamCardReceiver() != null)
                throw new HttpServletException(HttpStatus.Forbidden);
            AnyApp anyApp = (AnyApp) app;
            String keyUser = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
            anyApp.decipher(keyUser, null);
            Account account = anyApp.getAccount();
            if (account == null || account.getAccountInformationSet().isEmpty()) {
                Map<String, String> accountInformation = anyApp.getWebsite().getInformationNeeded(account_information);
                account = AccountFactory.getInstance().createAccountFromMap(accountInformation, keyUser, 0);
                anyApp.setAccount(account);
            } else
                account.edit(account_information, hibernateQuery);
            app.getAppInformation().setName(name);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = anyApp.getWebsite();
            if (!website.getLogin_url().equals(url)) {
                website = catalog.getWebsiteWithUrl(url, hibernateQuery);
                if (website != null) {
                    if (website.getWebsiteAttributes().isIntegrated()) {
                        App tmp_app = AppFactory.getInstance().createClassicApp(name, website, keyUser, account);
                        tmp_app.setProfile(anyApp.getProfile());
                        tmp_app.setPosition(anyApp.getPosition());
                        sm.deleteObject(anyApp);
                        app = tmp_app;
                    } else
                        anyApp.setWebsite(website);
                } else {
                    String img_url = sm.getStringParam("img_url", false, true);
                    website = WebsiteFactory.getInstance().createWebsiteAndLogo(url, name, img_url, sm.getHibernateQuery());
                    anyApp.setWebsite(website);
                }
            }
            sm.saveOrUpdate(app);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.DASHBOARD_APP, WebSocketMessageAction.CHANGED, app.getWebSocketJson()));
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
