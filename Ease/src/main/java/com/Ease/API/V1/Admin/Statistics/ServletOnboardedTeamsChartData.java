package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/onboarded-teams-chart-data")
public class ServletOnboardedTeamsChartData extends HttpServlet {

    private static final String APP_COUNT_QUERY = "SELECT *\n" +
            "FROM (SELECT\n" +
            "        team_id,\n" +
            "        COUNT(*) AS apps\n" +
            "      FROM teamCards\n" +
            "      GROUP BY team_id) AS t\n" +
            "WHERE t.apps ";
    private static final String BETWEEN_CONDITION = "BETWEEN :min AND :max";
    private static final String SUPERIOR_CONDITION = "> :min";
    private static final String ACTIVE_TEAMS_QUERY = "SELECT\n" +
            "  teamUsers.team_id,\n" +
            "  COUNT(ease_tracking.EASE_EVENT.id) AS passwordUsed\n" +
            "FROM ease_tracking.EASE_EVENT JOIN teamUsers ON ease_tracking.EASE_EVENT.user_id = teamUsers.user_id\n" +
            "WHERE name LIKE 'PasswordUsed' AND DATE(ease_tracking.EASE_EVENT.creation_date) BETWEEN DATE_SUB(CURDATE(), INTERVAL 1 WEEK) AND CURDATE()\n" +
            "GROUP BY team_id;";
    private static final String TEAMS_ONBOARDED = "SELECT teams.id\n" +
            "FROM teams\n" +
            "  JOIN TEAM_ONBOARDING_STATUS ON teams.onboarding_status_id = TEAM_ONBOARDING_STATUS.id\n" +
            "WHERE step = 5 AND active = 1;";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            JSONArray res = new JSONArray();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString(TEAMS_ONBOARDED);
            res.put(hibernateQuery.list().size());
            hibernateQuery.querySQLString(ACTIVE_TEAMS_QUERY);
            res.put(hibernateQuery.list().size());
            hibernateQuery.querySQLString(APP_COUNT_QUERY + BETWEEN_CONDITION);
            hibernateQuery.setParameter("min", 0);
            hibernateQuery.setParameter("max", 10);
            res.put(hibernateQuery.list().size());
            hibernateQuery.querySQLString(APP_COUNT_QUERY + BETWEEN_CONDITION);
            hibernateQuery.setParameter("min", 11);
            hibernateQuery.setParameter("max", 20);
            res.put(hibernateQuery.list().size());
            hibernateQuery.querySQLString(APP_COUNT_QUERY + BETWEEN_CONDITION);
            hibernateQuery.setParameter("min", 21);
            hibernateQuery.setParameter("max", 30);
            res.put(hibernateQuery.list().size());
            hibernateQuery.querySQLString(APP_COUNT_QUERY + SUPERIOR_CONDITION);
            hibernateQuery.setParameter("min", 30);
            res.put(hibernateQuery.list().size());
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
