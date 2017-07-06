package com.Ease.Dashboard.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.Ease.Utils.*;

public class Status {

    public enum Data {
        NOTHING,
        ID,
        FIRST_CONNECTION,
        CGU,
        CHROME_SCRAPPING,
        APPS_MANUALLY_ADDED,
        CLICK_ON_APP,
        MOVE_APPS,
        OPEN_CATALOG,
        ADD_AN_APP,
        TUTO_DONE,
        INVITE_SENDED,
        LAST_CONNECTION,
        HOMEPAGE_EMAIL_SENT
    }

    public static Status createStatus(Boolean send_news, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("INSERT INTO status values (null, 0, 0, 0, 0, 0, 0, 0, 0, 0, default, 0, 0, ?, 0);");
        request.setBoolean(send_news);
        String db_id = request.set().toString();
        Status status = new Status(db_id, false, false, false, false, false, false, false, false, false, false);
        status.setSend_news(send_news);
        status.setTerms_reviewed(false);
        return status;
    }

    public static Status loadStatus(String db_id, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM status WHERE id= ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        rs.next();
        boolean first_connection = rs.getBoolean(Data.FIRST_CONNECTION.ordinal());
        boolean CGU = rs.getBoolean(Data.CGU.ordinal());
        boolean chrome_scrapping = rs.getBoolean(Data.CHROME_SCRAPPING.ordinal());
        boolean apps_manually_added = rs.getBoolean(Data.APPS_MANUALLY_ADDED.ordinal());
        boolean click_on_app = rs.getBoolean(Data.CLICK_ON_APP.ordinal());
        boolean move_apps = rs.getBoolean(Data.MOVE_APPS.ordinal());
        boolean open_catalog = rs.getBoolean(Data.OPEN_CATALOG.ordinal());
        boolean add_an_app = rs.getBoolean(Data.ADD_AN_APP.ordinal());
        boolean tuto_done = rs.getBoolean(Data.TUTO_DONE.ordinal());
        boolean invite_sended = rs.getBoolean(Data.INVITE_SENDED.ordinal());
        Status loadedStatus = new Status(db_id, first_connection, CGU, chrome_scrapping, apps_manually_added, click_on_app,
                move_apps, open_catalog, add_an_app, tuto_done, invite_sended);
        loadedStatus.setSend_news(rs.getBoolean("send_news"));
        loadedStatus.setTerms_reviewed(rs.getBoolean("terms_reviewed"));
        loadedStatus.updateLastConnection(db);
        return loadedStatus;
    }

    protected String db_id;
    protected boolean first_connection;
    protected boolean CGU;
    protected boolean chrome_scrapping;
    protected boolean apps_manually_added;
    protected boolean click_on_app;
    protected boolean move_apps;
    protected boolean open_catalog;
    protected boolean tuto_done;
    private boolean add_an_app;
    private boolean invite_sended;
    private boolean send_news;
    private boolean terms_reviewed;

    public Status(String db_id, boolean first_connection, boolean CGU, boolean chrome_scrapping,
                  boolean apps_manually_added, boolean click_on_app, boolean move_apps, boolean open_catalog,
                  boolean add_an_app, boolean tuto_done, boolean invite_sended) {
        this.db_id = db_id;
        this.first_connection = first_connection;
        this.CGU = CGU;
        this.chrome_scrapping = chrome_scrapping;
        this.apps_manually_added = apps_manually_added;
        this.click_on_app = click_on_app;
        this.move_apps = move_apps;
        this.open_catalog = open_catalog;
        this.add_an_app = add_an_app;
        this.tuto_done = tuto_done;
        this.invite_sended = invite_sended;
    }

    public String getDbId() {
        return this.db_id;
    }

    public void passStep(String tutoStep, DataBaseConnection db) throws GeneralException {
        try {
            Method method = this.getClass().getMethod("set_" + tutoStep, Boolean.class);
            method.invoke(this, true);
            DatabaseRequest request = db.prepareRequest("UPDATE status SET " + tutoStep + " = 1 WHERE id = ?;");
            request.setInt(db_id);
            request.set();
            validateTuto(db);
        } catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException
                | InvocationTargetException e) {
            throw new GeneralException(ServletManager.Code.ClientError, e);
        }
    }

    public void set_first_connection(Boolean first_connection) {
        this.first_connection = first_connection;
    }

    public void set_CGU(Boolean cGU) {
        CGU = cGU;
    }

    public void set_chrome_scrapping(Boolean chrome_scrapping) {
        this.chrome_scrapping = chrome_scrapping;
    }

    public void set_apps_manually_added(Boolean apps_manually_added) {
        this.apps_manually_added = apps_manually_added;
    }

    public void set_click_on_app(Boolean click_on_app) {
        this.click_on_app = click_on_app;
    }

    public void set_move_apps(Boolean move_apps) {
        this.move_apps = move_apps;
    }

    public void set_open_catalog(Boolean open_catalog) {
        this.open_catalog = open_catalog;
    }

    public void set_tuto_done(Boolean tuto_done) {
        this.tuto_done = tuto_done;
    }

    public void set_add_an_app(Boolean add_an_app) {
        this.add_an_app = add_an_app;
    }

    public boolean tutoIsDone() {
        return this.tuto_done;
    }

    public void validateTuto(DataBaseConnection db) throws GeneralException {
        if ((!this.tuto_done) && this.CGU && this.first_connection
                && this.allTipsDone() && this.appsImported())
            this.passStep("tuto_done", db);
    }

    public boolean appsImported() {
        return this.chrome_scrapping || this.apps_manually_added;
    }

    public boolean allTipsDone() {
        return this.click_on_app && this.open_catalog && this.move_apps && this.add_an_app;
    }

    public boolean clickOnAppDone() {
        return this.click_on_app;
    }

    public boolean moveAppDone() {
        return this.move_apps;
    }

    public boolean openCatalogDone() {
        return this.open_catalog;
    }

    public boolean addAnAppDone() {
        return this.add_an_app;
    }

    public void set_invite_sended(Boolean invite_sended) {
        this.invite_sended = invite_sended;
    }

    public boolean invite_sended() {
        return this.invite_sended;
    }

    public boolean send_news() {
        return this.send_news;
    }

    public void setSend_news(boolean send_news) {
        this.send_news = send_news;
    }

    public void setSend_news(boolean send_news, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE status SET send_news = ? WHERE id = ?;");
            request.setBoolean(send_news);
            request.setInt(this.getDbId());
            request.set();
            this.setSend_news(send_news);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public boolean terms_reviewed() {
        return this.terms_reviewed;
    }

    public void setTerms_reviewed(boolean terms_reviewed) {
        this.terms_reviewed = terms_reviewed;
    }

    public void setTerms_reviewed(boolean terms_reviewed, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UDPATE status SET terms_reviewed = ? WHERE id = ?;");
            request.setBoolean(terms_reviewed);
            request.setInt(this.getDbId());
            request.set();
            this.setTerms_reviewed(terms_reviewed);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.BadRequest, e);
        }
    }

    public void updateLastConnection(DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("UPDATE status SET last_connection = CURDATE() WHERE id = ?;");
        request.setInt(db_id);
        request.set();
    }
}
