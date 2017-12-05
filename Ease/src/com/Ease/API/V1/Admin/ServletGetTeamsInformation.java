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
import java.util.List;

@WebServlet("/api/v1/admin/GetTeamsInformation")
public class ServletGetTeamsInformation extends HttpServlet {
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
                tmp.put("team_users_size", team.getTeamUsers().values().stream().filter(TeamUser::isVerified).count());
                tmp.put("active_team_users", team.getActiveTeamUserNumber());
                tmp.put("people_click_on_app_three_days", team.getNumberOfPeopleWhoClickOnApps(3, sm.getHibernateQuery()));
                tmp.put("is_active", team.isActive());
                tmp.put("credit", team.isActive() ? (float) -team.getCustomer().getAccountBalance() / 100 : 0);
                int card_number = 0;
                int link_number = 0;
                int single_number = 0;
                int enterprise_number = 0;
                int card_with_password_reminder = 0;
                tmp.put("card_number", card_number);
                tmp.put("link_number", link_number);
                tmp.put("single_number", single_number);
                tmp.put("enterprise_number", enterprise_number);
                tmp.put("card_with_password_reminder", card_with_password_reminder);
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
