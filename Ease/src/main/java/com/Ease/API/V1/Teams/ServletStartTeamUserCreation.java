package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
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
import org.hibernate.exception.ConstraintViolationException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by thomas on 24/05/2017.
 */
@WebServlet("/api/v1/teams/StartTeamUserCreation")
public class ServletStartTeamUserCreation extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            TeamUser adminTeamUser = sm.getTeamUser(team);
            String email = sm.getStringParam("email", true, false);
            email = email.toLowerCase();
            String username = sm.getStringParam("username", true, true);
            if (username == null)
                username = "";
            username = username.toLowerCase();
            Integer role = sm.getIntParam("role", true, false);
            if (!team.isValidFreemium() && role != TeamUserRole.Role.MEMBER.getValue())
                throw new HttpServletException(HttpStatus.Forbidden, "You must upgrade to have multiple admins.");
            if (email.equals("") || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "That doesn't look like a valid email address!");
            System.out.println("Username: " + username);
            if (username.equals("") || team.hasTeamUserWithUsername(username)) {
                username = email.substring(0, email.indexOf("@"));
                username = username.replaceAll("[\\W]", "_");
                if (team.hasTeamUserWithUsername(username)) {
                    int suffixe = 1;
                    while (team.hasTeamUserWithUsername(username + suffixe))
                        suffixe++;
                    username += suffixe;
                }
            }
            checkUsernameIntegrity(username);
            if (role == null || !TeamUserRole.isInferiorToOwner(role) || !TeamUserRole.isValidValue(role))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid inputs");
            if (!team.isValidFreemium() && TeamUserRole.Role.MEMBER.getValue() != role)
                throw new HttpServletException(HttpStatus.BadRequest, "You must upgrade to add other admins.");
            for (TeamUser teamUser : team.getTeamUsers().values()) {
                if (teamUser.getEmail().equals(email))
                    throw new HttpServletException(HttpStatus.BadRequest, "This person is already on your team.");
            }
            HibernateQuery query = sm.getHibernateQuery();
            Long departure_date = sm.getLongParam("departure_date", true, true);
            Long arrival_date = sm.getLongParam("arrival_date", true, true);
            if (!team.isValidFreemium()) {
                arrival_date = null;
                departure_date = null;
            }
            else if (arrival_date != null && arrival_date <= sm.getTimestamp().getTime())
                throw new HttpServletException(HttpStatus.BadRequest, "Arrival date cannot be past.");
            else if (departure_date != null && departure_date <= sm.getTimestamp().getTime())
                throw new HttpServletException(HttpStatus.BadRequest, "Departure date cannot be past.");
            else if (arrival_date != null && departure_date != null && arrival_date > departure_date)
                throw new HttpServletException(HttpStatus.BadRequest, "Arrival date must be before departure date");
            TeamUser teamUser = new TeamUser(email, username, arrival_date == null ? null : new Date(arrival_date), null, team, new TeamUserRole(role));
            teamUser.setAdmin_id(adminTeamUser.getDb_id());
            if (departure_date != null)
                teamUser.setDepartureDate(new Date(departure_date));
            String code;
            do {
                code = CodeGenerator.generateNewCode();
                query.querySQLString("SELECT * FROM teamUsers WHERE invitation_code = ?");
                query.setParameter(1, code);
            } while (!query.list().isEmpty());
            teamUser.setInvitation_code(code);
            try {
                sm.saveOrUpdate(teamUser);
            } catch (ConstraintViolationException e) {
                e.printStackTrace();
                throw new HttpServletException(HttpStatus.BadRequest, "This person is already on your team.");
            }
            team.addTeamUser(teamUser);
            team.getDefaultChannel().addTeamUser(teamUser);
            sm.saveOrUpdate(team.getDefaultChannel());
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CREATED, teamUser.getWebSocketJson()));
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
            throw new HttpServletException(HttpStatus.BadRequest, "Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
