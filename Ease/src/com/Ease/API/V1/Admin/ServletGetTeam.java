package com.Ease.API.V1.Admin;

import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamWebsiteCard;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/GetTeam")
public class ServletGetTeam extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            JSONObject res = new JSONObject();
            res.put("people_invited", team.getTeamUsers().size());
            res.put("people_joine", team.getTeamUsers().values().stream().filter(TeamUser::isVerified).count());
            res.put("people_active", team.getActiveTeamUserNumber());
            res.put("people_click_on_app_three_days", team.getNumberOfPeopleWhoClickOnApps(3, sm.getHibernateQuery()));
            res.put("card_number", team.getTeamCardMap().size());
            res.put("card_with_receiver_number", team.getTeamCardMap().values().stream().filter(teamCard -> !teamCard.getTeamCardReceiverMap().isEmpty()).count());
            res.put("card_with_receiver_and_password_reminder_number", team.getTeamCardMap().values().stream().filter(teamCard -> !teamCard.getTeamCardReceiverMap().isEmpty() && (teamCard.isTeamWebsiteCard() && ((TeamWebsiteCard)teamCard).getPassword_reminder_interval() > 0)).count());
            res.put("single_card_number", team.getTeamCardMap().values().stream().filter(TeamCard::isTeamSingleCard).count());
            res.put("enterprise_card_number", team.getTeamCardMap().values().stream().filter(TeamCard::isTeamEnterpriseCard).count());
            res.put("link_card_number", team.getTeamCardMap().values().stream().filter(TeamCard::isTeamLinkCard).count());
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
