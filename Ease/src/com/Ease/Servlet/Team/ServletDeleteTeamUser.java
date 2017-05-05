package com.Ease.Servlet.Team;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Team.TeamUserChannel;
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
 * Created by thomas on 02/05/2017.
 */
@WebServlet("/ServletDeleteTeamUser")
public class ServletDeleteTeamUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            HibernateQuery query = new HibernateQuery();
            String team_id = sm.getServletParam("team_id", true);
            String teamUser_id = sm.getServletParam("teamUser_id", true);
            if (teamUser_id == null || teamUser_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "teamUser_id is needed.");
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "team_id is needed.");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser = team.getTeamUserWithId(Integer.parseInt(teamUser_id));
            team.removeTeamUser(teamUser);
            for (TeamUserChannel teamUserChannel : team.getTeamUserChannels()) {
                if (teamUserChannel.getTeamUser_owner() == teamUser || teamUserChannel.getTeamUser_tenant() == teamUser) {
                    team.removeTeamUserChannel(teamUserChannel);
                    query.deleteObject(teamUserChannel);
                }
            }
            query.deleteObject(teamUser);
            query.commit();
            sm.setResponse(ServletManager.Code.Success, "teamUser deleted");
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
