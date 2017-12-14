package com.Ease.User;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamUser;
import com.Ease.websocketV1.WebSocketManager;
import com.Ease.websocketV1.WebSocketMessageFactory;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pendingTeamUserNotifications")
public class PendingNotification {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @ManyToOne
    @JoinColumn(name = "teamUser_id")
    private TeamUser teamUser;

    @Column(name = "content")
    private String content;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation_date = new Date();

    @Column(name = "url")
    private String url;

    @Column(name = "icon")
    private String icon;

    public PendingNotification() {

    }

    public PendingNotification(TeamUser teamUser, String content, String url, String icon) {
        this.teamUser = teamUser;
        this.content = content;
        this.url = url;
        this.icon = icon;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public TeamUser getTeamUser() {
        return teamUser;
    }

    public void setTeamUser(TeamUser teamUser) {
        this.teamUser = teamUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void sendToUser(User user, WebSocketManager userWebSocketManager, HibernateQuery hibernateQuery) {
        Notification notification = new Notification(this.getContent(), this.getUrl(), this.getIcon(), user);
        hibernateQuery.saveOrUpdateObject(notification);
        userWebSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
        hibernateQuery.deleteObject(this);
    }
}