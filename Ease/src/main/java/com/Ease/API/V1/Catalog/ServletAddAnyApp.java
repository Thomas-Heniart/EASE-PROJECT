package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.*;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.AppFactory;
import com.Ease.NewDashboard.Profile;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/catalog/AddAnyApp")
public class ServletAddAnyApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String url = sm.getStringParam("url", false, false);
            if (!Regex.isSimpleUrl(url))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid url");
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Profile profile = sm.getUser().getProfile(profile_id);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithUrl(url, sm.getHibernateQuery());
            App app;
            String symmetric_key = sm.getKeyUser();
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            //sm.decipher(account_information);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            if (website != null && website.getWebsiteAttributes().isIntegrated())
                app = AppFactory.getInstance().createClassicApp(name, website, symmetric_key, account_information, 0);
            else {
                if (website == null) {
                    String img_url = sm.getStringParam("img_url", false, true);
                    website = WebsiteFactory.getInstance().createWebsiteAndLogo(sm.getUser().getEmail(), url, name, img_url, sm.getHibernateQuery());
                }
                app = AppFactory.getInstance().createAnyApp(name, website, symmetric_key, account_information);
            }
            Boolean credentials_provided = sm.getBooleanParam("credentials_provided", true, false);
            if (credentials_provided) {
                HibernateQuery hibernateQuery = sm.getHibernateQuery();
                hibernateQuery.queryString("SELECT key FROM ServerPublicKey key");
                ServerPublicKey serverPublicKey = (ServerPublicKey) hibernateQuery.getSingleResult();
                WebsiteCredentials websiteCredentials = new WebsiteCredentials(RSA.Encrypt((String) account_information.get("login"), serverPublicKey.getPublicKey()), RSA.Encrypt((String) account_information.get("password"), serverPublicKey.getPublicKey()), website, serverPublicKey);
                sm.saveOrUpdate(websiteCredentials);
                website.addWebsiteCredentials(websiteCredentials);
            }
            app.setProfile(profile);
            app.setPosition(profile.getSize());
            sm.saveOrUpdate(app);
            profile.addApp(app);
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
