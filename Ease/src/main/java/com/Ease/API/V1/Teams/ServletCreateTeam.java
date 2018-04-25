package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailjetContactWrapper;
import com.Ease.Mail.NewTeamThread;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.User.UserEmail;
import com.Ease.User.UserPostRegistrationEmails;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas on 12/04/2017.
 */
@WebServlet("/api/v1/teams/CreateTeam")
public class ServletCreateTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String digits = sm.getStringParam("digits", false, true);
            String teamName = sm.getStringParam("team_name", true, false);
            String email = sm.getStringParam("email", true, false);
            email = email.toLowerCase();
            String username = sm.getStringParam("username", true, false);
            username = username.toLowerCase();
            Integer plan_id = sm.getIntParam("plan_id", true, false);
            if (plan_id == 1)
                plan_id = 2;
            Integer company_size = sm.getIntParam("company_size", true, false);
            if (teamName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "teamName is needed.");
            if (company_size < 1)
                throw new HttpServletException(HttpStatus.BadRequest, "Company size must be greater than 0");
            if (email.equals("") || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "email is needed.");
            checkUsernameIntegrity(username);
            if (!user.getVerifiedEmails().contains(email) && (digits == null || digits.equals("") || digits.length() != 6))
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot create a team.");
            if (user.getUnverifiedEmails().contains(email)) {
                HibernateQuery query = sm.getHibernateQuery();
                query.querySQLString("SELECT id FROM pendingTeamCreations WHERE email = ? AND digits = ?");
                query.setParameter(1, email);
                query.setParameter(2, digits);
                Object id = query.getSingleResult();
                if (id == null)
                    throw new HttpServletException(HttpStatus.Forbidden, "You cannot create a team.");
                query.querySQLString("DELETE FROM pendingTeamCreations WHERE id = ?");
                query.setParameter(1, id);
                query.executeUpdate();
            }
            String teamKey = AES.keyGenerator();
            Team team = new Team(teamName, company_size);
            String keyUser = sm.getKeyUser();
            Date arrivalDate = new Date();
            TeamUser owner = TeamUser.createOwner(email, username, arrivalDate, AES.encrypt(teamKey, keyUser), team);
            owner.getTeamUserStatus().setInvitation_sent(true);
            owner.setUser(user);
            sm.saveOrUpdate(team);
            sm.getTeamProperties(team.getDb_id()).put("teamKey", teamKey);
            owner.getTeamUserStatus().setInvitation_sent(true);
            sm.saveOrUpdate(owner);
            Channel channel = team.createDefaultChannel(owner);
            sm.saveOrUpdate(channel);
            user.addTeamUser(owner);
            team.addChannel(channel);
            team.addTeamUser(owner);

            /* ===== Stripe START ===== */
            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("email", email);
            team.setCustomer_id(Customer.create(customerParams).getId());
            Map<String, Object> item = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            switch (plan_id) {
                case 0:
                    item.put("plan", Team.plansMap.get(plan_id));
                    break;

                case 2:
                    item.put("plan", Team.plansMap.get(plan_id));
                    params.put("trial_period_days", 30);
                    params.put("tax_percent", 20.0);
                    break;

                default:
                    throw new HttpServletException(HttpStatus.BadRequest, "This plan does not exist");
            }
            Map<String, Object> items = new HashMap<>();
            items.put("0", item);
            params.put("customer", team.getCustomer_id());
            params.put("items", items);
            team.setSubscription_id(Subscription.create(params).getId());
            team.setSubscription_date(new Date());

            /* ====== Stripe END ====== */

            sm.saveOrUpdate(team);
            user.addTeamUser(owner);
            channel.addTeamUser(owner);
            sm.saveOrUpdate(channel);
            UserEmail userEmail = user.getUserEmail(email);
            if (userEmail == null)
                userEmail = new UserEmail(email, true, user);
            else if (!userEmail.isVerified())
                userEmail.setVerified(true);
            sm.saveOrUpdate(userEmail);
            sm.initializeTeamWithContext(team);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM, WebSocketMessageAction.CREATED, team.getWebSockeetJson()));
            UserPostRegistrationEmails userPostRegistrationEmails = user.getUserPostRegistrationEmails();
            if (!userPostRegistrationEmails.isEmail_team_creation_sent()) {
                /* MailJetBuilder mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(287210);
                mailJetBuilder.setFrom("benjamin@ease.space", "Benjamin Prigent");
                mailJetBuilder.addTo(user.getEmail());
                mailJetBuilder.activateTemplateLanguage();
                mailJetBuilder.sendEmail(); */
                userPostRegistrationEmails.setEmail_team_creation_sent(true);
                sm.saveOrUpdate(userPostRegistrationEmails);
            }
            MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
            mailjetContactWrapper.updateUserContactLists(user);
            JSONObject tmp = team.getJson();
            tmp.put("my_team_user_id", owner.getDb_id());
            new NewTeamThread(team).start();
            sm.setSuccess(tmp);
        } catch (StripeException e) {
            sm.setError(e);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void checkUsernameIntegrity(String username) throws HttpServletException {
        if (username == null || username.equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "Usernames can't be empty!");
        if (username.length() < 3 || username.length() >= 22)
            throw new HttpServletException(HttpStatus.BadRequest, "Sorry, usernames must be greater than 2 characters and fewer than 22 characters.");
        if (!username.equals(username.toLowerCase()) || !Regex.isValidUsername(username))
            throw new HttpServletException(HttpStatus.BadRequest, "Sorry, usernames must contain only lowercase characters.");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
