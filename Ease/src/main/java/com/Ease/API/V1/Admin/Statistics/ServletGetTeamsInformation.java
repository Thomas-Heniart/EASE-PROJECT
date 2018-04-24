package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Metrics.TeamMetrics;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONArray;
import org.json.JSONObject;

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

    private static final int EASE_FIRST_WEEK = 40;
    private static final int EASE_FIRST_YEAR = 2017;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            List<Team> teamList = teamManager.getTeams(sm.getHibernateQuery());
            JSONArray res = new JSONArray();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
            int year = calendar.get(Calendar.YEAR);
            int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
            for (Team team : teamList) {
                sm.initializeTeamWithContext(team);
                JSONObject tmp = new JSONObject();
                tmp.put("id", team.getDb_id());
                tmp.put("name", team.getName());
                TeamUser owner = team.getTeamUserOwner();
                tmp.put("admin_first_name", owner.getUser().getPersonalInformation().getFirst_name());
                tmp.put("admin_last_name", owner.getUser().getPersonalInformation().getLast_name());
                tmp.put("admin_email", owner.getEmail());
                tmp.put("plan_id", team.isActive() ? team.getPlan_id() : -1);
                tmp.put("card_entered", team.isCard_entered());
                String phoneNumber = owner.getUser().getPersonalInformation().getPhone_number();
                if (phoneNumber == null)
                    phoneNumber = "";
                tmp.put("phone_number", phoneNumber);
                tmp.put("credit", team.isActive() ? (float) -team.getCustomer().getAccountBalance() / 100 : 0);
                calendar.setTime(team.getSubscription_date());
                int weekOfSubscription = 0;
                if (calendar.get(Calendar.YEAR) > EASE_FIRST_YEAR) {
                    do {
                        weekOfSubscription += calendar.get(Calendar.WEEK_OF_YEAR);
                        calendar.add(Calendar.WEEK_OF_YEAR, -calendar.get(Calendar.WEEK_OF_YEAR));
                    } while (calendar.get(Calendar.YEAR) > EASE_FIRST_YEAR);
                }
                weekOfSubscription += calendar.get(Calendar.WEEK_OF_YEAR) - EASE_FIRST_WEEK;
                calendar = Calendar.getInstance();
                int weekNow = 0;
                if (calendar.get(Calendar.YEAR) > EASE_FIRST_YEAR) {
                    do {
                        weekNow += calendar.get(Calendar.WEEK_OF_YEAR);
                        calendar.add(Calendar.WEEK_OF_YEAR, -calendar.get(Calendar.WEEK_OF_YEAR));
                    } while (calendar.get(Calendar.YEAR) > EASE_FIRST_YEAR);
                }
                weekNow += calendar.get(Calendar.WEEK_OF_YEAR) - EASE_FIRST_WEEK;
                tmp.put("week_of_subscription", weekOfSubscription);
                tmp.put("week_now", weekNow);
                TeamMetrics teamMetrics = TeamMetrics.getMetrics(team.getDb_id(), year, weekOfYear, sm.getHibernateQuery());
                tmp.put("people_joined", teamMetrics.getPeople_joined());
                tmp.put("people_joined_with_cards", teamMetrics.getPeople_with_cards());
                tmp.put("cards_with_tags", teamMetrics.getCards_with_receiver());
                tmp.put("people_click_on_app_once", teamMetrics.getPeople_click_on_app_once());
                tmp.put("people_click_on_app_three_times", teamMetrics.getPeople_click_on_app_three_times());
                tmp.put("password_killed", teamMetrics.getPasswordKilled());
                res.put(tmp);
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
