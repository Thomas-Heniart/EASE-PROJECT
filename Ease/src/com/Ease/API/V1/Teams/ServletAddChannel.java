package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
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
@WebServlet("/api/v1/teams/AddChannel")
public class ServletAddChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            HibernateQuery query = new HibernateQuery();
            String team_id = sm.getServletParam("team_id", true);
            String channel_name = sm.getServletParam("channel_name", true);
            String purpose = sm.getServletParam("purpose", true);
            if (channel_name == null || channel_name.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "channel_id is needed.");
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "team_id is needed.");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            Channel channel = new Channel(team, channel_name, purpose);
            query.saveOrUpdateObject(channel);
            team.addChannel(channel);
            query.commit();
            sm.setResponse(ServletManager.Code.Success, channel.getJson().toString());
            sm.setLogResponse("Channel created");
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
