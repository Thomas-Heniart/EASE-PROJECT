package com.Ease.User;

import com.Ease.Hibernate.HibernateQuery;
import org.json.simple.JSONArray;

import java.util.List;

public class NotificationManager {
    public NotificationManager() {

    }

    public void readAllNotifications(User user, HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("UPDATE n FROM Notification n SET n.newNotification = :bool WHERE n.user = :user AND n.newNotification = :bool1");
        hibernateQuery.setParameter("bool", false);
        hibernateQuery.setParameter("bool1", true);
        hibernateQuery.setParameter("user", user);
    }

    private List<Notification> getNotifications(int limit, User user, HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT n FROM Notification n WHERE n.user = :user ORDER BY n.creation_date DESC");
        hibernateQuery.setParameter("user", user);
        hibernateQuery.setMaxResults(limit);
        return hibernateQuery.list();
    }

    public JSONArray getJson(int limit, User user, HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        this.getNotifications(limit, user, hibernateQuery).forEach(notification -> res.add(notification.getJson()));
        return res;
    }
}
