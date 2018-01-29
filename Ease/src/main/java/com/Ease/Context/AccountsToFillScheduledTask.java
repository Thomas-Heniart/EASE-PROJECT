package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailjetMessageWrapper;
import com.Ease.NewDashboard.App;
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
            hibernateQuery.queryString("SELECT a FROM App a INNER JOIN a.teamCardReceiver as r WHERE a.insert_date >= :date");
            hibernateQuery.setDate("date", calendar.getTime());
            List<App> apps = hibernateQuery.list();
            Map<TeamUser, Set<App>> teamUserSetMap = new HashMap<>();
            for (App app : apps) {
                TeamUser teamUser = app.getTeamCardReceiver().getTeamUser();
                Set<App> appSet = teamUserSetMap.get(teamUser);
                if (appSet == null)
                    appSet = new HashSet<>();
                appSet.add(app);
                teamUserSetMap.put(teamUser, appSet);
            }
            for (Map.Entry<TeamUser, Set<App>> entry : teamUserSetMap.entrySet()) {
                TeamUser teamUser = entry.getKey();
                Set<App> appSet = entry.getValue();
                JSONArray appArr = new JSONArray();
                appSet.forEach(app -> {
                    JSONObject appObj = new JSONObject();
                    appObj.put("name", app.getAppInformation().getName());
                    appArr.put(appObj);
                });
                /* MailjetMessageWrapper mailjetMessageWrapper = new MailjetMessageWrapper();
                mailjetMessageWrapper.setTemplateId(301785);
                mailjetMessageWrapper.setFrom("contact@ease.space", "Ease.Space");
                mailjetMessageWrapper.addTo(teamUser.getEmail());
                mailjetMessageWrapper.setVariable("apps", appArr);
                mailjetMessageWrapper.setVariable("number_of_apps", appArr.length());
                mailjetMessageWrapper.setVariable("link", teamUser.isVerified() ? Variables.URL_PATH : (Variables.URL_PATH + "#/teamJoin/" + teamUser.getInvitation_code()));
                mailjetMessageWrapper.setVariable("link_name", teamUser.isVerified() ? "Check your new apps" : "Activate account & check new apps");
                mailjetMessageWrapper.setErrorReporting();
                mailjetMessageWrapper.send(); */
                MailjetMessageWrapper.test();
            }
            hibernateQuery.commit();
        } catch (Exception e) {
            hibernateQuery.rollback();
            e.printStackTrace();
        }
    }
}
