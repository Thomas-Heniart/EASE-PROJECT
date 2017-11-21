package com.Ease.User;

import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "status")
public class UserStatus {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "first_connection")
    private boolean first_connection = true;

    @Column(name = "chrome_scrapping")
    private boolean chrome_scrapping_done = false;

    @Column(name = "apps_manually_added")
    private boolean apps_manually_added = false;

    @Column(name = "tuto_done")
    private boolean tuto_done = false;

    @Column(name = "last_connection")
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_connection = new Date();

    @Column(name = "homepage_email_sent")
    private boolean homepage_email_sent = false;

    @Column(name = "terms_reviewed")
    private boolean terms_reviewed = false;

    @Column(name = "team_tuto_done")
    private boolean team_tuto_done = false;

    @Column(name = "edit_email_code")
    private String edit_email_code;

    @Column(name = "email_requested")
    private String email_requested;

    public UserStatus() {

    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public boolean isFirst_connection() {
        return first_connection;
    }

    public void setFirst_connection(boolean first_connection) {
        this.first_connection = first_connection;
    }

    public boolean isChrome_scrapping_done() {
        return chrome_scrapping_done;
    }

    public void setChrome_scrapping_done(boolean chrome_scrapping_done) {
        this.chrome_scrapping_done = chrome_scrapping_done;
    }

    public boolean isApps_manually_added() {
        return apps_manually_added;
    }

    public void setApps_manually_added(boolean apps_manually_added) {
        this.apps_manually_added = apps_manually_added;
    }

    public boolean isTuto_done() {
        return tuto_done;
    }

    public void setTuto_done(boolean tuto_done) {
        this.tuto_done = tuto_done;
    }

    public Date getLast_connection() {
        return last_connection;
    }

    public void setLast_connection(Date last_connection) {
        this.last_connection = last_connection;
    }

    public boolean isHomepage_email_sent() {
        return homepage_email_sent;
    }

    public void setHomepage_email_sent(boolean homepage_email_sent) {
        this.homepage_email_sent = homepage_email_sent;
    }

    public boolean isTerms_reviewed() {
        return terms_reviewed;
    }

    public void setTerms_reviewed(boolean terms_reviewed) {
        this.terms_reviewed = terms_reviewed;
    }

    public boolean isTeam_tuto_done() {
        return team_tuto_done;
    }

    public void setTeam_tuto_done(boolean team_tuto_done) {
        this.team_tuto_done = team_tuto_done;
    }

    public String getEdit_email_code() {
        return edit_email_code;
    }

    public void setEdit_email_code(String edit_email_code) {
        this.edit_email_code = edit_email_code;
    }

    public String getEmail_requested() {
        return email_requested;
    }

    public void setEmail_requested(String email_requested) {
        this.email_requested = email_requested;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserStatus that = (UserStatus) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("tuto_done", this.isTuto_done());
        res.put("team_tuto_done", this.isTeam_tuto_done());
        res.put("terms_reviewed", this.isTerms_reviewed());
        return res;
    }
}