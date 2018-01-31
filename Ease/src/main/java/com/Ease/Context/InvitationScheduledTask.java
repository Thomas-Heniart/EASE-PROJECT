package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class InvitationScheduledTask extends TimerTask {

    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.queryString("SELECT t FROM TeamUser t JOIN t.teamUserStatus AS s WHERE t.arrival_date > :date AND t.invitation_sent is false");
            hibernateQuery.setDate("date", new Date());
            List<TeamUser> teamUsers = hibernateQuery.list();
            for (TeamUser teamUser : teamUsers) {
                Team team = teamUser.getTeam();
                teamUser.getTeamUserStatus().setInvitation_sent(true);
                hibernateQuery.saveOrUpdateObject(teamUser.getTeamUserStatus());
                /* Mail part */
            }
            hibernateQuery.commit();
        } catch (HttpServletException e) {
            e.printStackTrace();
            hibernateQuery.rollback();
        }
    }

    public void stripeInitialization(Team team) {
        try {
            if (team.getCustomer_id() != null) {
                Customer customer = Customer.retrieve(team.getCustomer_id());
                team.setCustomer(customer);
            }
            if (team.getSubscription_id() != null) {
                Subscription subscription = Subscription.retrieve(team.getSubscription_id());
                team.setSubscription(subscription);
            }
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}
