package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamManager;

import java.util.TimerTask;

/**
 * Created by thomas on 20/06/2017.
 */
public class StripeScheduledTask extends TimerTask {

    private TeamManager teamManager;

    StripeScheduledTask(TeamManager teamManager) {
        super();
        this.teamManager = teamManager;
    }

    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            teamManager.updateTeamsSubscriptions(hibernateQuery);
            hibernateQuery.commit();
        } catch (Exception e) {
            hibernateQuery.rollback();
            e.printStackTrace();
        }
    }
}