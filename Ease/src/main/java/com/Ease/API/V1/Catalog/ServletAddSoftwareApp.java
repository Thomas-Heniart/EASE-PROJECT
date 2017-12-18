package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Software;
import com.Ease.Catalog.SoftwareFactory;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.AppFactory;
import com.Ease.NewDashboard.Profile;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/catalog/AddSoftwareApp")
public class ServletAddSoftwareApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Profile profile = sm.getUser().getProfile(profile_id);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            String folder = name.replaceAll("\\W", "_");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            JSONObject connection_information = sm.getJsonParam("connection_information", false, false);
            Software software = catalog.getSoftwareWithFolderOrName(name, folder, connection_information, hibernateQuery);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            if (software == null) {
                String logo_url = sm.getStringParam("logo_url", false, true);
                if (logo_url != null && logo_url.length() > 2000)
                    throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter logo_url");
                software = SoftwareFactory.getInstance().createSoftwareAndLogo(name, folder, logo_url, connection_information, hibernateQuery);
            }
            App app = AppFactory.getInstance().createSoftwareApp(name, software, sm.getKeyUser(), account_information, sm.getHibernateQuery());
            app.setProfile(profile);
            app.setPosition(profile.getSize());
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
