package com.Ease.Context;

import com.Ease.Mail.ReminderEmailManager;
import com.Ease.Team.TeamManager;

import java.util.TimerTask;

/**
 * Created by thomas on 20/06/2017.
 */
public class RemindersScheduledTask extends TimerTask {

    private TeamManager teamManager;
    private ReminderEmailManager reminderEmailManager = new ReminderEmailManager();

    RemindersScheduledTask(TeamManager teamManager) {
        super();
        this.teamManager = teamManager;
    }

    @Override
    public void run() {
        try {
            /* teamManager.reminderThreeDays();
            reminderEmailManager.lunchReminders(); */
            teamManager.passwordReminder();
            teamManager.checkFreeTrialEnd();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}