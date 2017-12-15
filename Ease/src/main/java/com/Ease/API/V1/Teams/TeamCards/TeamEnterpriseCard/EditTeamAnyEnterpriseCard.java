package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamEnterpriseCard;
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

@WebServlet("/api/v1/teams/EditTeamAnyEnterpriseApp")
public class EditTeamAnyEnterpriseCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamEnterpriseCard teamEnterpriseCard = (TeamEnterpriseCard) hibernateQuery.get(TeamEnterpriseCard.class, team_card_id);
            if (teamEnterpriseCard == null)
                throw new HttpServletException(HttpStatus.BadRequest, "no such teamCard");
            Team team = teamEnterpriseCard.getTeam();
            sm.needToBeAdminOfTeam(team);
            String name = sm.getStringParam("name", true, false);
            teamEnterpriseCard.setName(name);
            sm.saveOrUpdate(teamEnterpriseCard);
            sm.setSuccess(teamEnterpriseCard.getJson());
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
