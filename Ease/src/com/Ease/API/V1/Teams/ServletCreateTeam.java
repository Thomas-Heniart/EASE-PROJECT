package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.User.User;
import com.Ease.Dashboard.User.UserEmail;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;

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
            String digits = sm.getStringParam("digits", false);
            String teamName = sm.getStringParam("team_name", true);
            String firstName = sm.getStringParam("first_name", true);
            String lastName = sm.getStringParam("last_name", true);
            String email = sm.getStringParam("email", true);
            String username = sm.getStringParam("username", true);
            String jobTitle = sm.getStringParam("job_title", true);
            if (teamName == null || teamName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "teamName is needed.");
            if (firstName == null || firstName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "firstName is needed.");
            if (lastName == null || lastName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "lastName is needed.");
            if (email == null || email.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "email is needed.");
            checkUsernameIntegrity(username);
            if (jobTitle == null)
                jobTitle = "";
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
            Team team = new Team(teamName);
            String teamKey_ciphered = user.encrypt(teamKey);
            Date arrivalDate = new Date(sm.getLongParam("timestamp", true));
            TeamUser owner = TeamUser.createOwner(firstName, lastName, email, username, arrivalDate, teamKey_ciphered, team);
            owner.setJobTitle(jobTitle);
            owner.setDeciphered_teamKey(teamKey);
            owner.setUser_id(user.getDBid());
            sm.saveOrUpdate(team);
            sm.saveOrUpdate(owner);
            Channel channel = team.createDefaultChannel(owner.getDb_id());
            sm.saveOrUpdate(channel);
            owner.setDashboard_user(user);
            user.addTeamUser(owner);
            team.addChannel(channel);
            team.addTeamUser(owner);

            /* ===== Stripe START ===== */

            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("email", email);
            team.setCustomer_id(Customer.create(customerParams).getId());
            Map<String, Object> item = new HashMap<>();
            item.put("plan", "EaseFreemium");
            Map<String, Object> items = new HashMap<>();
            items.put("0", item);
            Map<String, Object> params = new HashMap<>();
            params.put("customer", team.getCustomer_id());
            params.put("items", items);
            params.put("trial_period_days", 30);
            params.put("tax_percent", 20.0);
            team.setSubscription_id(Subscription.create(params).getId());
            team.setSubscription_date(new Date());

            /* ====== Stripe END ====== */

            sm.saveOrUpdate(team);
            sm.getHibernateQuery().commit();
            user.addTeamUser(owner);
            channel.addTeamUser(owner, sm.getDB());
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            teamManager.addTeam(team);
            team.lazyInitialize();
            UserEmail userEmail = user.getUserEmails().get(email);
            if (userEmail == null)
                user.getEmails().put(email, UserEmail.createUserEmail(email, user, true, sm.getDB()));
            else if (!userEmail.isVerified())
                userEmail.beVerified(sm.getDB());
            sm.setSuccess(team.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void checkUsernameIntegrity(String username) throws HttpServletException {
        if (username == null || username.equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "Usernames can't be empty!");
        if (username.length() < 4 || username.length() >= 22)
            throw new HttpServletException(HttpStatus.BadRequest, "Sorry, usernames must be greater than 3 characters and fewer than 22 characters.");
        /* if (username.length() >= 22)
            throw new HttpServletException(HttpStatus.BadRequest, "Sorry, that's a bit too long! Usernames must be fewer than 22 characters."); */
        if (!username.equals(username.toLowerCase()) || !Regex.isValidUsername(username))
            throw new HttpServletException(HttpStatus.BadRequest, "Sorry, usernames must contain only lowercase characters.");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
