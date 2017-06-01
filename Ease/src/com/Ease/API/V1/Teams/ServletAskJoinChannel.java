package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.SendGridMail;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.CodeGenerator;
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

/**
 * Created by thomas on 05/05/2017.
 */
@WebServlet("/api/v1/teams/AskJoinChannel")
public class ServletAskJoinChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team_id);
            Integer channel_id = sm.getIntParam("channel_id", true);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            Channel channel = team.getChannelWithId(channel_id);
            if (channel.getTeamUsers().contains(teamUser))
                throw new HttpServletException(HttpStatus.BadRequest, "You already are in this channel.");
            String code;
            HibernateQuery query = sm.getHibernateQuery();
            query.querySQLString("SELECT code FROM pendingJoinChannelRequests WHERE teamUser_id = ? AND channel_id = ?;");
            query.setParameter(1, channel.getDb_id());
            query.setParameter(2, teamUser.getDb_id());
            Object rs_id = query.getSingleResult();
            if (rs_id != null)
                code = (String) rs_id;
            else {
                code = CodeGenerator.generateNewCode();
                query.querySQLString("INSERT INTO pendingJoinChannelRequests values (null, ?, ?);");
                query.setParameter(1, teamUser.getDb_id());
                query.setParameter(2, channel.getDb_id());
                query.executeUpdate();
            }
            SendGridMail mail = new SendGridMail("Agathe @Ease", "contact@ease.space");
            mail.sendJoinChannelEmail(team.getName(), channel.getName(), team.getAdministratorsUsernameAndEmail(), teamUser.getUsername(), teamUser.getEmail(), code);
            sm.setSuccess("You request has been sent");
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
