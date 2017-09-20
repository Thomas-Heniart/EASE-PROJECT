package com.Ease.Dashboard.App.LinkApp;

import com.Ease.Dashboard.App.*;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

public class LinkApp extends App implements SharedApp, ShareableApp {

    public enum Data {
        NOTHING,
        ID,
        APP_ID,
        LINK_APP_INFO_ID,
        GROUP_LINK_APP_ID
    }

	/*
     *
	 * Loader And Creator
	 * 
	 */

    public static LinkApp loadLinkApp(Integer appDBid, Profile profile, Integer position, String insertDate, AppInformation appInfos, GroupApp groupApp, ServletContext context, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * from linkApps WHERE app_id= ?;");
        request.setInt(appDBid);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            LinkAppInformation linkInfos = LinkAppInformation.loadLinkAppInformation(rs.getString(Data.LINK_APP_INFO_ID.ordinal()), db);
            return new LinkApp(appDBid, profile, position, appInfos, groupApp, insertDate, linkInfos, rs.getInt(Data.ID.ordinal()));
        }
        throw new GeneralException(ServletManager.Code.InternError, "Link app not complete in db.");
    }

    public static LinkApp createLinkApp(Profile profile, Integer position, String name, String url, String imgUrl, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        Integer appDBid = App.createApp(profile, position, name, "linkApp", elevator, sm.getDB());
        LinkAppInformation infos = LinkAppInformation.createLinkAppInformation(url, imgUrl, sm.getDB());
        DatabaseRequest request = db.prepareRequest("INSERT INTO linkApps values(NULL, ?, ?, NULL);");
        request.setInt(appDBid);
        request.setInt(infos.getDb_id());
        Integer linkDBid = request.set();
        db.commitTransaction(transaction);
        return new LinkApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), infos, linkDBid);
    }

    public static App createLinkApp(LinkApp app, String name, Profile profile, DataBaseConnection db) throws HttpServletException {
        int transaction = 0;
        try {
            transaction = db.startTransaction();
            Map<String, Object> elevator = new HashMap<String, Object>();
            Integer position = profile.getSize();
            Integer appDBid = App.createApp(profile, position, name, "linkApp", elevator, db);
            LinkAppInformation infos = LinkAppInformation.createLinkAppInformation(app.getLinkAppInformations().getLink(), app.getLinkAppInformations().getImgUrl(), db);
            DatabaseRequest request = db.prepareRequest("INSERT INTO linkApps values(NULL, ?, ?, NULL);");
            request.setInt(appDBid);
            request.setInt(infos.getDb_id());
            Integer linkDBid = request.set();
            db.commitTransaction(transaction);
            return new LinkApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), infos, linkDBid);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static LinkApp createLinkApp(Profile profile, Integer position, String name, String url, String imgUrl, com.Ease.Utils.Servlets.ServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        Integer appDBid = App.createApp(profile, position, name, "linkApp", elevator, db);
        LinkAppInformation infos = LinkAppInformation.createLinkAppInformation(url, imgUrl, db);
        DatabaseRequest request = db.prepareRequest("INSERT INTO linkApps values(NULL, ?, ?, NULL);");
        request.setInt(appDBid);
        request.setInt(infos.getDb_id());
        Integer linkDBid = request.set();
        db.commitTransaction(transaction);
        return new LinkApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), infos, linkDBid);
    }

    public static LinkApp createShareableLinkApp(String name, String link, PostServletManager sm) throws GeneralException, HttpServletException {
        return createLinkApp(null, null, name, link, "/resources/icons/link_app.png", sm);
    }

	/*
     *
	 * Constructor
	 * 
	 */

    protected LinkAppInformation linkInfos;
    protected GroupLinkApp groupLinkApp;
    protected Integer linkAppDBid;

    public LinkApp(Integer db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, LinkAppInformation linkInfos, Integer linkAppDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate);
        this.linkInfos = linkInfos;
        this.groupLinkApp = (GroupLinkApp) groupApp;
        this.linkAppDBid = linkAppDBid;
    }

    public LinkApp(Integer db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, LinkAppInformation linkInfos, Integer linkAppDBid, ShareableApp holder) {
        super(db_id, profile, position, infos, groupApp, insertDate, holder);
        this.linkInfos = linkInfos;
        this.groupLinkApp = (GroupLinkApp) groupApp;
        this.linkAppDBid = linkAppDBid;
    }

    public void removeFromDB(DataBaseConnection db) throws GeneralException, HttpServletException {
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM linkApps WHERE id= ?;");
        request.setInt(linkAppDBid);
        request.set();
        if ((this.groupApp == null || this.groupApp.isCommon() == false) && this.getHolder() == null)
            linkInfos.removeFromDb(db);
        super.removeFromDB(db);
        db.commitTransaction(transaction);
    }

	/*
     *
	 * Getter And Setter
	 * 
	 */

    public JSONArray getJSON(ServletManager sm) {
        JSONObject link = new JSONObject();
        link.put("app_name", this.informations.getName());
        link.put("url", linkInfos.getLink());
        JSONArray result = new JSONArray();
        result.add(link);
        return result;
    }

    public JSONObject getJSON() {
        JSONObject res = super.getJSON();
        res.put("url", this.linkInfos.getLink());
        res.put("imgSrc", this.linkInfos.getImgUrl());
        return res;
    }

    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("logo", this.linkInfos.getImgUrl());
        return res;
    }

    public void fillJson(JSONObject json) {
        super.fillJson(json);
        json.put("url", this.linkInfos.getLink());
        json.put("imgSrc", this.linkInfos.getImgUrl());
        json.put("type", "linkApp");
    }

    public LinkAppInformation getLinkAppInformations() {
        return this.linkInfos;
    }

    @Override
    public String getLogo() {
        return this.linkInfos.getImgUrl();
    }

    @Override
    public void modifyShared(DataBaseConnection db, JSONObject editJson) throws HttpServletException {
        throw new HttpServletException(HttpStatus.Forbidden, "You cannot modify a shared link app");
    }

    @Override
    public void modifyShareable(DataBaseConnection db, JSONObject editJson) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            super.modifyShareable(db, editJson);
            this.getLinkAppInformations().edit(editJson, db);
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public SharedApp share(TeamUser teamUser_tenant, Team team, JSONObject params, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<>();
        Integer appDBid = App.createSharedApp(null, null, this.getName(), "linkApp", elevator, team.getDb_id(), teamUser_tenant.getDb_id(), this, true, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO linkApps values(NULL, ?, ?, NULL);");
        request.setInt(appDBid);
        request.setInt(this.linkInfos.getDb_id());
        Integer linkDBid = request.set();
        db.commitTransaction(transaction);
        LinkApp sharedApp = new LinkApp(appDBid, null, null, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), linkInfos, linkDBid, this);
        sharedApp.setAdminHasAccess(true, sm.getDB());
        sharedApp.setTeamUser_tenant(teamUser_tenant);
        sharedApp.setReceived(true);
        sharedApp.setCanSeeInformation(true);
        return sharedApp;
    }

    @Override
    public JSONObject getShareableJson() throws HttpServletException {
        JSONObject res = super.getShareableJson();
        res.put("type", "link");
        res.put("logo", this.getLinkAppInformations().getImgUrl());
        res.put("url", this.getLinkAppInformations().getLink());
        return res;
    }
}
