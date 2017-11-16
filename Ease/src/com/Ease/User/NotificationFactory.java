package com.Ease.User;

import com.Ease.Context.Variables;
import com.Ease.Team.Channel;
import com.Ease.Team.TeamUser;

public class NotificationFactory {
    private static NotificationFactory ourInstance = new NotificationFactory();

    private static final String base_url = Variables.URL_PATH;

    public static NotificationFactory getInstance() {
        return ourInstance;
    }

    private NotificationFactory() {
    }

    public Notification createNotification(User user, String content, String icon, Channel channel) {
        String url = base_url + "teams#/teams/" + channel.getTeam().getDb_id() + "/" + channel.getDb_id();
        return new Notification(content, url, icon, user);
    }

    public Notification createNotification(User user, String content, String icon, TeamUser teamUser) {
        String url = base_url + "teams#/teams/" + teamUser.getTeam().getDb_id() + "/@" + teamUser.getDb_id();
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
}
