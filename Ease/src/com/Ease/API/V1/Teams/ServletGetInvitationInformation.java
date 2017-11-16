package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/GetInvitationInformation")
public class ServletGetInvitationInformation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            String code = sm.getParam("code", true);
            if (code == null || code.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Missing code parameter");
            DataBaseConnection db = sm.getDB();
            DatabaseRequest databaseRequest = db.prepareRequest("SELECT team_id, id FROM teamUsers WHERE invitation_code = ?;");
            databaseRequest.setString(code);
            DatabaseResult rs = databaseRequest.get();
            if (!rs.next())
                throw new HttpServletException(HttpStatus.BadRequest, "Please provide a valid code.");
            Integer team_id = rs.getInt(1);
            Integer teamUser_id = rs.getInt(2);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeam(team_id, sm.getHibernateQuery());
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            databaseRequest = db.prepareRequest("SELECT id from users where email = ?;");
            databaseRequest.setString(teamUser.getEmail());
            rs = databaseRequest.get();
            JSONObject res = new JSONObject();
            res.put("account_exists", rs.next());
            res.put("email", teamUser.getEmail());
            res.put("team_name", team.getName());
            res.put("teamUser", teamUser.getJson());
            res.put("team_id", team_id);
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
