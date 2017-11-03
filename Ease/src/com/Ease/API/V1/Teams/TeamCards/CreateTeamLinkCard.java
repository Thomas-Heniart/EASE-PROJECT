package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamLinkCard;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/CreateTeamLinkCard")
public class CreateTeamLinkCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            sm.needToBeTeamUserOfTeam(team);
            Integer channel_id = sm.getIntParam("channel_id", true, false);
            Channel channel = team.getChannelWithId(channel_id);
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            if (!channel.getTeamUsers().contains(teamUser_connected))
                throw new HttpServletException(HttpStatus.Forbidden, "You must be part of the room.");
            String url = sm.getStringParam("url", true, false);
            if (url.length() >= 2000 || url.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            String img_url = sm.getStringParam("img_url", true, false);
            if (img_url.length() >= 2000 || img_url.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter img_url");
            String name = sm.getStringParam("name", true, false);
            if (name.length() >= 255 || name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            TeamCard teamCard = new TeamLinkCard(team, channel, name, url, img_url);
            sm.saveOrUpdate(teamCard);
            channel.addTeamCard(teamCard);
            team.addTeamCard(teamCard);
            sm.setSuccess(teamCard.getJson());
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
