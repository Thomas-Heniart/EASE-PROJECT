package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
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

@WebServlet("/api/v1/teams/SendFillerReminder")
public class ServletSendFillerReminder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamCard_id = sm.getIntParam("team_card_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamSingleCard teamSingleCard = (TeamSingleCard) hibernateQuery.get(TeamSingleCard.class, teamCard_id);
            if (teamSingleCard == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such teamCard");
            Team team = teamSingleCard.getTeam();
            sm.needToBeTeamUserOfTeam(team);
            TeamUser filler = teamSingleCard.getTeamUser_filler();
            if (filler == null) {
                TeamUser room_manager = teamSingleCard.getChannel().getRoom_manager();
                NotificationFactory.getInstance().createRemindTeamSingleCardFiller(teamSingleCard, sm.getTeamUser(teamSingleCard.getTeam()), room_manager, sm.getUserWebSocketManager(room_manager.getUser().getDb_id()), sm.getHibernateQuery());
            } else
                NotificationFactory.getInstance().createRemindTeamSingleCardFiller((TeamSingleCardReceiver) teamSingleCard.getTeamCardReceiver(filler), sm.getTeamUser(teamSingleCard.getTeam()), sm.getUserIdMap(), sm.getHibernateQuery());
            sm.setSuccess("Notification sent");
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
