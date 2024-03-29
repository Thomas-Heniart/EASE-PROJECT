package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.AppFactory;
import com.Ease.NewDashboard.Profile;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/catalog/AddClassicApp")
public class ServletAddClassicApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            String name = sm.getStringParam("name", true, false);
            if (name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Name too long");
            Integer website_id = sm.getIntParam("website_id", true, false);
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Website website = catalog.getPublicWebsiteWithId(website_id, sm.getHibernateQuery(), user.getTeams());
            Profile profile = user.getProfile(profile_id);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            sm.decipher(account_information);
            Map<String, String> information = website.getInformationNeeded(account_information);
            String keyUser = sm.getKeyUser();
            App app = AppFactory.getInstance().createClassicApp(name, website, keyUser, information, 0, sm.getHibernateQuery());
            app.setProfile(profile);
            app.setPosition(profile.getSize());
            sm.saveOrUpdate(app);
            profile.addApp(app);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.DASHBOARD_APP, WebSocketMessageAction.CREATED, app.getWebSocketJson()));
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
