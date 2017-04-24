package com.Ease.Website;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Table(name = "WebsiteAttributes")
public class WebsiteAttributes {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "locked")
    protected Boolean locked;

    @Column(name = "lockedExpiration")
    protected Date lockedExpiration;

    @Column(name = "addedDate")
    protected Date addedDate;

    @Column(name = "new")
    protected Boolean isNew;

    @Column(name = "work")
    protected Boolean work;

    @Column(name = "visits")
    protected Integer visits;

    @Column(name = "blacklisted")
    protected Boolean blacklisted;

    public WebsiteAttributes(Boolean locked, Date lockedExpiration, Date addedDate, Boolean isNew, Boolean work, Integer visits, Boolean blacklisted) {
        this.locked = locked;
        this.lockedExpiration = lockedExpiration;
        this.addedDate = addedDate;
        this.isNew = isNew;
        this.work = work;
        this.visits = visits;
        this.blacklisted = blacklisted;
    }

    public WebsiteAttributes() {

    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Date getLockedExpiration() {
        return lockedExpiration;
    }

    public void setLockedExpiration(Date lockedExpiration) {
        this.lockedExpiration = lockedExpiration;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Boolean getWork() {
        return work;
    }

    public void setWork(Boolean work) {
        this.work = work;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Boolean getBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(Boolean blacklisted) {
        this.blacklisted = blacklisted;
    }
}
