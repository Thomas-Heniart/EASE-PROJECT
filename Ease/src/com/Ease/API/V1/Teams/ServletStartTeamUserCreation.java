package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.SendGridMail;
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
@WebServlet("/ServletStartTeamUserCreation")
public class ServletStartTeamUserCreation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        HibernateQuery query = new HibernateQuery();
        try {
            String team_id = sm.getServletParam("team_id", true);
            String email = sm.getServletParam("email", true);
            String firstName = sm.getServletParam("firstName", true);
            String lastName = sm.getServletParam("lastName", true);
            String username = sm.getServletParam("username", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null");
            if (email == null || email.equals("") || !Regex.isEmail(email) || username == null || username.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Invalid email or username");
            query.querySQLString("SELECT id FROM teamUsers WHERE email = ? AND team_id = ? AND verified = 1;");
            query.setParameter(1, email);
            query.setParameter(2, team_id);
            if (!query.list().isEmpty()) {
                JSONObject tmp = new JSONObject();
                tmp.put("email", email);
                tmp.put("cause", "This person is already on your team.");
                failInvitations.add(tmp);
                System.out.println("fail invitations 1");
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
                System.out.println("fail invitations 2");
                continue;
            }
            query.querySQLString("SELECT id FROM users WHERE email = ?;");
            query.setParameter(1, email);
            if (!query.list().isEmpty()) {
                query.querySQLString("SELECT id FROM teamUsers WHERE email = ?");
                query.setParameter(1, email);
                if (!query.list().isEmpty()) {
                    JSONObject tmp = new JSONObject();
                    tmp.put("email", email);
                    tmp.put("cause", "This email has already been taken");
                    failInvitations.add(tmp);
                    System.out.println("fail invitations 3");
                    continue;
                }
            }
            System.out.println("teamUser creation");
            TeamUser teamUser = new TeamUser(firstName, lastName, email, username, null, false, team, new TeamUserPermissions(TeamUserPermissions.Role.MEMBER.getValue()));
            team.getGeneralChannel().addTeamUser(teamUser);
            query.saveOrUpdateObject(teamUser);
            teamUsersToAdd.add(teamUser);
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
            System.out.println("valid invitations");
            JSONObject tmp = new JSONObject();
            tmp.put("email", email);
            tmp.put("username", username);
            validInvitations.add(tmp);
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
