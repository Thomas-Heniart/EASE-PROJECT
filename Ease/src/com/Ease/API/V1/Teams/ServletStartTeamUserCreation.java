package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.SendGridMail;
import com.Ease.Team.*;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
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
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeAdminOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser adminTeamUser = sm.getTeamUserForTeam(team);
            String email = sm.getStringParam("email", true);
            String username = sm.getStringParam("username", true);
            Integer role = sm.getIntParam("role", true);
            if (email == null || email.equals("") || !Regex.isEmail(email) || username == null || username.equals("") || role == null)
                throw new GeneralException(ServletManager.Code.ClientWarning, "Invalid inputs");
            String first_name = sm.getStringParam("first_name", true);
            String last_name = sm.getStringParam("last_name", true);
            HibernateQuery query = sm.getHibernateQuery();
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
                TeamUser teamUser = new TeamUser(first_name, last_name, email, username, null, false, team, new TeamUserRole(role));
                team.getGeneralChannel().addTeamUser(teamUser);
                query.saveOrUpdateObject(team);
                team.addTeamUser(teamUser);
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
                res.put("team_user_id", teamUser.getDb_id());
                res.put("username", username);
            }
            sm.setSuccess(res);
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
