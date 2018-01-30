package com.Ease.Context;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class InvitationScheduledTask extends TimerTask {
    @Override
    public void run() {
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            hibernateQuery.queryString("SELECT t FROM TeamUser t JOIN t.teamUserStatus AS s WHERE t.arrival_date > :date AND t.invitation_sent is false");
            hibernateQuery.setDate("date", new Date());
            List<TeamUser> teamUsers = hibernateQuery.list();
            for (TeamUser teamUser : teamUsers) {
                teamUser.getTeamUserStatus().setInvitation_sent(true);
                hibernateQuery.saveOrUpdateObject(teamUser.getTeamUserStatus());
                /* Mail part */
            }
            hibernateQuery.commit();
        } catch (HttpServletException e) {
            e.printStackTrace();
            hibernateQuery.rollback();
        }
    }
}
