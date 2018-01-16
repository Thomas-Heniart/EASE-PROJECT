package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamManager;

import java.util.Map;
import java.util.TimerTask;

/**
 * Created by thomas on 20/06/2017.
 */
public class StripeScheduledTask extends TimerTask {

    private final Map<Integer, Map<String, Object>> teamIdMap;
    private TeamManager teamManager;

    StripeScheduledTask(TeamManager teamManager, Map<Integer, Map<String, Object>> teamIdMap) {
        super();
        this.teamManager = teamManager;
        this.teamIdMap = teamIdMap;
    }

    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            teamManager.updateTeamsSubscriptions(hibernateQuery, teamIdMap);
            hibernateQuery.commit();
        } catch (Exception e) {
            hibernateQuery.rollback();
            e.printStackTrace();
        }
    }
}