package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.NewDashboard.ClassicApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamEnterpriseCard;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
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

@WebServlet("/api/v1/teams/EditTeamEnterpriseCard")
public class EditTeamEnterpriseCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamEnterpriseCard())
                throw new HttpServletException(HttpStatus.Forbidden);
            String description = sm.getStringParam("description", true, true);
            if (description == null)
                description = "";
            if (description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Description size must be under 255 characters");
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter password_reminder_interval");
            TeamEnterpriseCard teamEnterpriseCard = (TeamEnterpriseCard) teamCard;
            teamEnterpriseCard.setPassword_reminder_interval(password_reminder_interval);
            teamEnterpriseCard.getTeamCardReceiverMap().values().stream().forEach(teamCardReceiver -> {
                TeamEnterpriseCardReceiver teamEnterpriseCardReceiver = (TeamEnterpriseCardReceiver) teamCardReceiver;
                ClassicApp classicApp = (ClassicApp) teamEnterpriseCardReceiver.getApp();
                if (classicApp.getAccount() != null)
                    classicApp.getAccount().setReminder_interval(password_reminder_interval);
            });
            sm.saveOrUpdate(teamCard);
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
