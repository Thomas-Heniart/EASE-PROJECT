package com.Ease.Servlet.Team;

import com.Ease.Team.TeamUser;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by thomas on 05/05/2017.
 */
@WebServlet("/ServletGetTeams")
public class ServletGetTeams extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            sm.needToBeTeamUser();
            List<TeamUser> teamUserList = sm.getTeamUsers();
            JSONArray res = new JSONArray();
            for (TeamUser teamUser : teamUserList)
                res.add(teamUser.getTeam().getSimpleJson());
            sm.setResponse(ServletManager.Code.Success, res.toString());
            sm.setLogResponse("GetTeams done");
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
