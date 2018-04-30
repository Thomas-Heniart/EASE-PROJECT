package com.Ease.API.V1.Common;

import com.Ease.NewDashboard.App;
import com.Ease.Team.Team;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/export-passwords")
public class ServletPasswordExport extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            StringBuilder stringBuilder = new StringBuilder("Name,URL,Login,Password\n");
            String keyUser = sm.getKeyUser();
            for (App app : sm.getUser().getApps()) {
                if (app.getAccount() == null)
                    continue;
                Team team = app.getTeamCardReceiver() != null ? app.getTeamCardReceiver().getTeamCard().getTeam() : null;
                if (team != null)
                    sm.needToBeTeamUserOfTeam(team);
                String teamKey = team != null ? sm.getTeamKey(team) : null;
                app.decipher(keyUser, teamKey);
                stringBuilder.append(app.passwordExportCsvString());
                stringBuilder.append("\n");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"ease_passwords.csv\"");
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
