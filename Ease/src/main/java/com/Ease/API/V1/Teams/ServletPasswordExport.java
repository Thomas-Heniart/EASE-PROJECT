package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/export-passwords")
public class ServletPasswordExport extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamId = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(teamId);
            sm.needToBeOwnerOfTeam(team);
            StringBuilder stringBuilder = new StringBuilder("Name,URL,Login,Password\n");
            String teamKey = sm.getTeamKey(team);
            for (TeamCard teamCard : team.getTeamCardSet()) {
                if (teamCard.getAccount() == null)
                    continue;
                teamCard.decipher(teamKey);
                stringBuilder.append(teamCard.passwordExportCsvString());
                stringBuilder.append("\n");
            }
            sm.getHibernateQuery().commit();
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"ease_team_passwords.csv\"");
            response.getOutputStream().write(stringBuilder.toString().getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            sm.setError(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
