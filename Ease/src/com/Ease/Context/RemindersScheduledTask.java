package com.Ease.Context;

import com.Ease.Team.TeamManager;
import com.Ease.Utils.HttpServletException;

import java.util.Date;
import java.util.TimerTask;

/**
 * Created by thomas on 20/06/2017.
 */
public class RemindersScheduledTask extends TimerTask {

    Date now;
    TeamManager teamManager;

    public RemindersScheduledTask(TeamManager teamManager) {
        super();
        this.teamManager = teamManager;
    }

    @Override
    public void run() {
        try {
            teamManager.reminderThreeDays();
        } catch (HttpServletException e) {
            e.printStackTrace();
        }
    }
}