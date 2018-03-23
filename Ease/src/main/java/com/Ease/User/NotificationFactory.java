package com.Ease.User;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.Update.Update;
import com.Ease.Utils.HttpServletException;
import com.Ease.websocketV1.WebSocketManager;
import com.Ease.websocketV1.WebSocketMessageFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationFactory {
    private static NotificationFactory ourInstance = new NotificationFactory();

    private static final String base_url = Variables.URL_PATH;

    public static NotificationFactory getInstance() {
        return ourInstance;
    }

    private NotificationFactory() {
    }

    private Notification createNotification(User user, String content, String icon, Channel channel) {
        String url = base_url + "#/teams/" + channel.getTeam().getDb_id() + "/" + channel.getDb_id();
        return new Notification(content, url, icon, user);
    }

    private PendingNotification createPendingNotification(TeamUser teamUser, String content, String icon, Channel channel) {
        String url = base_url + "#/teams/" + channel.getTeam().getDb_id() + "/" + channel.getDb_id();
        return new PendingNotification(teamUser, content, url, icon);
    }

    private Notification createNotification(User user, String content, String icon, TeamUser teamUser) {
        String url = base_url + "#/teams/" + teamUser.getTeam().getDb_id() + "/@" + teamUser.getDb_id();
        return new Notification(content, url, icon, user);
    }

    private Notification createNotification(User user, String content, String icon, TeamCard teamCard) {
        String url = base_url + "#/teams/" + teamCard.getChannel().getTeam().getDb_id() + "/" + teamCard.getChannel().getDb_id() + "?app_id=" + teamCard.getDb_id();
        return new Notification(content, url, icon, user);
    }

    public Notification createNotification(User user, String content, String icon, String url) {
        String final_url = base_url + url;
        return new Notification(content, final_url, icon, user);
    }

    private PendingNotification createPendingNotification(TeamUser teamUser, String content, String icon, String url) {
        String final_url = base_url + url;
        return new PendingNotification(teamUser, content, final_url, icon);
    }

    public Notification createNotification(User user, String content, String icon, TeamUser teamUser, boolean flexPanel) {
        if (!flexPanel)
            return createNotification(user, content, icon, teamUser);
        return createNotification(user, content, icon, "#/teams/" + teamUser.getTeam().getDb_id() + "/@" + teamUser.getDb_id() + "/flexPanel");
    }

    public void createAskOwnerForBillingNotification(Team team, TeamUser teamUser, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) throws HttpServletException {
        Notification notification = this.createNotification(team.getTeamUserOwner().getUser(), teamUser.getUsername() + " would like to access again your team " + team.getName(), "/resources/notifications/hand_shake.png", team.getDb_id() + "/" + team.getDefaultChannel().getDb_id().toString() + "/settings/payment");
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }


    public void createAskOwnerToUpgradeNotification(Team team, TeamUser teamUser, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) throws HttpServletException {
        Notification notification = this.createNotification(teamUser.getUser(), teamUser.getUsername() + " suggests to upgrade your Ease.space team!", "/resources/notifications/hand_shake.png", team.getDb_id() + "/" + team.getDefaultChannel().getDb_id().toString() + "/upgrade");
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createTeamUserRegisteredNotification(TeamUser teamUser, TeamUser teamUser_admin, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(teamUser_admin.getUser(), teamUser.getUsername() + " is now part of your team " + teamUser.getTeam().getName() + "!", "/resources/notifications/flag.png", teamUser);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createPasswordLostNotification(User user, TeamUser teamUser, Team team, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(user, teamUser.getUsername() + " lost the password to access your team " + team.getName() + " on Ease.space. Please give again the access to this person.", "/resources/notifications/user_password_lost.png", teamUser, true);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createJoinTeamCardNotification(TeamUser teamUser, TeamCard teamCard, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(teamCard.getChannel().getRoom_manager().getUser(), teamUser.getUsername() + " would like to have access to " + teamCard.getName(), teamCard.getLogo(), teamCard);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createAppSentNotification(TeamUser teamUser_receiver, TeamUser teamUser_sender, TeamCardReceiver teamCardReceiver, Map<Integer, Map<String, Object>> userIdMap, HibernateQuery hibernateQuery) {
        User user = teamUser_receiver.getUser();
        String content = teamUser_sender.getUsername() + " sent you " + teamCardReceiver.getApp().getAppInformation().getName();
        String url = "#/main/dashboard?app_id=" + teamCardReceiver.getApp().getDb_id();
        String logo = teamCardReceiver.getApp().getLogo();
        if (user != null && user.getUserStatus().isRegistered()) {
            Notification notification = this.createNotification(user, content, logo, url);
            hibernateQuery.saveOrUpdateObject(notification);
            this.getUserWebSocketManager(userIdMap, user).sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
        } else {
            PendingNotification pendingNotification = this.createPendingNotification(teamUser_receiver, content, logo, url);
            hibernateQuery.saveOrUpdateObject(pendingNotification);
        }
    }

    public void createRemindTeamCardFiller(TeamCardReceiver teamCardReceiver, TeamUser teamUser, Map<Integer, Map<String, Object>> userIdMap, HibernateQuery hibernateQuery) {
        TeamUser filler = teamCardReceiver.getTeamUser();
        User user = filler.getUser();
        String content = teamUser.getUsername() + " reminds you to enter " + teamCardReceiver.getTeamCard().getName() + "'s information";
        String url = "#/main/dashboard?app_id=" + teamCardReceiver.getApp().getDb_id();
        String logo = teamCardReceiver.getApp().getLogo();
        if (user != null && user.getUserStatus().isRegistered()) {
            Notification notification = this.createNotification(user, content, logo, url);
            hibernateQuery.saveOrUpdateObject(notification);
            this.getUserWebSocketManager(userIdMap, user).sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
        } else {
            PendingNotification pendingNotification = this.createPendingNotification(filler, content, logo, url);
            hibernateQuery.saveOrUpdateObject(pendingNotification);
        }
    }

    public void createRemindTeamCardFiller(TeamCard teamCard, TeamUser teamUser, TeamUser room_manager, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        User user = room_manager.getUser();
        String content = teamUser.getUsername() + " reminds you to enter " + teamCard.getName() + "'s information";
        String url = "#/teams/" + teamCard.getChannel().getDb_id() + "?app_id=" + teamCard.getDb_id();
        String logo = teamCard.getLogo();
        Notification notification = this.createNotification(user, content, logo, url);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createRemindTeamEnterpriseCardFiller(TeamCard teamCard, TeamUser teamUser, TeamUser room_manager, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        String content = room_manager.getUsername() + " reminds you to enter " + teamCard.getName() + "'s information";
        String url = "#/teams/" + teamCard.getChannel().getDb_id() + "?app_id=" + teamCard.getDb_id();
        String logo = teamCard.getLogo();
        if (!teamUser.isRegistered())
            return;
        User user = teamUser.getUser();
        Notification notification = this.createNotification(user, content, logo, url);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createEditRoleNotification(TeamUser teamUserToModify, TeamUser teamUser, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(teamUserToModify.getUser(), teamUser.getUsername() + " changed your role to " + teamUserToModify.getTeamUserRole().getRoleName(), "/resources/notifications/user_role_changed.png", teamUserToModify, true);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createTransferOwnershipNotification(TeamUser new_teamUser_owner, TeamUser teamUser, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(new_teamUser_owner.getUser(), teamUser.getUsername() + " changed your role to Owner", "/resources/notifications/user_role_changed.png", new_teamUser_owner, true);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createAcceptJoinRequestNotification(TeamUser teamUser_receiver, TeamUser teamUser, TeamCard teamCard, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(teamUser_receiver.getUser(), teamUser.getUsername() + " approved your access to " + teamCard.getName() + " in #" + teamCard.getChannel().getName(), teamCard.getLogo(), teamCard);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createAddTeamUserToChannelNotification(TeamUser teamUser, TeamUser teamUser_connected, Channel channel, Map<Integer, Map<String, Object>> userIdMap, HibernateQuery hibernateQuery) {
        User user = teamUser.getUser();
        String content = teamUser_connected.getUsername() + " added you in #" + channel.getName();
        String icon = "/resources/notifications/channel.png";
        if (user != null && user.getUserStatus().isRegistered()) {
            Notification notification = this.createNotification(user, content, icon, channel);
            hibernateQuery.saveOrUpdateObject(notification);
            this.getUserWebSocketManager(userIdMap, user).sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
        } else {
            PendingNotification pendingNotification = this.createPendingNotification(teamUser, content, icon, channel);
            hibernateQuery.saveOrUpdateObject(pendingNotification);
        }
    }

    public void createEditRoomNameNotification(TeamUser teamUser, Channel channel, String old_name, Map<Integer, Map<String, Object>> userIdMap, HibernateQuery hibernateQuery) {
        User user = teamUser.getUser();
        String content = "#" + old_name + " has been renamed to #" + channel.getName();
        String icon = "/resources/notifications/room_renamed.png";
        if (user != null && user.getUserStatus().isRegistered()) {
            Notification notification = this.createNotification(user, content, icon, channel);
            hibernateQuery.saveOrUpdateObject(notification);
            this.getUserWebSocketManager(userIdMap, user).sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
        } else {
            PendingNotification pendingNotification = this.createPendingNotification(teamUser, content, icon, channel);
            hibernateQuery.saveOrUpdateObject(pendingNotification);
        }
    }

    public void createAskJoinChannelNotification(TeamUser teamUser, Channel channel, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(channel.getRoom_manager().getUser(), teamUser.getUsername() + " would like to join #" + channel.getName(), "/resources/notifications/channel.png", channel);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createPasswordNotUpToDateNotification(TeamCard teamCard, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Channel channel = teamCard.getChannel();
        Notification notification = this.createNotification(channel.getRoom_manager().getUser(), "Password for " + teamCard.getName() + " needs to be updated as soon as possible", teamCard.getLogo(), teamCard);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createPasswordNotUpToDateNotification(TeamCardReceiver teamCardReceiver, Map<Integer, Map<String, Object>> userIdMap, HibernateQuery hibernateQuery) {
        TeamUser teamUser = teamCardReceiver.getTeamUser();
        User user = teamUser.getUser();
        String content = "Your password " + teamCardReceiver.getApp().getAppInformation().getName() + " needs to be updated as soon as possible";
        String url = "#/main/dashboard?app_id=" + teamCardReceiver.getApp().getDb_id();
        String icon = teamCardReceiver.getApp().getLogo();
        if (user != null && user.getUserStatus().isRegistered()) {
            Notification notification = this.createNotification(user, content, icon, url);
            hibernateQuery.saveOrUpdateObject(notification);
            this.getUserWebSocketManager(userIdMap, user).sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
        } else {
            PendingNotification pendingNotification = this.createPendingNotification(teamUser, content, icon, url);
            hibernateQuery.saveOrUpdateObject(pendingNotification);
        }
    }


    public void createPasswordNotUpToDateNotificationOneWeek(TeamCardReceiver teamCardReceiver, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        TeamCard teamCard = teamCardReceiver.getTeamCard();
        Team team = teamCardReceiver.getTeamCard().getTeam();
        Channel channel = teamCard.getChannel();
        String url = "#/teams/" + team.getDb_id() + "/" + channel.getDb_id() + "?app_id=" + teamCard.getDb_id();
        Notification notification = this.createNotification(channel.getRoom_manager().getUser(), "The password of " + teamCardReceiver.getTeamUser().getUsername() + " for " + teamCard.getName() + " is not up to date for the last 7 days", teamCard.getLogo(), url);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createDepartureDateThreeDaysNotification(TeamUser teamUser, TeamUser teamUser_admin, String formattedDate, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(teamUser_admin.getUser(), "the departure of " + teamUser.getUsername() + " is planned on next " + formattedDate + ".", "/resources/notifications/user_departure.png", teamUser, true);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createPasswordLostOneWeekNotification(TeamUser teamUser, TeamUser teamUser_admin, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(teamUser_admin.getUser(), "Since last week " + teamUser.getUsername() + " lost the password to access your team " + teamUser.getTeam().getName() + " on Ease.space. Please give again the access to this person.", "/resources/notifications/user_password_lost.png", teamUser);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createRemovedFromTeamCardNotification(TeamUser teamUser, TeamUser teamUser_admin, String app_name, String logo, Channel channel, Map<Integer, Map<String, Object>> userIdMap, HibernateQuery hibernateQuery) {
        User user = teamUser.getUser();
        String content = teamUser_admin.getUsername() + " removed your access to " + app_name + " (in #" + channel.getName() + ").";
        if (user != null && user.getUserStatus().isRegistered()) {
            Notification notification = this.createNotification(user, content, logo, channel);
            hibernateQuery.saveOrUpdateObject(notification);
            this.getUserWebSocketManager(userIdMap, user).sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
        } else {
            PendingNotification pendingNotification = this.createPendingNotification(teamUser, content, logo, channel);
            hibernateQuery.saveOrUpdateObject(pendingNotification);
        }
    }

    private WebSocketManager getUserWebSocketManager(Map<Integer, Map<String, Object>> userIdMap, User user) {
        Map<String, Object> userProperties = userIdMap.get(user.getDb_id());
        if (userProperties == null) {
            userProperties = new ConcurrentHashMap<>();
            userIdMap.put(user.getDb_id(), userProperties);
        }
        WebSocketManager webSocketManager = (WebSocketManager) userProperties.get("webSocketManager");
        if (webSocketManager == null) {
            webSocketManager = new WebSocketManager();
            userProperties.put("webSocketManager", webSocketManager);
        }
        return webSocketManager;
    }

    public void createMustFillAppNotification(TeamUser teamUser, TeamUser teamUser1, TeamCardReceiver teamCardReceiver, Map<Integer, Map<String, Object>> userIdMap, HibernateQuery hibernateQuery) {
        User user = teamUser.getUser();
        String content = teamUser1.getUsername() + " asks you to enter " + teamCardReceiver.getTeamCard().getName() + "'s information";
        String url = "#/main/dashboard?app_id=" + teamCardReceiver.getApp().getDb_id();
        String logo = teamCardReceiver.getTeamCard().getLogo();
        if (user != null && user.getUserStatus().isRegistered()) {
            Notification notification = this.createNotification(user, content, logo, url);
            hibernateQuery.saveOrUpdateObject(notification);
            this.getUserWebSocketManager(userIdMap, user).sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
        } else {
            PendingNotification pendingNotification = this.createPendingNotification(teamUser, content, logo, url);
            hibernateQuery.saveOrUpdateObject(pendingNotification);
        }
    }

    public void createRemindAdminPasswordLost(TeamUser teamUser, TeamUser teamUser_admin, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        User user = teamUser_admin.getUser();
        String content = "Reminder: " + teamUser.getUsername() + " asks you to be re-accepted in " + teamUser.getTeam().getName();
        Notification notification = this.createNotification(user, content, "/resources/notifications/user_password_lost.png", teamUser, true);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createAppFilledNotification(TeamUser filler, TeamCard teamCard, Map<Integer, Map<String, Object>> userIdMap, HibernateQuery hibernateQuery) {
        String content = teamCard.getName() + " is now ready";
        String url = "#/main/dashboard?app_id=";
        String logo = teamCard.getLogo();
        teamCard.getTeamCardReceiverMap().values().forEach(teamCardReceiver -> {
            TeamUser teamUser = teamCardReceiver.getTeamUser();
            if (!teamUser.equals(filler)) {
                User user = teamUser.getUser();
                if (user != null && user.getUserStatus().isRegistered()) {
                    Notification notification = this.createNotification(user, content, logo, url + teamCardReceiver.getApp().getDb_id());
                    hibernateQuery.saveOrUpdateObject(notification);
                    this.getUserWebSocketManager(userIdMap, user).sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
                } else {
                    PendingNotification pendingNotification = this.createPendingNotification(teamUser, content, logo, url + teamCardReceiver.getApp().getDb_id());
                    hibernateQuery.saveOrUpdateObject(pendingNotification);
                }
            }
        });
    }

    public void createUpdateTeamCardNotification(TeamUser teamUser, TeamCard teamCard, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        String content = teamUser.getUsername() + " suggests you a new password for " + teamCard.getName();
        String url = "#/main/catalog/website";
        Notification notification = this.createNotification(teamCard.getChannel().getRoom_manager().getUser(), content, teamCard.getLogo(), url);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createNewUpdateNotification(Update update, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        StringBuilder content = new StringBuilder();
        String url = "#/main/catalog/website";
        String logo;
        String contentAppName;
        if (update.getWebsite() != null) {
            logo = update.getWebsite().getLogo();
            if (update.getApp() == null) {
                content.append("New Account");
                contentAppName = update.getWebsite().getName();
            } else {
                if (!update.getApp().isEmpty())
                    content.append("New Password");
                else
                    content.append("Account Update");
                contentAppName = update.getApp().getAppInformation().getName();
            }
        } else {
            try {
                URL logoUrl = new URL(update.getUrl());
                logo = "https://placehold.it/175x175/373b60/FFFFFF/&text=" + logoUrl.getHost().substring(0, 1);
                content.append("New Account");
                contentAppName = logoUrl.getHost();
            } catch (MalformedURLException e) {
                logo = "E";
                contentAppName = "an app";
            }
        }
        content.append(" detected for ").append(contentAppName).append(". Manage now.");
        Notification notification = this.createNotification(update.getUser(), content.toString(), logo, url);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }
}
