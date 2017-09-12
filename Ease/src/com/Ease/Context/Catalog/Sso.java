package com.Ease.Context.Catalog;

import com.Ease.Utils.*;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Sso {

    public enum Data {
        NOTHING,
        ID,
        NAME,
        IMG
    }

    public static List<Sso> loadSsos(DataBaseConnection db, ServletContext context) throws GeneralException {
        List<Sso> ssos = new LinkedList<Sso>();
        DatabaseResult rs = db.prepareRequest("SELECT * FROM sso").get();
        Sso sso;
        while (rs.next()) {
            sso = new Sso(rs.getInt(Data.ID.ordinal()), rs.getString(Data.NAME.ordinal()), rs.getString(Data.IMG.ordinal()));
            ssos.add(sso);
        }
        return ssos;
    }

    protected List<Website> websites;
    protected Map<Integer, Website> websitesIdMap = new HashMap<>();
    protected String name;
    protected Integer db_id;
    protected String img_path;

    public Sso(Integer db_id, String name, String img_path) {
        this.name = name;
        this.websites = new LinkedList<Website>();
        this.db_id = db_id;
        this.img_path = img_path;
    }

    public void addWebsite(Website site) {
        this.websites.add(site);
        this.websitesIdMap.put(site.getDb_id(), site);
    }

    public void removeWebsite(Website website, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE websites SET sso = NULL where id = ?;");
            request.setInt(website.getDb_id());
            request.set();
            this.websites.remove(website);
            this.websitesIdMap.remove(website.getDb_id());
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public Integer getDbid() {
        return this.db_id;
    }

    public String getName() {
        return this.name;
    }

    public String getImgPath() {
        return "/resources/sso/" + img_path;
    }

    public JSONObject getJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("imgSrc", this.getImgPath());
        json.put("singleId", this.db_id);
        return json;
    }

    public void refresh(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM sso where id = ?");
        request.setInt(this.db_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.InternError, "This sso does not exist");
        this.name = rs.getString(Data.NAME.ordinal());
        this.img_path = rs.getString(Data.IMG.ordinal());
    }
}
