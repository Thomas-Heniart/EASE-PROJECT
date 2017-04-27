package com.Ease.NewDashboard.User;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 21/04/2017.
 */
@Entity
@Table(name = "status")
public class Status {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "first_connection")
    protected Boolean first_connection;

    @Column(name = "CGU")
    protected Boolean CGU;

    @Column(name = "chrome_scrapping")
    protected Boolean chrome_scrapping;

    @Column(name = "apps_manually_added")
    protected Boolean apps_manually_added;

    @Column(name = "click_on_app")
    protected Boolean click_on_app;

    @Column(name = "move_apps")
    protected Boolean move_apps;

    @Column(name = "open_catalog")
    protected Boolean open_catalog;

    @Column(name = "add_an_app")
    protected Boolean add_an_app;

    @Column(name = "tuto_done")
    protected Boolean tuto_done;

    @Column(name = "last_connection")
    protected Date last_connection;

    @Column(name = "invite_sended")
    protected Boolean invite_sended;

    @Column(name = "homepage_email_sent")
    protected Boolean homepage_email_sent;

    public Status(Boolean first_connection, Boolean CGU, Boolean chrome_scrapping, Boolean apps_manually_added, Boolean click_on_app, Boolean move_apps, Boolean open_catalog, Boolean add_an_app, Boolean tuto_done, Date last_connection, Boolean invite_sended, Boolean homepage_email_sent) {
        this.first_connection = first_connection;
        this.CGU = CGU;
        this.chrome_scrapping = chrome_scrapping;
        this.apps_manually_added = apps_manually_added;
        this.click_on_app = click_on_app;
        this.move_apps = move_apps;
        this.open_catalog = open_catalog;
        this.add_an_app = add_an_app;
        this.tuto_done = tuto_done;
        this.last_connection = last_connection;
        this.invite_sended = invite_sended;
        this.homepage_email_sent = homepage_email_sent;
    }

    public Status() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Boolean getFirst_connection() {
        return first_connection;
    }

    public void setFirst_connection(Boolean first_connection) {
        this.first_connection = first_connection;
    }

    public Boolean getCGU() {
        return CGU;
    }

    public void setCGU(Boolean CGU) {
        this.CGU = CGU;
    }

    public Boolean getChrome_scrapping() {
        return chrome_scrapping;
    }

    public void setChrome_scrapping(Boolean chrome_scrapping) {
        this.chrome_scrapping = chrome_scrapping;
    }

    public Boolean getApps_manually_added() {
        return apps_manually_added;
    }

    public void setApps_manually_added(Boolean apps_manually_added) {
        this.apps_manually_added = apps_manually_added;
    }

    public Boolean getClick_on_app() {
        return click_on_app;
    }

    public void setClick_on_app(Boolean click_on_app) {
        this.click_on_app = click_on_app;
    }

    public Boolean getMove_apps() {
        return move_apps;
    }

    public void setMove_apps(Boolean move_apps) {
        this.move_apps = move_apps;
    }

    public Boolean getOpen_catalog() {
        return open_catalog;
    }

    public void setOpen_catalog(Boolean open_catalog) {
        this.open_catalog = open_catalog;
    }

    public Boolean getAdd_an_app() {
        return add_an_app;
    }

    public void setAdd_an_app(Boolean add_an_app) {
        this.add_an_app = add_an_app;
    }

    public Boolean getTuto_done() {
        return tuto_done;
    }

    public void setTuto_done(Boolean tuto_done) {
        this.tuto_done = tuto_done;
    }

    public Date getLast_connection() {
        return last_connection;
    }

    public void setLast_connection(Date last_connection) {
        this.last_connection = last_connection;
    }

    public Boolean getInvite_sended() {
        return invite_sended;
    }

    public void setInvite_sended(Boolean invite_sended) {
        this.invite_sended = invite_sended;
    }

    public Boolean getHomepage_email_sent() {
        return homepage_email_sent;
    }

    public void setHomepage_email_sent(Boolean homepage_email_sent) {
        this.homepage_email_sent = homepage_email_sent;
    }

    public boolean appsImported() {
        return this.chrome_scrapping || this.apps_manually_added;
    }

    public boolean allTipsDone() {
        return this.click_on_app && this.open_catalog && this.move_apps && this.add_an_app;
    }

    public static Status createDefaultStatus() {
        return new Status(false, false, false, false, false, false, false, false, false, new Date(), false, false);
    }
}
