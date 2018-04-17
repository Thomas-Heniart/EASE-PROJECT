package com.Ease.API.V1.Teams.upgrade;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/initialize-team-score")
public class ServletInitializeTeamScore extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamId = sm.getIntParam("team", true, false);
            Team team = sm.getTeam(teamId);
            sm.needToBeAdminOfTeam(team);
            if (!team.isPasswordScoreInitialize()) {
                team.setPasswordScoreInitialize(true);
                sm.saveOrUpdate(team);
                String teamKey = sm.getTeamKey(team);
                HibernateQuery hibernateQuery = sm.getHibernateQuery();
                for (TeamCard teamCard : team.getTeamCardSet()) {
                    teamCard.decipher(teamKey);
                    if (teamCard.isTeamSingleCard()) {
                        teamCard.calculatePasswordScore();
                        hibernateQuery.saveOrUpdateObject(teamCard);
                    } else if (teamCard.isTeamEnterpriseCard()) {
                        for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values()) {
                            ((TeamEnterpriseCardReceiver) teamCardReceiver).calculatePasswordScore();
                            hibernateQuery.saveOrUpdateObject(teamCardReceiver);
                        }
                    }
                }
            }
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
