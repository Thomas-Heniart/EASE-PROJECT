package com.Ease.API.Rest;

import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import io.swagger.annotations.*;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SwaggerDefinition(
        info = @Info(
                description = "ease API doc",
                version = "1.0.0",
                title = "Swagger for ease API doc",
                termsOfService = "https://ease.space/legalNotice",
                contact = @Contact(name = "Ease.space", email = "contact@ease.space", url = "https://ease.space"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ),
        consumes = {"application/json", "application/xml"},
        produces = {"application/json", "application/xml"},
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        tags = {@Tag(name = "App information", description = "Operations about app")}
)

@WebServlet("/GetAppInformation")
public class GetAppInformation extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUserWithToken();
            Integer app_id = sm.getIntParam("app_id", true);
            Integer team_id = sm.getIntParam("team_id", true);
            String information_name = sm.getParam("information_name", true);
            App app = user.getApp(app_id, sm.getHibernateQuery());
            if (!app.isClassicApp())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot get password of this app");
            ClassicApp classicApp = (ClassicApp) app;
            String symmetric_key = null;
            String team_key = null;
            if (app.getTeamCardReceiver() != null)
                team_key = (String) sm.getTeamProperties(app.getTeamCardReceiver().getTeamCard().getTeam().getDb_id()).get("teamKey");
            else
                symmetric_key = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
            classicApp.decipher(symmetric_key, team_key);
            JSONObject res = new JSONObject();
            res.put(information_name, classicApp.getAccount().getInformationNamed(information_name).getDeciphered_information_value());
            sm.setSuccess(res);
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
