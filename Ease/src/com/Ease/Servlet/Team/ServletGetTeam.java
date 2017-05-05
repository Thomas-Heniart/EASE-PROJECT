package com.Ease.Servlet.Team;

import com.Ease.Dashboard.User.User;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;

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
@WebServlet("/ServletGetTeam")
public class ServletGetTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            TeamUser currentTeamUser = sm.getUser().getTeamUser();
            if (currentTeamUser == null)
                throw new GeneralException(ServletManager.Code.ClientWarning, "Your are not in team");
            JSONObject res = currentTeamUser.getTeam().getJson();
            res.put("myTeamUserId", currentTeamUser.getDb_id());
            sm.setResponse(ServletManager.Code.Success, res.toString());
            sm.setLogResponse("GetTeam done");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }
}
