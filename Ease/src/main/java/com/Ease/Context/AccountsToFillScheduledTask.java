package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailjetMessageWrapper;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class AccountsToFillScheduledTask extends TimerTask {
    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.queryString("SELECT tcr FROM TeamCardReceiver tcr WHERE r.sharing_date >= :date");
            hibernateQuery.setDate("date", calendar.getTime());
            List<TeamCardReceiver> teamCardReceivers = hibernateQuery.list();
            Map<TeamUser, Set<TeamCardReceiver>> teamUserSetMap = new HashMap<>();
            for (TeamCardReceiver teamCardReceiver : teamCardReceivers) {
                TeamUser teamUser = teamCardReceiver.getTeamUser();
                Set<TeamCardReceiver> teamCardReceiverSet = teamUserSetMap.get(teamUser);
                if (teamCardReceiverSet == null)
                    teamCardReceiverSet = new HashSet<>();
                teamCardReceiverSet.add(teamCardReceiver);
                teamUserSetMap.put(teamUser, teamCardReceiverSet);
            }
            for (Map.Entry<TeamUser, Set<TeamCardReceiver>> entry : teamUserSetMap.entrySet()) {
                TeamUser teamUser = entry.getKey();
                Set<TeamCardReceiver> teamCardReceiverSet = entry.getValue();
                JSONArray appArr = new JSONArray();
                teamCardReceiverSet.forEach(teamCardReceiver -> {
                    JSONObject appObj = new JSONObject();
                    appObj.put("name", teamCardReceiver.getTeamCard().getName());
                    appArr.put(appObj);
                });
                MailjetMessageWrapper.newAccountsMail(teamUser, appArr, appArr.length());
            }
            hibernateQuery.commit();
        } catch (Exception e) {
            hibernateQuery.rollback();
            e.printStackTrace();
        }
    }
}
