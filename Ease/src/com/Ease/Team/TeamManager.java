package com.Ease.Team;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.User.Notification;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.*;
import com.Ease.websocketV1.WebSocketManager;
import com.Ease.websocketV1.WebSocketMessageFactory;

import javax.servlet.ServletContext;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 28/04/2017.
 */
public class TeamManager {

    public TeamManager() {
    }

    public List<Team> getTeams(HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT t FROM Team t");
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

    public void updateTeamsSubscriptions(HibernateQuery hibernateQuery) {
        for (Team team : this.getTeams(hibernateQuery))
            team.updateSubscription();
    }

    public void checkFreeTrialEnd(DataBaseConnection db) {
        /* for (Team team : this.getTeams())
            team.checkFreeTrialEnd(db); */
    }

    public void teamUserNotRegisteredReminder(HibernateQuery hibernateQuery) throws HttpServletException {
        System.out.println("Team users not registered reminder start...");
        List<TeamUser> three_days_teamUsers = new LinkedList<>();
        List<TeamUser> eight_days_teamUsers = new LinkedList<>();
        List<TeamUser> twelve_days_teamUsers = new LinkedList<>();
        for (Team team : this.getTeams(hibernateQuery)) {
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
        }
        MailJetBuilder mailJetBuilder;
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
        System.out.println("Team user not registered reminder end...");
    }


    public void passwordReminder(HibernateQuery hibernateQuery, ServletContext servletContext) throws HttpServletException {

        System.out.println("Password reminder start...");
        for (Team team : this.getTeams(hibernateQuery)) {
            for (TeamCard teamCard : team.getTeamCardMap().values()) {
                if (teamCard.isTeamSingleCard()) {
                    TeamSingleCard teamSingleCard = (TeamSingleCard) teamCard;
                    Account account = teamSingleCard.getAccount();
                    if (account == null)
                        continue;
                    if (account.mustUpdatePassword() && !account.isPassword_must_be_updated()) {
                        account.setPassword_must_be_updated(true);
                        hibernateQuery.saveOrUpdateObject(account);
                        Channel channel = teamCard.getChannel();
                        String url = team.getDb_id() + "/" + channel.getDb_id() + "?app_id=" + teamCard.getDb_id();
                        Notification notification = NotificationFactory.getInstance().createNotification(channel.getRoom_manager().getUser(), "Password for " + teamSingleCard.getName() + " needs to be updated as soon as possible", teamSingleCard.getLogo(), url);
                        hibernateQuery.saveOrUpdateObject(notification);
                        WebSocketManager webSocketManager = this.getUserWebSocketManager(channel.getRoom_manager().getUser().getDb_id(), servletContext);
                        webSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
                    }
                } else if (teamCard.isTeamEnterpriseCard()) {
                    for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values()) {
                        if (teamCardReceiver.getTeamUser().getUser() == null)
                            continue;
                        TeamEnterpriseCardReceiver teamEnterpriseCardReceiver = (TeamEnterpriseCardReceiver) teamCardReceiver;
                        ClassicApp classicApp = (ClassicApp) teamEnterpriseCardReceiver.getApp();
                        if (classicApp == null || classicApp.getAccount() == null)
                            continue;
                        Account account = classicApp.getAccount();
                        if (account.mustUpdatePassword() && !account.isPassword_must_be_updated()) {
                            account.setPassword_must_be_updated(true);
                            hibernateQuery.saveOrUpdateObject(account);
                            Channel channel = teamCard.getChannel();
                            String url = team.getDb_id() + "/" + channel.getDb_id() + "?app_id=" + teamCard.getDb_id();
                            Notification notification = NotificationFactory.getInstance().createNotification(teamCardReceiver.getTeamUser().getUser(), "Your password " + classicApp.getAppInformation().getName() + " needs to be updated as soon as possible", teamCard.getLogo(), url);
                            hibernateQuery.saveOrUpdateObject(notification);
                            WebSocketManager webSocketManager = this.getUserWebSocketManager(teamCardReceiver.getTeamUser().getUser().getDb_id(), servletContext);
                            webSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
                        } else if (account.mustUpdatePassword() && !account.isAdmin_notified() && DateComparator.isOutdated(account.getLast_update(), account.getReminder_interval(), 7)) {
                            account.setAdmin_notified(true);
                            hibernateQuery.saveOrUpdateObject(account);
                            Channel channel = teamCard.getChannel();
                            String url = team.getDb_id() + "/" + channel.getDb_id() + "?app_id=" + teamCard.getDb_id();
                            Notification notification = NotificationFactory.getInstance().createNotification(channel.getRoom_manager().getUser(), "The password of " + teamCardReceiver.getTeamUser().getUsername() + " for " + teamCard.getName() + " is not up to date for the last 7 days", teamCard.getLogo(), url);
                            hibernateQuery.saveOrUpdateObject(notification);
                            WebSocketManager webSocketManager = this.getUserWebSocketManager(channel.getRoom_manager().getUser().getDb_id(), servletContext);
                            webSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
                        }
                    }
                }
            }
        }
        System.out.println("Password reminder end...");
    }

    private WebSocketManager getUserWebSocketManager(Integer user_id, ServletContext servletContext) {
        Map<Integer, Map<String, Object>> userIdMap = (Map<Integer, Map<String, Object>>) servletContext.getAttribute("userIdMap");
        Map<String, Object> userProperties = userIdMap.get(user_id);
        if (userProperties == null) {
            userProperties = new ConcurrentHashMap<>();
            userIdMap.put(user_id, userProperties);
        }
        WebSocketManager webSocketManager = (WebSocketManager) userProperties.get("webSocketManager");
        if (webSocketManager == null) {
            webSocketManager = new WebSocketManager();
            userProperties.put("webSocketManager", webSocketManager);
        }
        return webSocketManager;
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
