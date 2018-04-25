package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.NewDashboard.Account;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
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
import java.util.Date;

@WebServlet("/api/v1/teams/reminder-password-update")
public class PasswordReminder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamId = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(teamId);
            sm.needToBeAdminOfTeam(team);
            TeamUser teamUserConnected = sm.getTeamUser(team);
            Integer teamCardId = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(teamCardId);
            Channel channel = teamCard.getChannel();
            String teamKey = sm.getTeamKey(team);
            teamCard.decipher(teamKey);
            Account account;
            TeamUser teamUserReceiver;
            if (teamCard.isTeamSingleCard()) {
                teamUserReceiver = channel.getRoom_manager();
                account = teamCard.getAccount();
            } else if (teamCard.isTeamEnterpriseCard()) {
                Integer teamCardReceiverId = sm.getIntParam("team_card_receiver_id", true, false);
                TeamCardReceiver teamCardReceiver = teamCard.getTeamCardReceiver(teamCardReceiverId);
                teamUserReceiver = teamCardReceiver.getTeamUser();
                account = teamCardReceiver.getApp().getAccount();
            } else
                throw new HttpServletException(HttpStatus.BadRequest, "No reminder for link cards");
            if (!teamUserReceiver.isRegistered())
                throw new HttpServletException(HttpStatus.BadRequest, "This user is not registered for the moment");
            if (teamUserReceiver.equals(teamUserConnected))
                throw new HttpServletException(HttpStatus.BadRequest, "Dont push yourself to hard");
            if (account == null || !account.mustUpdatePassword())
                throw new HttpServletException(HttpStatus.BadRequest, "This password does not need to be updated");
            account.setLastPasswordReminderDate(new Date());
            sm.saveOrUpdate(account);
            NotificationFactory.getInstance().createPasswordNotUpToDateReminder(teamUserConnected, teamUserReceiver, teamCard, sm.getUserWebSocketManager(teamUserReceiver.getUser().getDb_id()), sm.getHibernateQuery());
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
