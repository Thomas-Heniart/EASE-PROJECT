package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;

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
@WebServlet("/api/v1/teams/FinalizeRegistration")
public class ServletFinalizeTeamUserRegistration extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String firstName = sm.getStringParam("first_name", true);
            String lastName = sm.getStringParam("last_name", true);
            String username = sm.getStringParam("username", true);
            String jobTitle = sm.getStringParam("job_title", true);
            String code = sm.getStringParam("code", true);
            if (username == null || username.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "username is needed.");
            if (firstName == null || firstName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "firstName is needed.");
            if (lastName == null || lastName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "lastName is needed.");
            if (code == null || code.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "code is needed.");
            if (jobTitle == null || jobTitle.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "jobTitle is needed.");
            HibernateQuery query = sm.getHibernateQuery();
            query.querySQLString("SELECT id, team_id, teamUser_id FROM pendingTeamInvitations WHERE code = ?");
            query.setParameter(1, code);
            Object idTeamAndTeamUserObj = query.getSingleResult();
            if (idTeamAndTeamUserObj == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot be part of this team");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Object[] idTeamAndTeamUser = (Object[]) idTeamAndTeamUserObj;
            Integer id = (Integer) idTeamAndTeamUser[0];
            Integer team_id = (Integer) idTeamAndTeamUser[1];
            if (sm.getTeamUserForTeamId(team_id) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot have two accounts in a team.");
            Integer teamUser_id = (Integer) idTeamAndTeamUser[2];
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            teamUser.setFirstName(firstName);
            teamUser.setLastName(lastName);
            teamUser.setUsername(username);
            teamUser.setJobTitle(jobTitle);
            query.saveOrUpdateObject(teamUser);
            query.querySQLString("DELETE FROM pendingTeamInvitations WHERE id = ?");
            query.setParameter(1, id);
            query.executeUpdate();
            String verificationCode;
            do {
                verificationCode = CodeGenerator.generateNewCode();
                query.querySQLString("SELECT * FROM pendingTeamUserVerifications WHERE code = ?");
                query.setParameter(1, verificationCode);
            } while (query.getSingleResult() != null);
            query.querySQLString("INSERT INTO pendingTeamUserVerifications values(NULL, ?, ?);");
            query.setParameter(1, teamUser.getDb_id());
            query.setParameter(2, code);
            query.executeUpdate();
            teamUser.setDashboard_user(sm.getUser());
            sm.saveOrUpdate(teamUser);
            team.askVerificationForTeamUser(teamUser, verificationCode);
            if (teamUser.getAdmin_id() == null || teamUser.getAdmin_id() == 0)
                throw new HttpServletException(HttpStatus.BadRequest, "The user must be invited by an admin");
            TeamUser teamUser_admin = team.getTeamUserWithId(teamUser.getAdmin_id());
            sm.saveOrUpdate(teamUser_admin.addNotification(teamUser.getUsername() + " is ready to join your team. Give your final approval to give the access.", sm.getTimestamp()));
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setTemplateId(180141);
            mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
            mailJetBuilder.addTo(teamUser_admin.getEmail());
            mailJetBuilder.addVariable("first_name", teamUser.getFirstName());
            mailJetBuilder.addVariable("last_name", teamUser.getLastName());
            mailJetBuilder.addVariable("team_name", team.getName());
            mailJetBuilder.addVariable("user_pseudo", teamUser.getUsername());
            mailJetBuilder.addVariable("user_email", teamUser.getEmail());
            mailJetBuilder.sendEmail();
            sm.setSuccess(teamUser.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            String code = sm.getParam("code", false);
            if (code == null || code.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "No code provided.");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT team_id, teamUser_id FROM pendingTeamInvitations WHERE code = ?");
            hibernateQuery.setParameter(1, code);
            Integer[] teamAndTeamUserId = (Integer[]) hibernateQuery.getSingleResult();
            hibernateQuery.commit();
            if (teamAndTeamUserId == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Your code is invalid.");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(teamAndTeamUserId[0]);
            TeamUser teamUser = team.getTeamUserWithId(teamAndTeamUserId[1]);
            sm.setSuccess(teamUser.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
    }
}
