package com.Ease.User;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.websocketV1.WebSocketManager;
import com.Ease.websocketV1.WebSocketMessageFactory;

public class NotificationFactory {
    private static NotificationFactory ourInstance = new NotificationFactory();

    private static final String base_url = Variables.URL_PATH;

    public static NotificationFactory getInstance() {
        return ourInstance;
    }

    private NotificationFactory() {
    }

    public Notification createNotification(User user, String content, String icon, Channel channel) {
        String url = base_url + "#/teams/" + channel.getTeam().getDb_id() + "/" + channel.getDb_id();
        return new Notification(content, url, icon, user);
    }

    public Notification createNotification(User user, String content, String icon, TeamUser teamUser) {
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

    public Notification createNotification(User user, String content, String icon, TeamUser teamUser, boolean flexPanel) {
        if (!flexPanel)
            return createNotification(user, content, icon, teamUser);
        return createNotification(user, content, icon, teamUser.getTeam().getDb_id() + "/@" + teamUser.getDb_id() + "/flexPanel");
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
        Notification notification = this.createNotification(teamUser_admin.getUser(), teamUser.getUsername() + "is now part of your team " + teamUser.getTeam().getName() + "!", "/resources/notifications/flag.png", teamUser);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createPasswordLostNotification(User user, TeamUser teamUser, Team team, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(user, teamUser.getUsername() + " lost the password to access your team " + team.getName() + " on Ease.space. Please give again the access to this person.", "/resources/notifications/user_role_changed.png", teamUser, true);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createJoinTeamCardNotification(TeamUser teamUser, TeamCard teamCard, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(teamCard.getChannel().getRoom_manager().getUser(), teamUser.getUsername() + " would like to have access to " + teamCard.getName(), teamCard.getLogo(), teamCard);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createAppSentNotification(User user, TeamUser teamUser, TeamCardReceiver teamCardReceiver, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = this.createNotification(user, teamUser.getUsername() + " sent you " + teamCardReceiver.getApp().getAppInformation().getName(), teamCardReceiver.getApp().getLogo(), "#/main/dashboard?app_id=" + teamCardReceiver.getApp().getDb_id());
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
        Notification notification = this.createNotification(teamUser_receiver.getUser(), teamUser.getUsername() + " approoved your access to " + teamCard.getName() + " in #" + teamCard.getChannel().getName(), teamCard.getLogo(), teamCard);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createAddTeamUserToChannelNotification(TeamUser teamUser, TeamUser teamUser_connected, Channel channel, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = NotificationFactory.getInstance().createNotification(teamUser.getUser(), teamUser_connected.getUsername() + " added you in #" + channel.getName(), "/resources/notifications/channel.png", channel);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createEditRoomNameNotification(TeamUser teamUser, Channel channel, String old_name, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = NotificationFactory.getInstance().createNotification(teamUser.getUser(), "#" + old_name + " has been renamed to #" + channel.getName(), "/resources/notifications/room_renamed.png", channel);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }

    public void createAskJoinChannelNotification(TeamUser teamUser, Channel channel, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = NotificationFactory.getInstance().createNotification(channel.getRoom_manager().getUser(), teamUser.getUsername() + " would like to join #" + channel.getName(), "/resources/notifications/channel.png", channel);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
    }
}
