package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.SendGridMail;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 05/05/2017.
 */
@WebServlet("/api/v1/teams/AskJoinTeam")
public class ServletAskJoinTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String team_name = sm.getServletParam("team_name", true);
            if (team_name == null || team_name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Empty team_id");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithName(team_name);
            String code;
            HibernateQuery query = new HibernateQuery();
            query.querySQLString("SELECT code FROM pendingJoinTeamRequests WHERE user_id = ? AND team_id = ?;");
            query.setParameter(1, team.getDb_id());
            query.setParameter(2, sm.getUser().getDBid());
            Object rs_id = query.getSingleResult();
            if (rs_id != null)
                code = (String) rs_id;
            else {
                code = CodeGenerator.generateNewCode();
                query.querySQLString("INSERT INTO pendingJoinTeamRequests values (null, ?, ?);");
                query.setParameter(1, team.getDb_id());
                query.setParameter(2, sm.getUser().getDBid());
                query.executeUpdate();
            }
            query.commit();
            SendGridMail mail = new SendGridMail("Agathe @Ease", "contact@ease.space");
            mail.sendJoinTeamEmail(team.getName(), team.getAdministratorsUsernameAndEmail(), sm.getUser().getFirstName(), sm.getUser().getEmail(), code);
            sm.setResponse(ServletManager.Code.Success, "You request has been sent");
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
