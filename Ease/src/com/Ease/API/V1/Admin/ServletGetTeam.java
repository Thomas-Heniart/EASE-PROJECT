package com.Ease.API.V1.Admin;

import com.Ease.Metrics.TeamMetrics;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@WebServlet("/api/v1/admin/GetTeam")
public class ServletGetTeam extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Integer year = sm.getIntParam("year", true, true);
            Integer week_of_year = sm.getIntParam("week_of_year", true, true);
            if (year == null || week_of_year == null) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                week_of_year = calendar.get(Calendar.WEEK_OF_YEAR);
            }
            sm.setSuccess(TeamMetrics.getMetrics(team_id, year, week_of_year, sm.getHibernateQuery()).getJson());
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
