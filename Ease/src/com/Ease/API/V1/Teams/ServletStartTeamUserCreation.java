package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.SendGridMail;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Team.TeamUserPermissions;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 24/05/2017.
 */
@WebServlet("/api/v1/teams/StartTeamUserCreation")
public class ServletStartTeamUserCreation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        HibernateQuery query = new HibernateQuery();
        try {
            sm.needToBeConnected();
            String team_id = sm.getServletParam("team_id", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null");
            sm.needToBeAdminOfTeam(Integer.parseInt(team_id));
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser adminTeamUser = sm.getTeamUserForTeam(team);
            String email = sm.getServletParam("email", true);
            String username = sm.getServletParam("username", true);
            String role = sm.getServletParam("role", true);
            if (email == null || email.equals("") || !Regex.isEmail(email) || username == null || username.equals("") || role == null || role.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Invalid inputs");
            String first_name = sm.getServletParam("first_name", true);
            String last_name = sm.getServletParam("last_name", true);
            query.querySQLString("SELECT id FROM teamUsers WHERE (email = ? OR username = ?) AND team_id = ? AND verified = 1;");
            query.setParameter(1, email);
            query.setParameter(2, username);
            query.setParameter(3, team_id);
            JSONObject res = new JSONObject();
            res.put("email", email);
            if (!query.list().isEmpty()) {
                res.put("success", false);
                res.put("cause", "This person is already on your team.");
            }
            if (res.get("success") == null) {
                query.querySQLString("SELECT id FROM teamUsers WHERE (email = ? OR username = ?) AND team_id = ? AND verified = 0;");
                query.setParameter(1, email);
                query.setParameter(2, username);
                query.setParameter(3, team_id);
                if (!query.list().isEmpty()) {
                    res.put("success", false);
                    res.put("cause", "This person has already been invited to your team.");
                }
            }
            if (res.get("success") == null) {
                query.querySQLString("SELECT id FROM users WHERE email = ?;");
                query.setParameter(1, email);
                if (!query.list().isEmpty()) {
                    query.querySQLString("SELECT id FROM teamUsers WHERE email = ?");
                    query.setParameter(1, email);
                    if (!query.list().isEmpty()) {
                        res.put("success", false);
                        res.put("cause", "This email has already been taken");
                    }
                }
            }
            if (res.get("success") == null) {
                TeamUser teamUser = new TeamUser(first_name, last_name, email, username, null, false, team, new TeamUserPermissions(Integer.parseInt(role)));
                team.getGeneralChannel().addTeamUser(teamUser);
                query.saveOrUpdateObject(teamUser);
                String code;
                do {
                    code = CodeGenerator.generateNewCode();
                    query.querySQLString("SELECT * FROM pendingTeamInvitations WHERE code = ?");
                    query.setParameter(1, code);
                } while (!query.list().isEmpty());
                query.querySQLString("INSERT INTO pendingTeamInvitations values(NULL, ?, ?, ?);");
                query.setParameter(1, teamUser.getDb_id());
                query.setParameter(2, code);
                query.setParameter(3, team.getDb_id());
                query.executeUpdate();
                SendGridMail sendGridMail = new SendGridMail("Benjamin @EaseSpace", "benjamin@ease.space");
                sendGridMail.sendInvitationToJoinTeamEmail(team.getName(), adminTeamUser.getFirstName(), email, code);
                res.put("success", true);
                res.put("teamUser_id", teamUser.getDb_id());
                res.put("username", username);
            }
            sm.setResponse(ServletManager.Code.Success, res.toString());
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
