package com.Ease.API.V1.Admin.upgrade;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Metrics.EaseEvent;
import com.Ease.Team.TeamCard.TeamCard;
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

@WebServlet("/api/v1/admin/update-team-metrics")
public class ServletUpdateTeamMetrics extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            HibernateQuery trackingHibernateQuery = sm.getTrackingHibernateQuery();
            trackingHibernateQuery.queryString("SELECT e FROM EaseEvent e WHERE e.name LIKE 'CardAdded'");
            List<EaseEvent> easeEvents = trackingHibernateQuery.list();
            for (EaseEvent easeEvent : easeEvents) {
                JSONObject data = easeEvent.getJsonData();
                int id = data.optInt("id", -1);
                hibernateQuery.queryString("SELECT t FROM TeamCard t WHERE t.id = :id");
                hibernateQuery.setParameter("id", id);
                TeamCard teamCard = (TeamCard) hibernateQuery.getSingleResult();
                if (teamCard == null)
                    data.put("sub_type", "classic");
                else
                    data.put("sub_type", teamCard.getSubtype());
                data.put("from", "Catalog");
                easeEvent.setData(data);
                trackingHibernateQuery.saveOrUpdateObject(easeEvent);
            }
            sm.setSuccess("Success");
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
