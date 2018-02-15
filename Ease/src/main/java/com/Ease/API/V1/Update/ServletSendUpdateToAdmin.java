package com.Ease.API.V1.Update;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
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
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.json.JSONObject;

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
            if (teamCard == null || !teamCard.isTeamSingleCard())
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot send this update to an admin");
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            if (account_information.length() == 0)
                throw new HttpServletException(HttpStatus.BadRequest, "Empty account information");
            Team team = sm.getTeam(teamCard.getTeam().getDb_id());
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = sm.getTeamUser(team);
            update.decipher(sm.getUserPrivateKey());
            update.getApp().getAccount().edit(update.getAccountInformation(), hibernateQuery);
            TeamUser roomManager = teamCard.getChannel().getRoom_manager();
            Hibernate.initialize(teamCard);
            if (teamCard instanceof HibernateProxy)
                teamCard = (TeamCard) ((HibernateProxy) teamCard).getHibernateLazyInitializer().getImplementation();
            TeamSingleCard teamSingleCard = (TeamSingleCard) teamCard;
            hibernateQuery.queryString("SELECT u FROM Update u WHERE u.teamCard.db_id = :card_id AND u.user.db_id = :user_id");
            hibernateQuery.setParameter("user_id", roomManager.getUser().getDb_id());
            hibernateQuery.setParameter("card_id", teamCard.getDb_id());
            Update new_update = (Update) hibernateQuery.getSingleResult();
            if (new_update == null)
                new_update = UpdateFactory.getInstance().createUpdate(roomManager.getUser(), account_information, teamSingleCard, teamUser);
            else {
                new_update.setTeamUser(teamUser);
                new_update.edit(account_information, roomManager.getUser().getUserKeys().getPublicKey());
            }
            hibernateQuery.saveOrUpdateObject(new_update);
            hibernateQuery.deleteObject(update);
            NotificationFactory.getInstance().createUpdateTeamCardNotification(teamUser, teamCard, sm.getUserWebSocketManager(roomManager.getUser().getDb_id()), sm.getHibernateQuery());
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
            mailJetBuilder.addTo(roomManager.getEmail());
            mailJetBuilder.setTemplateId(315991);
            mailJetBuilder.addVariable("username", teamUser.getUsername());
            mailJetBuilder.addVariable("app_name", teamCard.getName());
            mailJetBuilder.addVariable("link", Variables.URL_PATH + "#/main/catalog/website");
            mailJetBuilder.sendEmail();
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
