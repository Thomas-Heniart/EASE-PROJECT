package com.Ease.Team;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.App;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.DateUtils;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.websocketV1.WebSocketManager;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 28/04/2017.
 */
public class TeamManager {

    public TeamManager() {
    }

    public static void main (String[] args) {
        final Collection<? extends Number> foo = new ArrayList<Number>();
        foo.add(null);
    }

    public List<Team> getAllTeams(HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT t FROM Team t");
        return hibernateQuery.list();
    }

    public List<Team> getTeams(HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT t FROM Team t WHERE t.active = true");
        return hibernateQuery.list();
    }

    public Team getTeam(Integer team_id, HibernateQuery hibernateQuery) throws HttpServletException {
        Team team = (Team) hibernateQuery.get(Team.class, team_id);
        if (team == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This team does not exist.");
        return team;
    }

    public void updateTeamsSubscriptions(HibernateQuery hibernateQuery, Map<Integer, Map<String, Object>> teamIdMap) {
        for (Team team : this.getTeams(hibernateQuery)) {
            Map<String, Object> teamProperties = teamIdMap.get(team.getDb_id());
            if (teamProperties == null) {
                teamProperties = new ConcurrentHashMap<>();
                teamIdMap.put(team.getDb_id(), teamProperties);
            }
            team.updateSubscription(teamProperties);
        }

    }

    public void checkFreeTrialEnd(HibernateQuery hibernateQuery, Map<Integer, Map<String, Object>> teamIdMap) {
        for (Team team : this.getTeams(hibernateQuery)) {
            Map<String, Object> teamProperties = teamIdMap.get(team.getDb_id());
            if (teamProperties == null) {
                teamProperties = new ConcurrentHashMap<>();
                teamIdMap.put(team.getDb_id(), teamProperties);
            }
            team.initializeStripe(teamProperties);
            team.checkFreeTrialEnd();
        }
    }

    public void teamUserNotRegisteredReminder(HibernateQuery hibernateQuery) throws HttpServletException {
        System.out.println("Team users not registered reminder start...");
        List<TeamUser> three_days_teamUsers = new LinkedList<>();
        List<TeamUser> eight_days_teamUsers = new LinkedList<>();
        List<TeamUser> twelve_days_teamUsers = new LinkedList<>();
        for (Team team : this.getTeams(hibernateQuery)) {
            for (TeamUser teamUser : team.getTeamUsers().values()) {
                if (teamUser.isRegistered() || !teamUser.getTeamUserStatus().isInvitation_sent())
                    continue;
                if (DateUtils.wasDaysAgo(teamUser.getCreation_date(), 3))
                    three_days_teamUsers.add(teamUser);
                else if (DateUtils.wasDaysAgo(teamUser.getCreation_date(), 8))
                    eight_days_teamUsers.add(teamUser);
                else if (DateUtils.wasDaysAgo(teamUser.getCreation_date(), 12))
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
                mailJetBuilder.addVariable("first_name", admin.getUser().getPersonalInformation().getFirst_name());
                mailJetBuilder.addVariable("last_name", admin.getUser().getPersonalInformation().getLast_name());
                mailJetBuilder.addVariable("email", admin.getEmail());
                mailJetBuilder.addVariable("team_name", teamUser.getTeam().getName());
                String url = Variables.URL_PATH + "#/teamJoin/";
                hibernateQuery.querySQLString("SELECT invitation_code FROM teamUsers WHERE id = ?");
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
                mailJetBuilder.addVariable("first_name", admin.getUser().getPersonalInformation().getFirst_name());
                mailJetBuilder.addVariable("last_name", admin.getUser().getPersonalInformation().getLast_name());
                mailJetBuilder.addVariable("email", admin.getEmail());
                mailJetBuilder.addVariable("team_name", teamUser.getTeam().getName());
                String url = Variables.URL_PATH + "#/teamJoin/";
                hibernateQuery.querySQLString("SELECT invitation_code FROM teamUsers WHERE id = ?");
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
                mailJetBuilder.addVariable("first_name", admin.getUser().getPersonalInformation().getFirst_name());
                mailJetBuilder.addVariable("last_name", admin.getUser().getPersonalInformation().getLast_name());
                mailJetBuilder.addVariable("email", admin.getEmail());
                mailJetBuilder.addVariable("team_name", teamUser.getTeam().getName());
                String url = Variables.URL_PATH + "#/teamJoin/";
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
            for (TeamCard teamCard : team.getTeamCardSet()) {
                if (teamCard.isTeamSingleCard()) {
                    Account account = teamCard.getAccount();
                    if (account == null)
                        continue;
                    if (account.mustUpdatePassword() && !account.isPassword_must_be_updated()) {
                        account.setPassword_must_be_updated(true);
                        hibernateQuery.saveOrUpdateObject(account);
                        NotificationFactory.getInstance().createPasswordNotUpToDateNotification(teamCard, this.getUserWebSocketManager(teamCard.getChannel().getRoom_manager().getUser().getDb_id(), servletContext), hibernateQuery);
                    }
                } else if (teamCard.isTeamEnterpriseCard()) {
                    for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values()) {
                        if (teamCardReceiver.getTeamUser().isRegistered())
                            continue;
                        App app = teamCardReceiver.getApp();
                        if (app == null || app.getAccount() == null)
                            continue;
                        Account account = app.getAccount();
                        if (account.mustUpdatePassword() && !account.isPassword_must_be_updated()) {
                            account.setPassword_must_be_updated(true);
                            hibernateQuery.saveOrUpdateObject(account);
                            NotificationFactory.getInstance().createPasswordNotUpToDateNotification(teamCardReceiver, this.getUserIdMap(servletContext), hibernateQuery);
                        } else if (teamCardReceiver.getTeamUser().isVerified() && account.mustUpdatePassword() && !account.isAdmin_notified() && DateUtils.isOutdated(account.getLast_update(), account.getReminder_interval(), 7)) {
                            account.setAdmin_notified(true);
                            hibernateQuery.saveOrUpdateObject(account);
                            Channel channel = teamCard.getChannel();
                            NotificationFactory.getInstance().createPasswordNotUpToDateNotificationOneWeek(teamCardReceiver, this.getUserWebSocketManager(channel.getRoom_manager().getUser().getDb_id(), servletContext), hibernateQuery);
                            MailJetBuilder mailJetBuilder = new MailJetBuilder();
                            mailJetBuilder.setTemplateId(286064);
                            mailJetBuilder.setFrom("contact@ease.space", "Ease.Space");
                            mailJetBuilder.addVariable("app_name", teamCard.getName());
                            mailJetBuilder.addVariable("username", teamCardReceiver.getTeamUser().getEmail());
                            mailJetBuilder.addTo(channel.getRoom_manager().getEmail());
                            mailJetBuilder.sendEmail();
                        }
                    }
                }
            }
        }
        System.out.println("Password reminder end...");
    }

    private Map<Integer, Map<String, Object>> getUserIdMap(ServletContext servletContext) {
        return (Map<Integer, Map<String, Object>>) servletContext.getAttribute("userIdMap");
    }

    private WebSocketManager getUserWebSocketManager(Integer user_id, ServletContext servletContext) {
        Map<Integer, Map<String, Object>> userIdMap = this.getUserIdMap(servletContext);
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

    public void passwordLostReminder(HibernateQuery hibernateQuery, ServletContext servletContext) throws HttpServletException {
        System.out.println("Password lost reminder start...");
        for (Team team : this.getTeams(hibernateQuery)) {
            for (TeamUser teamUser : team.getTeamUsers().values()) {
                if (teamUser.isDisabled() && teamUser.getDisabledDate() != null) {
                    Calendar calendar = Calendar.getInstance();
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(teamUser.getDisabledDate());
                    calendar1.add(Calendar.DAY_OF_YEAR, 7);
                    if (calendar.get(Calendar.DAY_OF_YEAR) == calendar1.get(Calendar.DAY_OF_YEAR) && calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR)) {
                        if (teamUser.getAdmin_id() == null)
                            continue;
                        TeamUser teamUser_admin = team.getTeamUserWithId(teamUser.getAdmin_id());
                        NotificationFactory.getInstance().createPasswordLostOneWeekNotification(teamUser, teamUser_admin, this.getUserWebSocketManager(teamUser_admin.getUser().getDb_id(), servletContext), hibernateQuery);
                    }
                }
            }
        }
        System.out.println("Password lost reminder end...");
    }

    public void checkDepartureDates(HibernateQuery hibernateQuery, ServletContext servletContext) throws HttpServletException {
        for (Team team : this.getTeams(hibernateQuery)) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd");
            for (TeamUser teamUser : team.getTeamUsers().values()) {
                if (teamUser.getDepartureDate() == null)
                    continue;
                if (DateUtils.isInDays(teamUser.getDepartureDate(), 3)) {
                    calendar.setTime(teamUser.getDepartureDate());
                    String suffixe = "th";
                    switch (calendar.get(Calendar.DAY_OF_MONTH)) {
                        case 1: {
                            suffixe = "st";
                            break;
                        }
                        case 2: {
                            suffixe = "nd";
                            break;
                        }
                        case 3: {
                            suffixe = "rd";
                            break;
                        }
                    }
                    String formattedDate = simpleDateFormat.format(teamUser.getDepartureDate()) + suffixe;
                    if (teamUser.getUser() == null || !teamUser.getUser().getUserStatus().isRegistered())
                        continue;
                    TeamUser teamUser_admin = team.getTeamUserWithId(teamUser.getAdmin_id());
                    NotificationFactory.getInstance().createDepartureDateThreeDaysNotification(teamUser, teamUser_admin, formattedDate, this.getUserWebSocketManager(teamUser_admin.getUser().getDb_id(), servletContext), hibernateQuery);
                }
            }
        }
    }
}
