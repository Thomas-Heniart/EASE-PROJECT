package com.Ease.API.V1.Update;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamUser;
import com.Ease.Update.Update;
import com.Ease.Update.UpdateFactory;
import com.Ease.User.NotificationFactory;
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

@WebServlet("/api/v1/updates/SendUpdateToAdmin")
public class ServletSendUpdateToAdmin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Long updateId = sm.getLongParam("update_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Update update = (Update) hibernateQuery.get(Update.class, updateId);
            if (update == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This update does not exist");
            TeamCard teamCard = update.getTeamCard();
            if (teamCard == null || !teamCard.isTeamSingleCard() || !teamCard.isTeamWebsiteCard())
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot send this update to an admin");
            Team team = sm.getTeam(teamCard.getTeam().getDb_id());
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = sm.getTeamUser(team);
            update.decipher(sm.getUserPrivateKey());
            update.getApp().getAccount().edit(update.getAccountInformation(), hibernateQuery);
            TeamUser roomManager = teamCard.getChannel().getRoom_manager();
            Update new_update = UpdateFactory.getInstance().createUpdate(roomManager.getUser(), update.getAccountInformation(), (TeamSingleCard) teamCard, teamUser);
            hibernateQuery.saveOrUpdateObject(new_update);
            hibernateQuery.deleteObject(update);
            NotificationFactory.getInstance().createUpdateTeamCardNotification(teamUser, teamCard, sm.getUserWebSocketManager(roomManager.getUser().getDb_id()), sm.getHibernateQuery());
            /* MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
            mailJetBuilder.addTo(roomManager.getEmail());
            mailJetBuilder.setTemplateId(0);

            mailJetBuilder.sendEmail(); */
            sm.setSuccess(new_update.getJson());
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
