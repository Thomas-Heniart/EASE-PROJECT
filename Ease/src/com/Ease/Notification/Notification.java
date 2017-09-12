package com.Ease.Notification;

import com.Ease.Utils.*;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by thomas on 02/06/2017.
 */

public class Notification {

    private final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Notification createNotification(String content, String url, String icon, String user_id, Date date, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("INSERT INTO notifications VALUES (null, ?, ?, ?, ?, default, ?);");
            request.setString(content);
            request.setDate(date);
            request.setString(url);
            request.setString(icon);
            request.setInt(user_id);
            Integer id = request.set();
            return new Notification(id, content, date, url, icon, true);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }

    }

    public static List<Notification> loadNextNotifications(String user_id, Integer last_index, Integer offset, DataBaseConnection db) throws HttpServletException {
        List<Notification> nextNotifications = new LinkedList<>();
        try {
            DatabaseRequest request = db.prepareRequest("SELECT id, content, creation_date, url, icon, is_new FROM notifications WHERE user_id = ? ORDER BY creation_date DESC LIMIT ?,?;");
            request.setInt(user_id);
            request.setInt(last_index - 1);
            request.setInt(last_index + offset);
            DatabaseResult rs = request.get();
            int i = 0;
            while (rs.next()) {
                Notification notification = new Notification(rs.getInt("id"), rs.getString("content"), dateFormat.parse(rs.getString("creation_date")), rs.getString("url"), rs.getString("icon"), rs.getBoolean("is_new"));
                nextNotifications.add(notification);
            }
            return nextNotifications;
        } catch (Exception e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    protected Integer id;
    protected String content;
    protected Date creation_date;
    protected String icon;
    protected Boolean is_new = false;
    protected String url;

    public Notification(Integer id, String content, Date creation_date, String url, String icon, Boolean is_new) {
        this.id = id;
        this.content = content;
        this.creation_date = creation_date;
        this.icon = icon;
        this.is_new = is_new;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean is_new() {
        return is_new;
    }

    public void setIs_new(Boolean is_new, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE notifications SET is_new = ? WHERE id = ?;");
            request.setBoolean(is_new);
            request.setInt(this.getId());
            request.set();
            this.is_new = is_new;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getId());
        res.put("content", this.getContent());
        res.put("icon", this.getIcon());
        res.put("date", this.getCreation_date().getTime());
        res.put("url", this.getUrl());
        res.put("is_new", this.is_new());
        return res;
    }
}
