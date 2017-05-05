package com.Ease.Servlet.Team;

import com.Ease.Dashboard.User.User;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Crypto.Hashing;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by thomas on 03/05/2017.
 */
@WebServlet("/ServletSetUserForTeamUser")
public class ServletSetUserForTeamUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            String user_email = sm.getServletParam("email", true);
            String password = sm.getServletParam("password", false);
            String team_id = sm.getServletParam("team_id", true);
            String teamUser_id = sm.getServletParam("teamUser_id", true);
            DataBaseConnection db = sm.getDB();
            DatabaseRequest databaseRequest = db.prepareRequest("SELECT users.id, userKeys.password FROM users JOIN userKeys ON users.key_id = userKeys.id WHERE email = ?;");
            databaseRequest.setString(user_email);
            DatabaseResult rs = databaseRequest.get();
            if (!rs.next())
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong email or password");
            String user_id = rs.getString(1);
            String hashed_password = rs.getString(2);
            Map<String, User> connectedUsers = (Map<String, User>) sm.getContextAttr("users");
            User connected_user = connectedUsers.get(user_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser = team.getTeamUserWithId(Integer.parseInt(teamUser_id));
            if (connected_user == null) {
                if (!Hashing.compare(password, hashed_password))
                    throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong email or password");
                databaseRequest = db.prepareRequest("UPDATE teamUsers SET user_id = ? WHERE id = ?;");
                databaseRequest.setInt(user_id);
                databaseRequest.setInt(teamUser.getDb_id());
                databaseRequest.set();
            } else {
                if (!connected_user.getKeys().isGoodPassword(password))
                    throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong email or password");
                connected_user.addTeamUser(teamUser, sm);
            }
            sm.setResponse(ServletManager.Code.Success, "TeamUser setup");
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
