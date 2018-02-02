package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailjetMessageWrapper;
import com.Ease.NewDashboard.App;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
            hibernateQuery.queryString("SELECT tcr FROM TeamCardReceiver tcr WHERE tcr.sharing_date >= :date");
            hibernateQuery.setDate("date", calendar.getTime());
            List<App> apps = hibernateQuery.list();
            Map<TeamUser, Set<App>> teamUserSetMap = new HashMap<>();
            for (App app : apps) {
                TeamUser teamUser = app.getTeamCardReceiver().getTeamUser();
                if (teamUser.getArrival_date() != null && teamUser.getArrival_date().getTime() > now)
                    continue;
                Set<App> appSet = teamUserSetMap.get(teamUser);
                if (appSet == null)
                    appSet = new HashSet<>();
                appSet.add(app);
                teamUserSetMap.put(teamUser, appSet);
            }
            for (Map.Entry<TeamUser, Set<App>> entry : teamUserSetMap.entrySet()) {
                TeamUser teamUser = entry.getKey();
                Team team = teamUser.getTeam();
                Map<String, Object> teamProperties = teamIdMap.get(team.getDb_id());
                if (teamProperties == null) {
                    teamProperties = new ConcurrentHashMap<>();
                    teamIdMap.put(team.getDb_id(), teamProperties);
                }
                team.initializeStripe(teamProperties);
                if (team.getTeamUsers().values().stream().filter(teamUser1 -> teamUser1.getTeamUserStatus().isInvitation_sent()).count() >= (15 + team.getInvitedFriendMap().size()) && !team.isValidFreemium())
                    continue;
                Set<App> appSet = entry.getValue();
                JSONArray appArr = new JSONArray();
                appSet.forEach(app -> {
                    JSONObject appObj = new JSONObject();
                    appObj.put("name", app.getAppInformation().getName());
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
