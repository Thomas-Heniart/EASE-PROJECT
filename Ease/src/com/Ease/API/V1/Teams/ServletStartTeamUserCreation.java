package com.Ease.API.V1.Teams;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Team.TeamUserRole;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thomas on 24/05/2017.
 */
@WebServlet("/api/v1/teams/StartTeamUserCreation")
public class ServletStartTeamUserCreation extends HttpServlet {
    private static final SimpleDateFormat departure_format = new SimpleDateFormat("yyyy-MM-dd");

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
            if (email == null || email.equals("") || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "That doesn't look like a valid email address!");
            checkUsernameIntegrity(username);
            if (role == null || !TeamUserRole.isInferiorToOwner(role) || !TeamUserRole.isValidValue(role))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid inputs");
            String first_name = sm.getStringParam("first_name", true);
            String last_name = sm.getStringParam("last_name", true);
            HibernateQuery query = sm.getHibernateQuery();
            query.querySQLString("SELECT id FROM teamUsers WHERE email = ? AND team_id = ?;");
            query.setParameter(1, email);
            query.setParameter(2, team_id);
            if (!query.list().isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "This person is already on your team.");
            query.querySQLString("SELECT id FROM teamUsers WHERE username = ? AND team_id = ?;");
            query.setParameter(1, username);
            query.setParameter(2, team_id);
            if (!query.list().isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "Username is already taken");
            Date arrival_date = sm.getTimestamp();
            String departure_date_string = sm.getStringParam("departure_date", true);
            Date departure_date = null;
            if (departure_date_string != null && !departure_date_string.equals("")) {
                departure_date = departure_format.parse(departure_date_string);
                if (departure_date.getTime() < sm.getTimestamp().getTime())
                    throw new HttpServletException(HttpStatus.BadRequest, "Departure date cannot be past.");
            }
            TeamUser teamUser = new TeamUser(first_name, last_name, email, username, arrival_date, null, team, new TeamUserRole(role));
            teamUser.setAdmin_id(adminTeamUser.getDb_id());
            if (departure_date != null)
                teamUser.setDepartureDate(departure_date);
            query.saveOrUpdateObject(teamUser);
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
            query.commit();
            team.getDefaultChannel().addTeamUser(teamUser, sm.getDB());
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
            mailJetBuilder.setTemplateId(179023);
            mailJetBuilder.addTo(email);
            mailJetBuilder.addVariable("team_name", team.getName());
            mailJetBuilder.addVariable("first_name", adminTeamUser.getFirstName());
            mailJetBuilder.addVariable("last_name", adminTeamUser.getLastName());
            mailJetBuilder.addVariable("email", adminTeamUser.getEmail());
            mailJetBuilder.addVariable("link", Variables.URL_PATH + "teams#/teamJoin/" + code);
            mailJetBuilder.sendEmail();
            /* SendGridMail sendGridMail = new SendGridMail("Benjamin @EaseSpace", "benjamin@ease.space");
            sendGridMail.sendInvitationToJoinTeamEmail(team.getName(), adminTeamUser.getFirstName(), adminTeamUser.getEmail(), email, code); */
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.ADDED, teamUser.getJson(), teamUser.getOrigin()));
            sm.setSuccess(teamUser.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void checkUsernameIntegrity(String username) throws HttpServletException {
        if (username == null || username.equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "Usernames can't be empty!");
        if (username.length() >= 22 || username.length() < 3)
            throw new HttpServletException(HttpStatus.BadRequest, "Sorry, usernames must be between 3 and 21 characters.");
        if (!username.equals(username.toLowerCase()) || !Regex.isValidUsername(username))
            throw new HttpServletException(HttpStatus.BadRequest, "Sorry, usernames must contain only lowercase characters.");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
