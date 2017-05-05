package com.Ease.Servlet.Team;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.SendGridMail;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 03/05/2017.
 */
@WebServlet("/ServletSendJoinTeamInvitation")
public class ServletSendJoinTeamInvitation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String team_id = sm.getServletParam("team_id", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Empty team_id");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser adminTeamUser = sm.getTeamUserForTeam(team);
            if (adminTeamUser == null || !adminTeamUser.isTeamAdmin() || !user.isAdmin())
                throw new GeneralException(ServletManager.Code.ClientError, "Not allowed to do this");
            String email = sm.getServletParam("email", true);
            String firstName = sm.getServletParam("firstName", true);
            if (email == null || email.equals("") || !Regex.isEmail(email))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Invalid email field.");
            if (firstName == null || firstName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty first name.");
            String code;
            HibernateQuery query = new HibernateQuery();
            query.querySQLString("SELECT code FROM pendingTeamInvitations WHERE email = ?");
            query.setParameter(1, email);
            Object id = query.getSingleResult();
            if (id == null) {
                code = CodeGenerator.generateNewCode();
                query.querySQLString("INSERT INTO pendingTeamInvitations values(NULL, ?, ?, ?);");
                query.setParameter(1, email);
                query.setParameter(2, code);
                query.setParameter(3, team.getDb_id());
                query.executeUpdate();
            } else
                code = (String) id;
            query.commit();
            SendGridMail sendGridMail = new SendGridMail("Thomas @EaseSpace", "thomas@ease.space");
            sendGridMail.sendInvitationToJoinTeamEmail(team.getName(), adminTeamUser.getFirstName(), firstName, email, code);
            sm.setResponse(ServletManager.Code.Success, "Invitation to join a team sent");
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
