package com.Ease.Dashboard.User;

import com.Ease.Utils.*;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Status {

    public static Status createStatus(DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("INSERT INTO status values (null, 1, 0, 0, 0, default, 0, 0, 0);");
        String db_id = request.set().toString();
        return new Status(db_id, true, false, false, false, false, false);
    }

    public static Status loadStatus(String db_id, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM status WHERE id= ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        rs.next();
        boolean first_connection = rs.getBoolean("first_connection");
        boolean chrome_scrapping = rs.getBoolean("chrome_scrapping");
        boolean apps_manually_added = rs.getBoolean("apps_manually_added");
        boolean tuto_done = rs.getBoolean("tuto_done");
        boolean team_tuto_done = rs.getBoolean("team_tuto_done");
        boolean terms_reviewed = rs.getBoolean("terms_reviewed");
        Status loadedStatus = new Status(db_id, first_connection, chrome_scrapping, apps_manually_added, tuto_done, team_tuto_done, terms_reviewed);
        loadedStatus.setTerms_reviewed(rs.getBoolean("terms_reviewed"));
        loadedStatus.updateLastConnection(db);
        return loadedStatus;
    }

    protected String db_id;
    protected boolean first_connection;
    protected boolean chrome_scrapping;
    protected boolean apps_manually_added;
    protected boolean tuto_done;
    protected boolean team_tuto_done;
    protected boolean terms_reviewed;

    public Status(String db_id, boolean first_connection, boolean chrome_scrapping, boolean apps_manually_added, boolean tuto_done, boolean team_tuto_done, boolean terms_reviewed) {
        this.db_id = db_id;
        this.first_connection = first_connection;
        this.chrome_scrapping = chrome_scrapping;
        this.apps_manually_added = apps_manually_added;
        this.tuto_done = tuto_done;
        this.team_tuto_done = team_tuto_done;
        this.terms_reviewed = terms_reviewed;
    }

    public String getDbId() {
        return this.db_id;
    }

    public void set_first_connection(Boolean first_connection) {
        this.first_connection = first_connection;
    }

    public void set_chrome_scrapping(Boolean chrome_scrapping) {
        this.chrome_scrapping = chrome_scrapping;
    }

    public void set_apps_manually_added(Boolean apps_manually_added) {
        this.apps_manually_added = apps_manually_added;
    }

    public void set_tuto_done(Boolean tuto_done) {
        this.tuto_done = tuto_done;
    }

    public boolean tutoIsDone() {
        return this.tuto_done;
    }

    public boolean appsImported() {
        return this.chrome_scrapping || this.apps_manually_added;
    }

    public boolean isTeam_tuto_done() {
        return this.team_tuto_done;
    }

    public void setTeam_tuto_done(Boolean team_tuto_done, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("UPDATE status SET team_tuto_done = ? WHERE id = ?;");
        request.setBoolean(team_tuto_done);
        request.setInt(this.db_id);
        request.set();
        this.team_tuto_done = team_tuto_done;
    }

    public boolean terms_reviewed() {
        return this.terms_reviewed;
    }

    private void setTerms_reviewed(boolean terms_reviewed) {
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

    public Boolean isTuto_done() {
        return this.tuto_done;
    }

    public void setTuto_done(Boolean tuto_done) {
        this.tuto_done = tuto_done;
    }

    public void setTuto_done(Boolean tuto_done, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("UPDATE status SET tuto_done = ? WHERE id = ?;");
        request.setBoolean(tuto_done);
        request.setInt(this.getDbId());
        request.set();
        this.setTuto_done(tuto_done);
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("tuto_done", this.tutoIsDone());
        res.put("team_tuto_done", this.isTeam_tuto_done());
        res.put("terms_reviewed", this.terms_reviewed());
        return res;
    }

    public void passStep(String tutoStep, DataBaseConnection db) throws GeneralException {
        try {
            Method method = this.getClass().getMethod("set_" + tutoStep, Boolean.class);
            method.invoke(this, true);
            DatabaseRequest request = db.prepareRequest("UPDATE status SET " + tutoStep + " = 1 WHERE id = ?;");
            request.setInt(db_id);
            request.set();
        } catch (SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new GeneralException(ServletManager.Code.ClientError, e);
        }
    }
}
