package com.Ease.API.V1.Admin.onboarding;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Team.TeamUserRole;
import com.Ease.User.User;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.onboarding.OnboardingCustomerInformation;
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

@WebServlet("/api/v1/admin/onboarding/create-team")
public class ServletCreateTeam extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Long id = sm.getLongParam("id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            OnboardingCustomerInformation onboardingCustomerInformation = (OnboardingCustomerInformation) hibernateQuery.get(OnboardingCustomerInformation.class, id);
            if (onboardingCustomerInformation == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Customer not found");
            User user = sm.getUser();
            String teamKey = AES.keyGenerator();
            Team team = new Team(onboardingCustomerInformation.getTeamName(), onboardingCustomerInformation.getTeamSize());
            String keyUser = sm.getKeyUser();
            Date arrivalDate = new Date();
            TeamUser owner = TeamUser.createOwner(user.getEmail(), user.getUsername(), arrivalDate, AES.encrypt(teamKey, keyUser), team);
            owner.getTeamUserStatus().setInvitation_sent(true);
            owner.setUser(user);
            sm.saveOrUpdate(team);
            onboardingCustomerInformation.setCreated(true);
            onboardingCustomerInformation.setTeamId(team.getDb_id());
            sm.saveOrUpdate(onboardingCustomerInformation);
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
            customerParams.put("email", onboardingCustomerInformation.getEmail());
            team.setCustomer_id(Customer.create(customerParams).getId());
            Map<String, Object> item = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            switch (onboardingCustomerInformation.getPlanId()) {
                case 0:
                    item.put("plan", Team.plansMap.get(onboardingCustomerInformation.getPlanId()));
                    break;

                case 2:
                    item.put("plan", Team.plansMap.get(onboardingCustomerInformation.getPlanId()));
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
            /* Hack while front not done */
            team.getOnboardingStatus().setStep(5);
            /* ====== Stripe END ====== */

            sm.saveOrUpdate(team);
            user.addTeamUser(owner);
            channel.addTeamUser(owner);
            sm.saveOrUpdate(channel);

            /*========== True owner creation ===========*/
            String username = onboardingCustomerInformation.getEmail().substring(0, onboardingCustomerInformation.getEmail().indexOf("@"));
            if (username.length() > 22)
                username = username.substring(0, 21);
            while (username.length() < 3)
                username += "_";
            username = username.replaceAll("[\\W]", "_");
            if (team.hasTeamUserWithUsername(username)) {
                int suffixe = 1;
                while (team.hasTeamUserWithUsername(username + suffixe))
                    suffixe++;
                username += suffixe;
            }
            TeamUser teamUser = new TeamUser(onboardingCustomerInformation.getEmail(), username, null, null, team, new TeamUserRole(TeamUserRole.Role.MEMBER.getValue()));
            String code;
            do {
                code = CodeGenerator.generateNewCode();
                hibernateQuery.querySQLString("SELECT * FROM teamUsers WHERE invitation_code = ?");
                hibernateQuery.setParameter(1, code);
            } while (!hibernateQuery.list().isEmpty());
            teamUser.setInvitation_code(code);
            sm.saveOrUpdate(teamUser);
            team.addTeamUser(teamUser);
            team.getDefaultChannel().addTeamUser(teamUser);
            sm.saveOrUpdate(team.getDefaultChannel());
            /*========== True owner creation ===========*/
            sm.initializeTeamWithContext(team);
            /* hibernateQuery.commit();
            sm.setRedirectUrl("/#/teams/" + team.getDb_id()); */
            sm.setSuccess(team.getJson());
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
