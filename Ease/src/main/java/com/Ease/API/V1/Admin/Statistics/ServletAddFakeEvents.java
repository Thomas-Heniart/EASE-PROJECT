package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Metrics.EaseEvent;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet("/ServletAddFakeEvents")
public class ServletAddFakeEvents extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < 1000; i++) {
                EaseEvent easeEvent = new EaseEvent();
                easeEvent.setName("PasswordUsed");
                JSONObject data = new JSONObject();
                data.put("type", "classicApp");
                data.put("from", this.getFrom());
                easeEvent.setData(data.toString());
                easeEvent.setUser_id(ThreadLocalRandom.current().nextInt(3122, 3127));
                calendar.add(Calendar.DAY_OF_YEAR, ThreadLocalRandom.current().nextInt(-5, 6));
                easeEvent.setCreation_date(calendar);
                sm.getTrackingHibernateQuery().saveOrUpdateObject(easeEvent);
            }
            sm.setSuccess("Done");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private String getFrom() {
        switch (ThreadLocalRandom.current().nextInt(0, 4)) {
            case 0:
                return "DashboardClick";
            case 1:
                return "Extension";
            case 2:
                return "FillIn";
            case 3:
                return "Copy";
            default:
                return "WTF";
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
