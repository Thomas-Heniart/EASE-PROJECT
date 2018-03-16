package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@WebServlet("/api/v1/admin/GetUsersCohortData")
public class ServletGetUsersCohortData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Calendar start_calendar = Calendar.getInstance();
            Calendar end_calendar = Calendar.getInstance();
            Long start_week_ms = sm.getLongParam("start_week_ms", true, true);
            if (start_week_ms == null)
                start_calendar.add(Calendar.WEEK_OF_YEAR, -5);
            else
                start_calendar.setTimeInMillis(start_week_ms);
            start_calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Long end_week_ms = sm.getLongParam("end_week_ms", true, true);
            if (end_week_ms != null) {
                if (start_week_ms != null && end_week_ms < start_week_ms)
                    throw new HttpServletException(HttpStatus.BadRequest, "End cannot be before start");
                end_calendar.setTimeInMillis(end_week_ms);
            }
            end_calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            HibernateQuery hibernateQuery = sm.getTrackingHibernateQuery();
            hibernateQuery.queryString("SELECT e.w FROM EaseEvent e WHERE e.creation_date BETWEEN :start_week AND :end_week");
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
