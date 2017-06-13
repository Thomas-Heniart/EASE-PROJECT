package com.Ease.Notification;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 02/06/2017.
 */

@Entity
@Table(name = "notifications")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Notification {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer id;

    @Column(name = "title")
    protected String title;

    @Column(name = "content")
    protected String content;

    @Column(name = "logo_path")
    protected String logo_path;

    @Column(name = "seen")
    protected Boolean seen;

    @Column(name = "creation_date")
    protected Date creation_date;

    @Column(name = "seen_date")
    protected Date seen_date;

    @Column(name = "url")
    protected String url;

    public Notification(String title, String content, String logo_path, Boolean seen, Date creation_date, String url) {
        this.title = title;
        this.content = content;
        this.logo_path = logo_path;
        this.seen = seen;
        this.creation_date = creation_date;
        this.url = url;
    }

    protected Notification() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLogo_path() {
        return logo_path;
    }

    public void setLogo_path(String logo_path) {
        this.logo_path = logo_path;
    }

    public Boolean isSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Date getSeen_date() {
        return seen_date;
    }

    public void setSeen_date(Date seen_date) {
        this.seen_date = seen_date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
