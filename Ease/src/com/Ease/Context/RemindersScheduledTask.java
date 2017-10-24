package com.Ease.Context;

import com.Ease.Mail.ReminderEmailManager;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.DataBase;
import com.Ease.Utils.DataBaseConnection;

import java.sql.SQLException;
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
            teamManager.teamUserNotRegisteredReminder();
            //reminderEmailManager.lunchReminders();
            teamManager.passwordReminder();
            DataBaseConnection db;
            try {
                db = new DataBaseConnection(DataBase.getConnection());
                teamManager.checkDepartureDates(db);
                teamManager.checkFreeTrialEnd(db);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}