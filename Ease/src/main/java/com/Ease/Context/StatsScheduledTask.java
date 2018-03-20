package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Metrics.ClickOnApp;
import com.Ease.Metrics.WeeklyStats;
import com.Ease.Utils.HttpServletException;
import org.json.JSONArray;

import java.util.*;

public class StatsScheduledTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("Start StatsScheduledTask");
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            System.out.println("End StatsScheduledTask");
            return;
        }
        Calendar last_week = Calendar.getInstance();
        last_week.add(Calendar.WEEK_OF_YEAR, -4);
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            JSONArray rows = new JSONArray();
            for (int i = 0; i < 3; i++) {
                rows.put(WeeklyStats.retrieveWeeklyStats(last_week.get(Calendar.YEAR), last_week.get(Calendar.WEEK_OF_YEAR), hibernateQuery).getJson());
                last_week.add(Calendar.WEEK_OF_YEAR, 1);
            }
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom("contact@ease.space", "Agathe The Power");
            mailJetBuilder.addTo("thomas@ease.space");
            mailJetBuilder.setTemplateId(330444);
            mailJetBuilder.addTo("benjamin@ease.space");
            mailJetBuilder.addTo("victor@ease.space");
            mailJetBuilder.addTo("sergii@ease.space");
            mailJetBuilder.addTo("victorien@ease.space");
            mailJetBuilder.addTo("clement@ease.space");
            Date this_week = calendar.getTime();
            Date last_week_date = last_week.getTime();
            WeeklyStats weeklyStats = this.generateWeeklyStats(hibernateQuery, last_week_date, this_week, last_week);
            rows.put(weeklyStats.getJson());
            mailJetBuilder.addVariable("rows", rows);
            mailJetBuilder.sendEmail();
            hibernateQuery.commit();
            System.out.println("End StatsScheduledTask");
        } catch (HttpServletException e) {
            e.printStackTrace();
            hibernateQuery.rollback();
        }
    }

    private WeeklyStats generateWeeklyStats(HibernateQuery hibernateQuery, Date last_week_date, Date this_week, Calendar last_week) {
        hibernateQuery.queryString("SELECT t FROM Team t WHERE t.active IS true AND t.subscription_date >= :date_start AND t.subscription_date < :date_end");
        hibernateQuery.setTimestamp("date_start", last_week_date);
        hibernateQuery.setTimestamp("date_end", this_week);
        int new_companies = hibernateQuery.list().size();
        hibernateQuery.queryString("SELECT u FROM User u WHERE u.registration_date >= :date_start AND u.registration_date < :date_end AND u.userStatus.registered IS true");
        hibernateQuery.setTimestamp("date_start", last_week_date);
        hibernateQuery.setTimestamp("date_end", this_week);
        int new_users = hibernateQuery.list().size();
        hibernateQuery.queryString("SELECT a FROM App a LEFT JOIN a.teamCardReceiver as r WHERE r IS NULL AND a.insert_date >= :date_start AND a.insert_date < :date_end");
        hibernateQuery.setTimestamp("date_start", last_week_date);
        hibernateQuery.setTimestamp("date_end", this_week);
        int new_apps = hibernateQuery.list().size();
        hibernateQuery.queryString("SELECT a FROM App a LEFT JOIN a.teamCardReceiver as r WHERE r IS NOT NULL AND a.insert_date >= :date_start AND a.insert_date < :date_end AND r.teamCard.team.active IS true");
        hibernateQuery.setTimestamp("date_start", last_week_date);
        hibernateQuery.setTimestamp("date_end", this_week);
        int new_team_apps = hibernateQuery.list().size();
        hibernateQuery.queryString("SELECT m FROM ClickOnApp m WHERE m.week_of_year = :week_of_year AND m.year = :year");
        hibernateQuery.setParameter("week_of_year", last_week.get(Calendar.WEEK_OF_YEAR));
        hibernateQuery.setParameter("year", last_week.get(Calendar.YEAR));
        List<ClickOnApp> clickOnApps = hibernateQuery.list();
        int passwords_killed = 0;
        Set<Integer> userIds = new HashSet<>();
        for (ClickOnApp clickOnApp : clickOnApps) {
            passwords_killed += clickOnApp.getTotalClicks();
            userIds.add(clickOnApp.getUser_id());
        }
        Integer teamCount = 0;
        if (!userIds.isEmpty()) {
            hibernateQuery.queryString("SELECT DISTINCT tu.team FROM TeamUser tu WHERE tu.user IS NOT NULL AND tu.user.db_id IN (:userIds)");
            hibernateQuery.setParameter("userIds", userIds);
            teamCount = hibernateQuery.list().size();
        }
        WeeklyStats weeklyStats = WeeklyStats.retrieveWeeklyStats(last_week.get(Calendar.YEAR), last_week.get(Calendar.WEEK_OF_YEAR), hibernateQuery);
        weeklyStats.updateValues(new_companies, new_users, new_apps, new_team_apps, passwords_killed, userIds.size(), teamCount);
        hibernateQuery.saveOrUpdateObject(weeklyStats);
        return weeklyStats;
    }
}
