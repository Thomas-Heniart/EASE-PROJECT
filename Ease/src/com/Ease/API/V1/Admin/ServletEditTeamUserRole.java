package com.Ease.API.V1.Admin;

import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Team.TeamUserRole;
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

@WebServlet("/ServletEditTeamUserRole")
public class ServletEditTeamUserRole extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUserToModify = (TeamUser) sm.getHibernateQuery().get(TeamUser.class, teamUser_id);
            if (teamUserToModify == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This user does not exist");
            Team team = teamUserToModify.getTeam();
            Integer roleValue = sm.getIntParam("role", true, false);
            if (roleValue == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Empty role.");
            if (TeamUserRole.Role.getRoleNameByValue(roleValue) == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This role does not exist");
            if (roleValue.equals(TeamUserRole.Role.OWNER.getValue())) {
                TeamUser owner = team.getTeamUserOwner();
                owner.getTeamUserRole().setRoleValue(teamUserToModify.getTeamUserRole().getRoleValue());
                sm.saveOrUpdate(owner.getTeamUserRole());
            }
            teamUserToModify.getTeamUserRole().setRoleValue(roleValue);
            sm.saveOrUpdate(teamUserToModify.getTeamUserRole());
            sm.setSuccess("TeamUser role edited.");
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
