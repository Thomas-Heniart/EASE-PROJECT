package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.LinkApp;
import com.Ease.NewDashboard.LinkAppInformation;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamLinkCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/FixLinkApps")
public class ServletFixLinkApps extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            for (Team team : teamManager.getAllTeams(hibernateQuery)) {
                for (TeamCard teamCard : team.getTeamCardSet()) {
                    if (!teamCard.isTeamLinkCard())
                        continue;
                    for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values()) {
                        LinkApp linkApp = (LinkApp) teamCardReceiver.getApp();
                        TeamCardReceiver other_receiver = teamCard.getTeamCardReceiverMap().values().stream().filter(teamCardReceiver1 -> !teamCardReceiver.equals(teamCardReceiver1) && ((LinkApp) teamCardReceiver.getApp()).getLinkAppInformation().equals(((LinkApp) teamCardReceiver1.getApp()).getLinkAppInformation())).findFirst().orElse(null);
                        if (other_receiver == null)
                            continue;
                        TeamLinkCard teamLinkCard = (TeamLinkCard) teamCard;
                        LinkAppInformation linkAppInformation = new LinkAppInformation(teamLinkCard.getUrl(), teamLinkCard.getImg_url());
                        sm.saveOrUpdate(linkAppInformation);
                        hibernateQuery.queryString("UPDATE LinkApp l SET l.linkAppInformation = :info WHERE l.db_id = :id");
                        hibernateQuery.setParameter("info", linkAppInformation);
                        hibernateQuery.setParameter("id", linkApp.getDb_id());
                        linkApp.setLinkAppInformation(linkAppInformation);
                    }
                }
            }
            sm.setSuccess("Done");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
