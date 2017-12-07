package com.Ease.API.V1.Admin;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

@WebServlet("/api/v1/admin/GetTeamsInformation")
public class ServletGetTeamsInformation extends HttpServlet {

    private final static int EASE_FIRST_WEEK = 40;
    private final static int EASE_FIRST_YEAR = 2017;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            List<Team> teamList = teamManager.getTeams(sm.getHibernateQuery());
            JSONArray res = new JSONArray();
            for (Team team : teamList) {
                sm.initializeTeamWithContext(team);
                JSONObject tmp = new JSONObject();
                tmp.put("id", team.getDb_id());
                tmp.put("name", team.getName());
                TeamUser owner = team.getTeamUserOwner();
                tmp.put("admin_first_name", owner.getFirstName());
                tmp.put("admin_last_name", owner.getLastName());
                tmp.put("admin_email", owner.getEmail());
                tmp.put("plan_id", team.isActive() ? team.getPlan_id() : -1);
                tmp.put("card_entered", team.isCard_entered());
                String phoneNumber = owner.getPhone_number();
                if (phoneNumber == null)
                    phoneNumber = "";
                tmp.put("phone_number", phoneNumber);
                tmp.put("credit", team.isActive() ? (float) -team.getCustomer().getAccountBalance() / 100 : 0);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(team.getSubscription_date());
                int week_of_subscription = 0;
                if (calendar.get(Calendar.YEAR) > EASE_FIRST_YEAR) {
                    do {
                        week_of_subscription += calendar.get(Calendar.WEEK_OF_YEAR);
                        calendar.add(Calendar.WEEK_OF_YEAR, -calendar.get(Calendar.WEEK_OF_YEAR));
                    } while (calendar.get(Calendar.YEAR) > EASE_FIRST_YEAR);
                }
                week_of_subscription += calendar.get(Calendar.WEEK_OF_YEAR) - EASE_FIRST_WEEK;
                calendar = Calendar.getInstance();
                int week_now = 0;
                if (calendar.get(Calendar.YEAR) > EASE_FIRST_YEAR) {
                    do {
                        week_now += calendar.get(Calendar.WEEK_OF_YEAR);
                        calendar.add(Calendar.WEEK_OF_YEAR, -calendar.get(Calendar.WEEK_OF_YEAR));
                    } while (calendar.get(Calendar.YEAR) > EASE_FIRST_YEAR);
                }
                week_now += calendar.get(Calendar.WEEK_OF_YEAR) - EASE_FIRST_WEEK;
                tmp.put("week_of_subscription", week_of_subscription);
                tmp.put("week_now", week_now);
                res.add(tmp);
            }
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
