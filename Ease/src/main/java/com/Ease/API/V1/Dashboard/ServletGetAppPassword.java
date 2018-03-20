package com.Ease.API.V1.Dashboard;

import com.Ease.Metrics.EaseEventFactory;
import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.App;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/GetAppPassword")
public class ServletGetAppPassword extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer app_id = sm.getIntParam("app_id", true, false);
            App app = user.getApp(app_id, sm.getHibernateQuery());
            JSONObject res = new JSONObject();
            if (app.isLinkApp())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot ask password for this app");
            Account account = app.getAccount();
            String password;
            String team_key = null;
            String keyUser = sm.getKeyUser();
            if (account == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This app is empty");
            TeamCardReceiver teamCardReceiver = app.getTeamCardReceiver();
            if (teamCardReceiver != null) {
                TeamUser teamUser = sm.getTeamUser(teamCardReceiver.getTeamCard().getTeam());
                if (teamCardReceiver.isTeamSingleCardReceiver() && (!((TeamSingleCardReceiver) teamCardReceiver).isAllowed_to_see_password() && !teamUser.isTeamAdmin()))
                    throw new HttpServletException(HttpStatus.Forbidden, "You are not allowed to see the password");
                Team team = teamCardReceiver.getTeamCard().getTeam();
                sm.initializeTeamWithContext(team);
                sm.needToBeTeamUserOfTeam(team);
                team_key = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
            }
            app.decipher(keyUser, team_key);
            password = account.getInformationNamed("password").getDeciphered_information_value();
            res.put("password", sm.cipher(password));
            sm.saveOrUpdate(EaseEventFactory.getInstance().createPasswordUsedEvent(user.getDb_id(), "Copy", app));
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
