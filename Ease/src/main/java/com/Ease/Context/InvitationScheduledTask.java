package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
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
            hibernateQuery.queryString("SELECT t FROM TeamUser t JOIN t.teamUserStatus AS s WHERE t.arrival_date IS NOT NULL AND t.arrival_date < :date AND s.invitation_sent is false");
            hibernateQuery.setDate("date", new Date());
            List<TeamUser> teamUsers = hibernateQuery.list();
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
                if (team.getTeamUsers().values().stream().filter(teamUser1 -> teamUser1.getTeamUserStatus().isInvitation_sent()).count() >= (Team.MAX_MEMBERS + team.getInvitedFriendMap().size()) && !team.isValidFreemium())
                    continue;
                teamUser.getTeamUserStatus().setInvitation_sent(true);
                hibernateQuery.saveOrUpdateObject(teamUser.getTeamUserStatus());
                MailJetBuilder mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(179023);
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.addTo(teamUser.getEmail());
                TeamUser teamUser_admin = team.getTeamUserWithId(teamUser.getAdmin_id());
                mailJetBuilder.addVariable("team_name", team.getName());
                mailJetBuilder.addVariable("first_name", teamUser_admin.getUser().getPersonalInformation().getFirst_name());
                mailJetBuilder.addVariable("last_name", teamUser_admin.getUser().getPersonalInformation().getLast_name());
                mailJetBuilder.addVariable("email", teamUser_admin.getEmail());
                mailJetBuilder.addVariable("link", Variables.URL_PATH + "#/teamJoin/" + teamUser.getInvitation_code());
                mailJetBuilder.sendEmail();
            }
            hibernateQuery.commit();
        } catch (HttpServletException e) {
            e.printStackTrace();
            hibernateQuery.rollback();
        }
    }
}
