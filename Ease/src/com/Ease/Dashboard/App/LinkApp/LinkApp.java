package com.Ease.Dashboard.App.LinkApp;

import java.util.HashMap;
import java.util.Map;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Servlets.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Dashboard.Profile.Profile;

import javax.servlet.ServletContext;

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

    public static LinkApp loadLinkApp(String appDBid, Profile profile, Integer position, String insertDate, AppInformation appInfos, GroupApp groupApp, ServletContext context, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * from linkApps WHERE app_id= ?;");
        request.setInt(appDBid);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            LinkAppInformation linkInfos = LinkAppInformation.loadLinkAppInformation(rs.getString(Data.LINK_APP_INFO_ID.ordinal()), db);
            /*GroupLinkApp groupLinkApp = null;
            String groupLinkId = rs.getString(Data.GROUP_LINK_APP_ID.ordinal());
			if (groupLinkId != null)
				groupLinkApp  = (GroupLinkApp) GroupManager.getGroupManager(sm).getGroupAppFromDBid(groupLinkId);*/
            IdGenerator idGenerator = (IdGenerator) context.getAttribute("idGenerator");
            return new LinkApp(appDBid, profile, position, appInfos, groupApp, insertDate, idGenerator.getNextId(), linkInfos, rs.getString(Data.ID.ordinal()));
        }
        throw new GeneralException(ServletManager.Code.InternError, "Link app not complete in db.");
    }

    public static LinkApp createLinkApp(Profile profile, Integer position, String name, String url, String imgUrl, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String appDBid = App.createApp(profile, position, name, "linkApp", elevator, sm.getDB());
        LinkAppInformation infos = LinkAppInformation.createLinkAppInformation(url, imgUrl, sm.getDB());
        DatabaseRequest request = db.prepareRequest("INSERT INTO linkApps values(NULL, ?, ?, NULL);");
        request.setInt(appDBid);
        request.setInt(infos.getDb_id());
        String linkDBid = request.set().toString();
        db.commitTransaction(transaction);
        return new LinkApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), infos, linkDBid);
    }

    public static LinkApp createLinkApp(Profile profile, Integer position, String name, String url, String imgUrl, com.Ease.Utils.Servlets.ServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String appDBid = App.createApp(profile, position, name, "linkApp", elevator, db);
        LinkAppInformation infos = LinkAppInformation.createLinkAppInformation(url, imgUrl, db);
        DatabaseRequest request = db.prepareRequest("INSERT INTO linkApps values(NULL, ?, ?, NULL);");
        request.setInt(appDBid);
        request.setInt(infos.getDb_id());
        String linkDBid = request.set().toString();
        db.commitTransaction(transaction);
        return new LinkApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), infos, linkDBid);
    }

    public static LinkApp createShareableLinkApp(String name, String link, PostServletManager sm) throws GeneralException, HttpServletException {
        Catalog catalog = (Catalog) sm.getContextAttr("catalog");
        String linkHost = link.split("\\.")[1];
        String imgUrl = "";
        Website websiteForLogo = catalog.getWebsiteWithHost(linkHost);
        if (websiteForLogo == null)
            imgUrl = linkHost.substring(0, 2);
        else
            imgUrl = websiteForLogo.getLogo();
        return createLinkApp(null, null, name, link, imgUrl, sm);
    }

	/*
	 * 
	 * Constructor
	 * 
	 */

    protected LinkAppInformation linkInfos;
    protected GroupLinkApp groupLinkApp;
    protected String linkAppDBid;

    public LinkApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, LinkAppInformation linkInfos, String linkAppDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id);
        this.linkInfos = linkInfos;
        this.groupLinkApp = (GroupLinkApp) groupApp;
        this.linkAppDBid = linkAppDBid;
    }

    public LinkApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, LinkAppInformation linkInfos, String linkAppDBid, ShareableApp holder) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id, holder);
        this.linkInfos = linkInfos;
        this.groupLinkApp = (GroupLinkApp) groupApp;
        this.linkAppDBid = linkAppDBid;
    }

    public void removeFromDB(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM linkApps WHERE id= ?;");
        request.setInt(linkAppDBid);
        request.set();
        if ((this.groupApp == null || this.groupApp.isCommon() == false) && this.getHolder() == null)
            linkInfos.removeFromDb(sm);
        super.removeFromDB(sm);
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

    public JSONObject getJson() {
        JSONObject res = super.getJSON();
        res.put("url", this.linkInfos.getLink());
        res.put("imgSrc", this.linkInfos.getImgUrl());
        return res;
    }


    public void fillJson(JSONObject json) {
        super.fillJson(json);
        json.put("url", this.linkInfos.getLink());
        json.put("imgSrc", this.linkInfos.getImgUrl());
        json.put("type", "linkApp");
    }

    /* For sancho le robot */
    public boolean isEmpty() {
        return true;
    }

    public LinkAppInformation getLinkAppInformations() {
        return this.linkInfos;
    }

    @Override
    public void modifyShared(ServletManager sm, JSONObject editJson) throws GeneralException {
        this.getHolder().modifyShareable(sm, editJson, this);
    }

    @Override
    public void deleteShared(ServletManager sm) throws GeneralException {
        throw new GeneralException(ServletManager.Code.ClientError, "You can't delete this app");
    }

    @Override
    public void modifyShareable(ServletManager sm, JSONObject editJson, SharedApp sharedApp) throws GeneralException {
        this.getLinkAppInformations().edit(editJson, sm);
    }

    @Override
    public void deleteShareable(ServletManager sm, SharedApp sharedApp) throws GeneralException {
        this.removeFromDB(sm);
    }

    @Override
    public SharedApp share(TeamUser teamUser_owner, TeamUser teamUser_tenant, Channel channel, Team team, JSONObject params, PostServletManager sm) throws GeneralException, HttpServletException {
        if (this.tenant_teamUsers.contains(teamUser_tenant))
            throw new GeneralException(ServletManager.Code.ClientWarning, "App already shared with this teamUser");
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<>();
        String appDBid = App.createSharedApp(null, null, this.getName(), "linkApp", elevator, false, true, team.getDb_id(), (channel == null) ? null : channel.getDb_id(), teamUser_tenant.getDb_id(), this, true, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO linkApps values(NULL, ?, ?, NULL);");
        request.setInt(appDBid);
        request.setInt(this.linkInfos.getDb_id());
        String linkDBid = request.set().toString();
        db.commitTransaction(transaction);
        this.tenant_teamUsers.add(teamUser_tenant);
        this.channel = channel;
        SharedApp sharedApp = new LinkApp(appDBid, null, null, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), linkInfos, linkDBid, this);
        sharedApp.setTeamUser_tenant(teamUser_tenant);
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
	