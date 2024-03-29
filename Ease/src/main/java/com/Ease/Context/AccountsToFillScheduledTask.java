package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailjetMessageWrapper;
import com.Ease.Metrics.ConnectionMetric;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class AccountsToFillScheduledTask extends TimerTask {

    private Map<Integer, Map<String, Object>> teamIdMap;

    public AccountsToFillScheduledTask(Map<Integer, Map<String, Object>> teamIdMap) {
        this.teamIdMap = teamIdMap;
    }

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        HibernateQuery hibernateQuery = new HibernateQuery();
        Long now = new Date().getTime();
        try {
            hibernateQuery.queryString("SELECT tcr FROM TeamCardReceiver tcr INNER JOIN tcr.teamUser.user AS u WHERE tcr.sharing_date >= :date AND u.userStatus.registered IS TRUE");
            hibernateQuery.setTimestamp("date", calendar.getTime());
            List<TeamCardReceiver> teamCardReceivers = hibernateQuery.list();
            hibernateQuery.queryString("SELECT c FROM ConnectionMetric c ");
            Map<TeamUser, Set<TeamCardReceiver>> teamUserSetMap = new HashMap<>();
            for (TeamCardReceiver teamCardReceiver : teamCardReceivers) {
                TeamUser teamUser = teamCardReceiver.getTeamUser();
                Set<TeamCardReceiver> teamCardReceiverSet = teamUserSetMap.get(teamUser);
                if (teamCardReceiverSet == null)
                    teamCardReceiverSet = new HashSet<>();
                teamCardReceiverSet.add(teamCardReceiver);
                teamUserSetMap.put(teamUser, teamCardReceiverSet);
            }
            Set<TeamUser> teamUserSet = teamUserSetMap.keySet();
            for (TeamUser teamUser : teamUserSet) {
                Team team = teamUser.getTeam();
                if (!team.isActive())
                    teamUserSetMap.remove(teamUser);
                ConnectionMetric metric = ConnectionMetric.getMetricOrNull(teamUser.getUser().getDb_id(), calendar.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_YEAR), hibernateQuery);
                if (metric != null && metric.isConnected())
                    teamUserSetMap.remove(teamUser);
            }
            for (Map.Entry<TeamUser, Set<TeamCardReceiver>> entry : teamUserSetMap.entrySet()) {
                TeamUser teamUser = entry.getKey();
                Team team = teamUser.getTeam();
                if (!team.isActive())
                    continue;
                Map<String, Object> teamProperties = teamIdMap.computeIfAbsent(team.getDb_id(), k -> new HashMap<>());
                if ((teamUser.getArrival_date() != null && teamUser.getArrival_date().getTime() > now) || (team.getTeamUsers().values().stream().filter(teamUser1 -> teamUser1.getTeamUserStatus().isInvitation_sent()).count() >= team.getExtraMembersCount() && !team.isValidFreemium()))
                    continue;
                team.initializeStripe(teamProperties);
                Set<TeamCardReceiver> teamCardReceiverSet = entry.getValue();
                JSONArray appArr = new JSONArray();
                teamCardReceiverSet.forEach(teamCardReceiver -> {
                    JSONObject appObj = new JSONObject();
                    appObj.put("name", teamCardReceiver.getTeamCard().getName());
                    appArr.put(appObj);
                });
                if (!teamUser.getTeamUserStatus().isInvitation_sent()) {
                    teamUser.getTeamUserStatus().setInvitation_sent(true);
                    hibernateQuery.saveOrUpdateObject(teamUser.getTeamUserStatus());
                }
                MailjetMessageWrapper.newAccountsMail(teamUser, appArr, appArr.length());
            }
            hibernateQuery.commit();
        } catch (Exception e) {
            hibernateQuery.rollback();
            e.printStackTrace();
        }
    }
}
