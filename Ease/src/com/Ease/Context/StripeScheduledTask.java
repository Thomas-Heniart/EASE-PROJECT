package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.HttpServletException;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by thomas on 20/06/2017.
 */
public class StripeScheduledTask extends TimerTask {

    Date now;
    TeamManager teamManager;

    public StripeScheduledTask(TeamManager teamManager) {
        super();
        this.teamManager = teamManager;
    }

    @Override
    public void run() {
        teamManager.updateTeamsSubscriptions();
    }
}