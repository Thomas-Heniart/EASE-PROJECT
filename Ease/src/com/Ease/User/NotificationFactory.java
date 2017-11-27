package com.Ease.User;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
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
}
