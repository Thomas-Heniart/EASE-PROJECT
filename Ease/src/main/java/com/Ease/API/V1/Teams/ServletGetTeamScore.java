package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/v1/teams/GetTeamScore")
public class ServletGetTeamScore extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamId = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(teamId);
            sm.needToBeAdminOfTeam(team);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT card.passwordScore FROM TeamSingleCard card WHERE card.passwordScore IS NOT NULL AND card.team.db_id = :team_id");
            hibernateQuery.setParameter("team_id", teamId);
            List<Integer> scores = hibernateQuery.list();
            hibernateQuery.queryString("SELECT card.passwordScore FROM TeamSingleSoftwareCard card WHERE card.passwordScore IS NOT NULL AND card.team.db_id = :team_id");
            hibernateQuery.setParameter("team_id", teamId);
            scores.addAll(hibernateQuery.list());
            hibernateQuery.queryString("SELECT receiver.passwordScore FROM TeamEnterpriseCardReceiver receiver WHERE receiver.passwordScore IS NOT NULL AND receiver.teamCard.team.db_id = :team_id");
            hibernateQuery.setParameter("team_id", teamId);
            scores.addAll(hibernateQuery.list());
            Integer count = scores.size();
            scores = scores.stream().filter(integer -> integer == 4).collect(Collectors.toList());
            JSONObject res = new JSONObject();
            res.put("password_count", count);
            res.put("strong_password_count", scores.size());
            sm.setSuccess(res);
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
