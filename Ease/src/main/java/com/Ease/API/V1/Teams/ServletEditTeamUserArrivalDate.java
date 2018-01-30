package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/api/v1/teams/EditTeamUserArrivalDate")
public class ServletEditTeamUserArrivalDate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            if (!team.isValidFreemium())
                throw new HttpServletException(HttpStatus.Forbidden, "You must upgrade to Pro to access this feature");
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (teamUser.getTeamUserRole().isSuperiorOrEquals(teamUser_connected.getTeamUserRole().getRoleValue()))
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot edit departure date of this member");
            Long arrival_date = sm.getLongParam("arrival_date", true, true);
            if (arrival_date < new Date().getTime())
                throw new HttpServletException(HttpStatus.BadRequest, "Arrival date cannot be past");
            if (teamUser.getDepartureDate() != null && arrival_date > teamUser.getDepartureDate().getTime())
                throw new HttpServletException(HttpStatus.BadRequest, "Arrival date cannot be after departure date");
            teamUser.setArrival_date(new Date(arrival_date));
            sm.saveOrUpdate(teamUser);
            sm.setSuccess(teamUser.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
