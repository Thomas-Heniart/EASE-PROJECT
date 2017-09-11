package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
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
import java.util.LinkedList;
import java.util.List;

@WebServlet("/api/v1/admin/GetTeamsInformation")
public class ServletGetTeamsInformation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            List<Team> teamList = new LinkedList<>();
            teamList.addAll(teamManager.getTeams());
            hibernateQuery.queryString("SELECT t FROM Team t WHERE t.active = false");
            teamList.addAll(hibernateQuery.list());
            JSONArray res = new JSONArray();
            for (Team team : teamList) {
                JSONObject tmp = new JSONObject();
                tmp.put("id", team.getDb_id());
                tmp.put("name", team.getName());
                TeamUser owner = team.getTeamUserOwner();
                tmp.put("admin_first_name", owner.getFirstName());
                tmp.put("admin_last_name", owner.getLastName());
                tmp.put("admin_email", owner.getEmail());
                String phoneNumber = owner.getPhone_number();
                if (phoneNumber == null)
                    phoneNumber = "";
                tmp.put("phone_number", phoneNumber);
                tmp.put("team_users_size", team.getTeamUsers().size());
                tmp.put("active_team_users", team.getActiveTeamUserNumber());
                tmp.put("is_active", team.isActive());
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
