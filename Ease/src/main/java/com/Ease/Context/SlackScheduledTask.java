package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Utils.Slack.SlackAPIWrapper;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

public class SlackScheduledTask extends TimerTask {
    @Override
    public void run() {
        if (!Variables.ENVIRONNEMENT.equals("Prod"))
            return;
        System.out.println("Start Slack scheduled task...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            this.notificationForTeamsConnectedToDay(hibernateQuery);
            hibernateQuery.commit();
        } catch (Exception e) {
            e.printStackTrace();
            hibernateQuery.rollback();
        }
        System.out.println("End Slack scheduled task...");
    }

    private List<Team> getTeamsConnectedToday(HibernateQuery hibernateQuery) throws Exception {
        Calendar calendar = Calendar.getInstance();
        hibernateQuery.queryString("SELECT c.user_id FROM ConnectionMetric c WHERE c.year = :year AND c.day_of_year = :day_of_year");
        hibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
        hibernateQuery.setParameter("day_of_year", calendar.get(Calendar.DAY_OF_YEAR));
        List<Integer> userIds = hibernateQuery.list();
        hibernateQuery.queryString("SELECT DISTINCT t.team FROM TeamUser t WHERE t.user IS NOT NULL AND t.user.db_id IN (:userIds)");
        hibernateQuery.setParameter("userIds", userIds);
        return hibernateQuery.list();
    }

    private void notificationForTeamsConnectedToDay(HibernateQuery hibernateQuery) throws Exception {
        StringBuilder stringBuilder = new StringBuilder("*Active companies today*\n");
        this.getTeamsConnectedToday(hibernateQuery).forEach(team -> stringBuilder.append("- ").append(team.getName()).append("\n"));
        SlackAPIWrapper.getInstance().postMessage("C9P9UL1MM", stringBuilder.toString());
    }
}
