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
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 02/05/2017.
 */
@WebServlet("/api/v1/teams/StartTeamUserCreations")
public class ServletStartTeamUserCreations extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String teamUsersString = sm.getServletParam("teamUsersToCreate", true);
            String team_id = sm.getServletParam("team_id", true);
            JSONObject res = new JSONObject();
            JSONArray validInvitations = new JSONArray();
            JSONArray failInvitations = new JSONArray();
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null.");
            if (teamUsersString == null || teamUsersString.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "teamUsers is null.");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            sm.needToBeAdminOfTeam(team);
            TeamUser adminTeamUser = sm.getTeamUserForTeam(team);
            JSONParser parser = new JSONParser();
            JSONArray teamUsersToCreate = (JSONArray) parser.parse(StringEscapeUtils.unescapeHtml4(teamUsersString));
            if (teamUsersToCreate.isEmpty())
                throw new GeneralException(ServletManager.Code.ClientError, "Empty teamusers.");
            HibernateQuery query = new HibernateQuery();
            for (Object teamUserToCreateObj : teamUsersToCreate) {
                JSONObject teamUserToCreate = (JSONObject) teamUserToCreateObj;
                String email = (String) teamUserToCreate.get("email");
                String firstName = (String) teamUserToCreate.get("firstName");
                String lastName = (String) teamUserToCreate.get("lastName");
                String username = (String) teamUserToCreate.get("username");
                if (email == null || email.equals("") || !Regex.isEmail(email) || username == null || username.equals(""))
                    throw new GeneralException(ServletManager.Code.ClientError, "Invalid teamUsers");
                query.querySQLString("SELECT id FROM teamUsers WHERE email = ? AND team_id = ? AND verified = 1;");
                query.setParameter(1, email);
                query.setParameter(2, team_id);
                if (!query.list().isEmpty()) {
                    JSONObject tmp = new JSONObject();
                    tmp.put("email", email);
                    tmp.put("cause", "This person is already on your team.");
                    failInvitations.add(tmp);
                    continue;
                }
                query.querySQLString("SELECT id FROM teamUsers WHERE email = ? AND team_id = ? AND verified = 0;");
                query.setParameter(1, email);
                query.setParameter(2, team_id);
                if (!query.list().isEmpty()) {
                    JSONObject tmp = new JSONObject();
                    tmp.put("email", email);
                    tmp.put("cause", "This person has already been invited to your team.");
                    failInvitations.add(tmp);
                    continue;
                }
                query.querySQLString("SELECT users.id FROM users LEFT JOIN teamUsers ON users.id = teamUsers.user_id WHERE users.email = ? OR teamUsers.email = ?;");
                query.setParameter(1, email);
                query.setParameter(2, email);
                if (!query.list().isEmpty()) {
                    JSONObject tmp = new JSONObject();
                    tmp.put("email", email);
                    tmp.put("cause", "This email has already been taken");
                    failInvitations.add(tmp);
                    continue;
                }
                TeamUser teamUser = new TeamUser(firstName, lastName, email, username, null, false, team, new TeamUserPermissions(TeamUserPermissions.Role.MEMBER.getValue()));
                team.addTeamUser(teamUser);
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
                JSONObject tmp = new JSONObject();
                tmp.put("email", email);
                tmp.put("username", username);
                validInvitations.add(tmp);
            }
            query.saveOrUpdateObject(team);
            query.commit();
            res.put("validInvitations", validInvitations);
            res.put("failInvitations", failInvitations);
            sm.setResponse(ServletManager.Code.Success, res.toString());
            sm.setLogResponse("TeamUsers created");
        } catch(Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
