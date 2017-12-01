package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.User.User;
import com.Ease.NewDashboard.*;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@WebServlet("/api/v1/catalog/AddClassicAppSameAs")
public class ServletAddClassicAppSameAs extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer website_id = sm.getIntParam("website_id", true, false);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() >= 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            Integer app_id = sm.getIntParam("same_app_id", true, false);
            App same_app = user.getApp(app_id, sm.getHibernateQuery());
            if (!same_app.isClassicApp())
                throw new HttpServletException(HttpStatus.BadRequest, "Please provide a classic app");
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Map.Entry<String, String> public_and_private_key = RSA.generateKeys();
            Set<AccountInformation> accountInformationSet = new HashSet<>();
            String keyUser = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
            same_app.decipher(keyUser);
            for (AccountInformation accountInformation : ((ClassicApp) same_app).getAccount().getAccountInformationSet())
                accountInformationSet.add(new AccountInformation(accountInformation.getInformation_name(), RSA.Encrypt(accountInformation.getDeciphered_information_value(), public_and_private_key.getKey()), accountInformation.getDeciphered_information_value()));
            Account account = new Account(((ClassicApp) same_app).getAccount().getReminder_interval(), public_and_private_key.getKey(), AES.encrypt(public_and_private_key.getValue(), keyUser), accountInformationSet, public_and_private_key.getValue());
            accountInformationSet.stream().forEach(accountInformation -> accountInformation.setAccount(account));
            Website website = catalog.getWebsiteWithId(website_id, sm.getHibernateQuery());
            Profile profile = user.getProfile(profile_id);
            AppInformation appInformation = new AppInformation(name);
            ClassicApp classicApp = new ClassicApp(appInformation, website, account);
            classicApp.setProfile(profile);
            classicApp.setPosition(profile.getSize());
            sm.saveOrUpdate(classicApp);
            profile.addApp(classicApp);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.DASHBOARD_APP, WebSocketMessageAction.CREATED, classicApp.getJson()));
            sm.setSuccess(classicApp.getJson());
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
