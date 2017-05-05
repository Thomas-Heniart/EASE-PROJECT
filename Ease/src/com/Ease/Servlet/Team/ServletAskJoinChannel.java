package com.Ease.Servlet.Team;

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
import java.io.StreamCorruptedException;

/**
 * Created by thomas on 05/05/2017.
 */
@WebServlet("/ServletAskJoinChannel")
public class ServletAskJoinChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            sm.needToBeTeamUser();
            String team_id = sm.getServletParam("team_id", true);
            String channel_id = sm.getServletParam("channel_id", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Empty team_id");
            if (channel_id == null || channel_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Empty channel_id");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            Channel channel = team.getChannelWithId(Integer.parseInt(channel_id));
            sm.getTeamUserForTeam(team);
            String code;
            HibernateQuery query = new HibernateQuery();
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
            query.commit();
            SendGridMail mail = new SendGridMail("Agathe @Ease", "contact@ease.space");
            mail.sendJoinChannelEmail(team.getName(), channel.getName(), team.getAdministratorsUsernameAndEmail(), teamUser.getUsername(), teamUser.getEmail(), code);
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
