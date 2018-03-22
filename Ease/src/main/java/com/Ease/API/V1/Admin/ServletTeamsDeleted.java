package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
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
import java.util.List;

@WebServlet("/api/v1/admin/TeamsDeleted")
public class ServletTeamsDeleted extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT t FROM Team t WHERE t.active IS false");
            List<Team> teams = hibernateQuery.list();
            JSONArray res = new JSONArray();
            for (Team team : teams) {
                TeamUser owner = team.getTeamUserOwner();
                User user_owner = owner.getUser();
                JSONObject tmp = new JSONObject();
                tmp.put("name", team.getName());
                tmp.put("phone_number", user_owner != null ? user_owner.getPersonalInformation().getPhone_number() : "Invalid");
                tmp.put("email", owner.getEmail());
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
