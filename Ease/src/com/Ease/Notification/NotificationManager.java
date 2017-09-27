package com.Ease.Notification;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.simple.JSONArray;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationManager {
    private final static Integer OFFSET = 5;
    protected List<Notification> notifications = new LinkedList<>();
    protected Map<Integer, Notification> idNotificationMap = new ConcurrentHashMap<>();
    protected Integer last_index = 1;
    protected String user_id;

    public NotificationManager(String user_id) {
        this.user_id = user_id;
    }

    public void loadNextNotifications(Integer offset, DataBaseConnection db) throws HttpServletException {
        if (offset + 1 < last_index)
            return;
        List<Notification> nextNotifications = Notification.loadNextNotifications(this.user_id, this.last_index, offset - this.last_index, db);
        for (Notification notification : nextNotifications)
            idNotificationMap.put(notification.getId(), notification);
        notifications.addAll(nextNotifications);
        last_index += nextNotifications.size();
    }

    private void addNotification(Notification notification) {
        notifications.add(0, notification);
        idNotificationMap.put(notification.getId(), notification);
        last_index++;
    }

    public Notification addNotification(String content, String url, String icon, DataBaseConnection db) throws HttpServletException {
        return this.addNotification(content, url, icon, new Date(), db);
    }

    public Notification addNotification(String content, String url, String icon, Date timestamp, DataBaseConnection db) throws HttpServletException {
        Notification notification = Notification.createNotification(content, url, icon, this.user_id, timestamp, db);
        this.addNotification(notification);
        return notification;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public List<Notification> getNotifications(Integer start, Integer end) {
        return this.notifications.subList(start, end);
    }

    public Notification getNotificationById(Integer id) throws HttpServletException {
        Notification notification = this.idNotificationMap.get(id);
        if (notification == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No notification with this id");
        return notification;
    }

    private void readNotification(Notification notification, DataBaseConnection db) throws HttpServletException {
        if (!notification.is_new())
            return;
        notification.setIs_new(false, db);
    }

    public void readNotificationWithId(Integer id, DataBaseConnection db) throws HttpServletException {
        Notification notification = this.getNotificationById(id);
        if (!notification.is_new())
            return;
        this.readNotification(notification, db);
    }

    public void readAllNotifications(DataBaseConnection db) throws HttpServletException {
        for (Notification notification : this.notifications) {
            if (!notification.is_new)
                continue;
            this.readNotification(notification, db);
        }
    }

    public JSONArray getJson() {
        JSONArray notifications = new JSONArray();
        for (Notification notification : this.getNotifications())
            notifications.add(notification.getJson());
        return notifications;
    }

    public JSONArray getJson(Integer start, Integer end) {
        JSONArray notifications = new JSONArray();
        if (start < 0 || start > end)
            return notifications;
        List<Notification> notificationList;
        if (end >= this.notifications.size())
            notificationList = this.getNotifications(start, this.notifications.size());
        else
            notificationList = this.getNotifications(start, end);
        for (Notification notification : notificationList)
            notifications.add(notification.getJson());
        return notifications;
    }
}