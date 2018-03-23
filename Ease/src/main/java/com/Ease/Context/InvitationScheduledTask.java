package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InvitationScheduledTask extends TimerTask {

    private Map<Integer, Map<String, Object>> teamIdMap;

    public InvitationScheduledTask(Map<Integer, Map<String, Object>> teamIdMap) {
        this.teamIdMap = teamIdMap;
    }

    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.queryString("SELECT t FROM TeamUser t JOIN t.teamUserStatus AS s WHERE t.admin_id IS NOT NULL AND t.arrival_date IS NOT NULL AND t.arrival_date < :date AND s.invitation_sent is false");
            hibernateQuery.setTimestamp("date", new Date());
            List<TeamUser> teamUsers = hibernateQuery.list();
            Map<TeamUser, Set<TeamUser>> adminAndTeamUsersMap = new HashMap<>();
            for (TeamUser teamUser : teamUsers) {
                Team team = teamUser.getTeam();
                if (!team.isActive())
                    continue;
                Map<String, Object> teamProperties = teamIdMap.get(team.getDb_id());
                if (teamProperties == null) {
                    teamProperties = new ConcurrentHashMap<>();
                    teamIdMap.put(team.getDb_id(), teamProperties);
                }
                team.initializeStripe(teamProperties);
                if (team.getTeamUsers().values().stream().filter(teamUser1 -> teamUser1.getTeamUserStatus().isInvitation_sent()).count() >= team.getExtraMembersCount() && !team.isValidFreemium())
                    continue;
                TeamUser admin = teamUser.getAdmin();
                Set<TeamUser> teamUserSet = adminAndTeamUsersMap.get(admin);
                if (teamUserSet == null)
                    teamUserSet = new HashSet<>();
                teamUserSet.add(teamUser);
                adminAndTeamUsersMap.put(admin, teamUserSet);
            }
            for (Map.Entry<TeamUser, Set<TeamUser>> entry : adminAndTeamUsersMap.entrySet()) {
                TeamUser admin = entry.getKey();
                MailJetBuilder mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(311811);
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.addTo(admin.getEmail());
                mailJetBuilder.addVariable("team_name", admin.getTeam().getName());
                StringBuilder usernames = new StringBuilder();
                entry.getValue().forEach(teamUser -> usernames.append(teamUser.getUsername()).append(", "));
                usernames.delete(usernames.length() - 2, usernames.length());
                mailJetBuilder.addVariable("usernames", usernames.toString());
                mailJetBuilder.addVariable("link", Variables.URL_PATH);
                mailJetBuilder.sendEmail();
            }
            hibernateQuery.commit();
        } catch (HttpServletException e) {
            e.printStackTrace();
            hibernateQuery.rollback();
        }
    }
}
