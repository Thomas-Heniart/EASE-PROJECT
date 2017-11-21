package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.ReminderEmailManager;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.DataBase;
import com.Ease.Utils.DataBaseConnection;

import javax.servlet.ServletContext;
import java.sql.SQLException;
import java.util.TimerTask;

/**
 * Created by thomas on 20/06/2017.
 */
public class RemindersScheduledTask extends TimerTask {

    private TeamManager teamManager;
    private ReminderEmailManager reminderEmailManager = new ReminderEmailManager();
    private ServletContext servletContext;

    RemindersScheduledTask(TeamManager teamManager, ServletContext servletContext) {
        super();
        this.teamManager = teamManager;
        this.servletContext = servletContext;
    }

    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            teamManager.teamUserNotRegisteredReminder(hibernateQuery);
            //reminderEmailManager.lunchReminders();
            teamManager.passwordReminder(hibernateQuery, servletContext);
            DataBaseConnection db;
            try {
                db = new DataBaseConnection(DataBase.getConnection());
                teamManager.checkDepartureDates(db);
                teamManager.checkFreeTrialEnd(db);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            hibernateQuery.commit();
        } catch (Exception e) {
            hibernateQuery.rollback();
            e.printStackTrace();
        }
    }
}