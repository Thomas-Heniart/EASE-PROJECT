package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.Utils.HttpServletException;

public class InitializeTeamPasswords implements Runnable {

    private Team team;
    private String teamKey;

    public InitializeTeamPasswords(Team team, String teamKey) {
        this.team = team;
        this.teamKey = teamKey;
    }

    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        if (this.team.isPasswordScoreInitialize())
            return;
        this.team.setPasswordScoreInitialize(true);
        hibernateQuery.saveOrUpdateObject(team);
        for (TeamCard teamCard : team.getTeamCardSet()) {
            try {
                teamCard.decipher(teamKey);
                if (teamCard.isTeamSingleCard()) {
                    teamCard.calculatePasswordScore();
                    hibernateQuery.saveOrUpdateObject(teamCard);
                } else if (teamCard.isTeamEnterpriseCard()) {
                    for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values()) {
                        ((TeamEnterpriseCardReceiver) teamCardReceiver).calculatePasswordScore();
                        hibernateQuery.saveOrUpdateObject(teamCardReceiver);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            hibernateQuery.commit();
        } catch (HttpServletException e) {
            hibernateQuery.rollback();
            e.printStackTrace();
        }
    }
}
