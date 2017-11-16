package com.Ease.Team;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by thomas on 28/04/2017.
 */
public class TeamManager {

    public TeamManager() {
    }

    public List<Team> getTeams(HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT t FROM Team t WHERE t.db_id = :id");
        return hibernateQuery.list();
    }

    public Team getTeam(Integer team_id, HibernateQuery hibernateQuery) throws HttpServletException {
        hibernateQuery.queryString("SELECT t FROM Team t WHERE t.db_id = :id");
        hibernateQuery.setParameter("id", team_id);
        Team team = (Team) hibernateQuery.getSingleResult();
        if (team == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This team does not exist.");
        return team;
    }

    public void updateTeamsSubscriptions() {
        /*for (Team team : this.getTeams())
            team.updateSubscription(); */
    }

    public void checkFreeTrialEnd(DataBaseConnection db) {
        /* for (Team team : this.getTeams())
            team.checkFreeTrialEnd(db); */
    }

    public void teamUserNotRegisteredReminder() throws HttpServletException {
        System.out.println("Team users not registered reminder start...");
        List<TeamUser> three_days_teamUsers = new LinkedList<>();
        List<TeamUser> eight_days_teamUsers = new LinkedList<>();
        List<TeamUser> twelve_days_teamUsers = new LinkedList<>();
        /* for (Team team : this.getTeams()) {
            for (TeamUser teamUser : team.getTeamUsers().values()) {
                if (teamUser.isRegistered())
                    continue;
                if (DateComparator.wasDaysAgo(teamUser.getArrivalDate(), 3))
                    three_days_teamUsers.add(teamUser);
                else if (DateComparator.wasDaysAgo(teamUser.getArrivalDate(), 8))
                    eight_days_teamUsers.add(teamUser);
                else if (DateComparator.wasDaysAgo(teamUser.getArrivalDate(), 12))
                    twelve_days_teamUsers.add(teamUser);
            }
        } */
        MailJetBuilder mailJetBuilder;
        HibernateQuery hibernateQuery = new HibernateQuery();
        if (!three_days_teamUsers.isEmpty()) {
            System.out.println("Three days reminder emails: " + three_days_teamUsers.size());
            for (TeamUser teamUser : three_days_teamUsers) {
                if (teamUser.getAdmin_id() == null)
                    continue;
                mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.setTemplateId(180262);
                mailJetBuilder.addTo(teamUser.getEmail());
                TeamUser admin = teamUser.getTeam().getTeamUserWithId(teamUser.getAdmin_id());
                mailJetBuilder.addVariable("first_name", admin.getFirstName());
                mailJetBuilder.addVariable("last_name", admin.getLastName());
                mailJetBuilder.addVariable("email", admin.getEmail());
                mailJetBuilder.addVariable("team_name", teamUser.getTeam().getName());
                String url = Variables.URL_PATH + "teams#/teamJoin/";
                hibernateQuery.querySQLString("SELECT code FROM pendingTeamInvitations WHERE teamUser_id = ?");
                hibernateQuery.setParameter(1, teamUser.getDb_id());
                url += (String) hibernateQuery.getSingleResult();
                mailJetBuilder.addVariable("url", url);
                mailJetBuilder.sendEmail();
            }
        }
        if (!eight_days_teamUsers.isEmpty()) {
            System.out.println("Eight days reminder emails: " + eight_days_teamUsers.size());
            for (TeamUser teamUser : eight_days_teamUsers) {
                if (teamUser.getAdmin_id() == null)
                    continue;
                mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.setTemplateId(238541);
                mailJetBuilder.addTo(teamUser.getEmail());
                TeamUser admin = teamUser.getTeam().getTeamUserWithId(teamUser.getAdmin_id());
                mailJetBuilder.addVariable("first_name", admin.getFirstName());
                mailJetBuilder.addVariable("last_name", admin.getLastName());
                mailJetBuilder.addVariable("email", admin.getEmail());
                mailJetBuilder.addVariable("team_name", teamUser.getTeam().getName());
                String url = Variables.URL_PATH + "teams#/teamJoin/";
                hibernateQuery.querySQLString("SELECT code FROM pendingTeamInvitations WHERE teamUser_id = ?");
                hibernateQuery.setParameter(1, teamUser.getDb_id());
                url += (String) hibernateQuery.getSingleResult();
                mailJetBuilder.addVariable("url", url);
                mailJetBuilder.sendEmail();
            }
        }
        if (!twelve_days_teamUsers.isEmpty()) {
            System.out.println("Twelve days reminder emails: " + twelve_days_teamUsers.size());
            for (TeamUser teamUser : twelve_days_teamUsers) {
                if (teamUser.getAdmin_id() == null)
                    continue;
                mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.setTemplateId(238542);
                mailJetBuilder.addTo(teamUser.getEmail());
                TeamUser admin = teamUser.getTeam().getTeamUserWithId(teamUser.getAdmin_id());
                mailJetBuilder.addVariable("first_name", admin.getFirstName());
                mailJetBuilder.addVariable("last_name", admin.getLastName());
                mailJetBuilder.addVariable("email", admin.getEmail());
                mailJetBuilder.addVariable("team_name", teamUser.getTeam().getName());
                String url = Variables.URL_PATH + "teams#/teamJoin/";
                hibernateQuery.querySQLString("SELECT code FROM pendingTeamInvitations WHERE teamUser_id = ?");
                hibernateQuery.setParameter(1, teamUser.getDb_id());
                url += (String) hibernateQuery.getSingleResult();
                mailJetBuilder.addVariable("url", url);
                mailJetBuilder.sendEmail();
            }
        }
        hibernateQuery.commit();
        System.out.println("Team user not registered reminder end...");
    }


    public void passwordReminder() throws HttpServletException {

        System.out.println("Password reminder start...");
        Date timestamp = new Date();
        HibernateQuery hibernateQuery = new HibernateQuery();
        /* for (Team team : this.getTeams()) {

        } */
        hibernateQuery.commit();
        System.out.println("Password reminder end...");
    }

    public void passwordLostReminder() throws HttpServletException {
        System.out.println("Password lost reminder start...");
        HibernateQuery hibernateQuery = new HibernateQuery();
        DataBaseConnection db = null;
        try {
            db = new DataBaseConnection(DataBase.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        try {
            int transaction = db.startTransaction();
            /* for (Team team : this.getTeams()) {
                for (TeamUser teamUser : team.getTeamUsers().values()) {
                    if (teamUser.isDisabled()) {
                        hibernateQuery.querySQLString("SELECT DATE_ADD(DATE(?), INTERVAL 7 DAY) = CURDATE();");
                        hibernateQuery.setParameter(1, teamUser.getDisabledDate());
                        if ((Boolean) hibernateQuery.getSingleResult()) {
                            if (teamUser.getAdmin_id() == null)
                                continue;
                            team.getTeamUserWithId(teamUser.getAdmin_id()).addNotification("Since last week " + teamUser.getUsername() + " lost the password to access your team " + team.getName() + " on Ease.space. Please give again the access to this person.", "@" + teamUser.getDb_id(), "", new Date(), db);
                        }
                    }

                }
            } */
            db.commitTransaction(transaction);
            hibernateQuery.commit();
        } catch (GeneralException e) {
            e.printStackTrace();
        }
        db.close();
        System.out.println("Password lost reminder end...");
    }

    public void checkDepartureDates(DataBaseConnection db) {
        /* for (Team team : this.getTeams()) {
            team.checkDepartureDates(new Date(), db);
        } */
    }
}
