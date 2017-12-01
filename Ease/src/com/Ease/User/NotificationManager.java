package com.Ease.User;

import com.Ease.Hibernate.HibernateQuery;
import org.json.simple.JSONArray;

import java.util.List;

public class NotificationManager {
    public NotificationManager() {

    }

    public void readAllNotifications(User user, HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("UPDATE Notification n SET n.newNotification = :bool WHERE n.user.db_id = :user_id AND n.newNotification = :bool1");
        hibernateQuery.setParameter("bool", false);
        hibernateQuery.setParameter("bool1", true);
        hibernateQuery.setParameter("user_id", user.getDb_id());
        hibernateQuery.executeUpdate();
    }

    private List<Notification> getNotifications(int limit, User user, HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT n FROM Notification n WHERE n.user.db_id = :user_id ORDER BY n.creation_date DESC");
        hibernateQuery.setParameter("user_id", user.getDb_id());
        hibernateQuery.setMaxResults(limit);
        return hibernateQuery.list();
    }

    public JSONArray getJson(Integer offset, int limit, User user, HibernateQuery hibernateQuery) {
        JSONArray res = new JSONArray();
        this.getNotifications(limit, user, hibernateQuery).subList(offset, limit).forEach(notification -> res.add(notification.getJson()));
        return res;
    }
}
