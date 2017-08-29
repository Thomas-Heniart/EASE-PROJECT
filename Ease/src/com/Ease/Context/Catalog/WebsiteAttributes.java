package com.Ease.Context.Catalog;

import com.Ease.Utils.*;

public class WebsiteAttributes {

    public static WebsiteAttributes createWebsiteAttributes(boolean is_public, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("INSERT INTO websiteAttributes values (null, 0, null, default, 1, ?, default, 0, ?, default);");
        request.setBoolean(is_public);
        request.setBoolean(!is_public);
        String db_id = request.set().toString();
        return new WebsiteAttributes(db_id, false, true, is_public, 0, false, !is_public, true);
    }

    public static WebsiteAttributes createWebsiteAttributes(boolean is_public, boolean integrated, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("INSERT INTO websiteAttributes values (null, 0, null, default, 1, ?, default, 0, ?, ?);");
            request.setBoolean(is_public);
            request.setBoolean(!is_public);
            request.setBoolean(integrated);
            String db_id = request.set().toString();
            return new WebsiteAttributes(db_id, false, true, is_public, 0, false, !is_public, integrated);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static WebsiteAttributes loadWebsiteAttributes(String db_id, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM websiteAttributes WHERE id= ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        rs.next();
        return new WebsiteAttributes(db_id, rs.getBoolean("locked"), rs.getBoolean("new"), rs.getBoolean("public"), rs.getInt("visits"), rs.getBoolean("blacklisted"), rs.getBoolean("noScrap"), rs.getBoolean("integrated"));
    }

    protected String db_id;
    protected boolean locked;
    protected boolean isNew;
    protected boolean is_public;
    protected int visits;
    protected boolean blacklisted;
    protected boolean noScrap;
    protected boolean integrated;

    public WebsiteAttributes(String db_id, boolean locked, boolean isNew, boolean is_public, int visits, boolean blacklisted, boolean noScrap, boolean integrated) {
        this.db_id = db_id;
        this.locked = locked;
        this.isNew = isNew;
        this.is_public = is_public;
        this.blacklisted = blacklisted;
        this.noScrap = noScrap;
        this.integrated = integrated;
    }

    public boolean isPublic() {
        return this.is_public;
    }

    public boolean isNew() {
        return this.isNew;
    }

    public String getDbId() {
        return db_id;
    }

    public int getVisits() {
        return visits;
    }

    public void increaseVisits(int count, ServletManager sm) throws GeneralException {
        if (this.blacklisted)
            return;
        DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET visits = ? WHERE id = ?;");
        request.setInt(visits + count);
        request.setInt(db_id);
        request.set();
        this.visits += count;
    }

    public void setVisits(int visits, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("UPDATE websiteAttributes SET visits = ? WHERE id = ?;");
        request.setInt(visits);
        request.setInt(db_id);
        request.set();
        this.visits = visits;

    }

    public void setNew(boolean b, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("UPDATE websiteAttributes SET new = ? WHERE id = ?;");
        request.setBoolean(b);
        request.setInt(this.getDbId());
        request.set();
        this.isNew = b;
    }

    public void turnOff(ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET public = ?, noScrap = ? WHERE id = ?");
        request.setBoolean(false);
        request.setBoolean(true);
        request.setInt(db_id);
        request.set();
        this.is_public = false;
        this.noScrap = true;
    }

    public void bePrivate(DataBaseConnection db) throws HttpServletException {
        if (!this.isPublic())
            return;
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE websiteAttributes SET public = ?, noScrap = ? WHERE id = ?;");
            request.setBoolean(false);
            request.setBoolean(true);
            request.set();
            this.is_public = false;
            this.noScrap = true;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void bePublic(DataBaseConnection db) throws HttpServletException {
        if (this.isPublic())
            return;
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE websiteAttributes SET public = ?, noScrap = ? WHERE id = ?;");
            request.setBoolean(true);
            request.setBoolean(false);
            request.set();
            this.is_public = true;
            this.noScrap = false;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }


    public void turnOn(ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET public = ? WHERE id = ?");
        request.setBoolean(true);
        request.setInt(db_id);
        request.set();
        this.is_public = true;
    }

    public void blacklist(ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET blacklisted = ? WHERE id = ?;");
        request.setBoolean(true);
        request.setInt(db_id);
        request.set();
        this.blacklisted = true;
    }

    public void whitelist(ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("UPDATE websiteAttributes SET blacklisted = ? WHERE id = ?;");
        request.setBoolean(false);
        request.setInt(db_id);
        request.set();
        this.blacklisted = false;
    }

    public Boolean isBlacklisted() {
        return this.blacklisted;
    }

    public boolean canBeScrapped() {
        return !this.noScrap;
    }

    public boolean isIntegrated() {
        return this.integrated;
    }

    public void integrate(DataBaseConnection db) throws HttpServletException {
        if (this.isIntegrated())
            return;
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE websiteAttributes SET integrated = ? WHERE id = ?;");
            request.setBoolean(true);
            request.setInt(this.getDbId());
            request.set();
            this.integrated = true;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }
}
