package com.Ease.Servlet.Team;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 02/05/2017.
 */
@WebServlet("/ServletEditTeam")
public class ServletEditTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            HibernateQuery query = new HibernateQuery();
            String team_id = sm.getServletParam("team_id", true);
            String editJsonString = sm.getServletParam("editJson", true);
            if (editJsonString == null || editJsonString.equals(""))
                editJsonString = "{}";
            JSONParser parser = new JSONParser();
            JSONObject editJson = (JSONObject) parser.parse(editJsonString);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            team.edit(editJson);
            query.saveOrUpdateObject(team);
            query.commit();
            sm.setResponse(ServletManager.Code.Success, "team edited");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
