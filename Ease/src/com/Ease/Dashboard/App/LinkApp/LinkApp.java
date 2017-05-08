package com.Ease.Dashboard.App.LinkApp;

import java.util.HashMap;
import java.util.Map;

import com.Ease.Dashboard.App.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

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

    public static LinkApp loadLinkApp(String appDBid, Profile profile, Integer position, String insertDate, AppInformation appInfos, GroupApp groupApp, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT * from linkApps WHERE app_id= ?;");
        request.setInt(appDBid);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            LinkAppInformation linkInfos = LinkAppInformation.loadLinkAppInformation(rs.getString(Data.LINK_APP_INFO_ID.ordinal()), db);
			/*GroupLinkApp groupLinkApp = null;
			String groupLinkId = rs.getString(Data.GROUP_LINK_APP_ID.ordinal());
			if (groupLinkId != null)
				groupLinkApp  = (GroupLinkApp) GroupManager.getGroupManager(sm).getGroupAppFromDBid(groupLinkId);*/
            IdGenerator idGenerator = (IdGenerator) sm.getContextAttr("idGenerator");
            return new LinkApp(appDBid, profile, position, appInfos, groupApp, insertDate, idGenerator.getNextId(), linkInfos, rs.getString(Data.ID.ordinal()));
        }
        throw new GeneralException(ServletManager.Code.InternError, "Link app not complete in db.");
    }

    public static LinkApp createLinkApp(Profile profile, int position, String name, String url, String imgUrl, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String appDBid = App.createApp(profile, position, name, "linkApp", elevator, sm);
        LinkAppInformation infos = LinkAppInformation.createLinkAppInformation(url, imgUrl, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO linkApps values(NULL, ?, ?, NULL);");
        request.setInt(appDBid);
        request.setInt(infos.getDb_id());
        String linkDBid = request.set().toString();
        db.commitTransaction(transaction);
        return new LinkApp(appDBid, profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("registrationDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), infos, linkDBid);
    }
	
	/*
	 * 
	 * Constructor
	 * 
	 */

    protected LinkAppInformation linkInfos;
    protected GroupLinkApp groupLinkApp;
    protected String linkAppDBid;

    public LinkApp(String db_id, Profile profile, int position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, LinkAppInformation linkInfos, String linkAppDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id);
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
        if (this.groupApp == null || this.groupApp.isCommon() == false)
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

    }

    @Override
    public ShareableApp getHolder() {
        return this.holder;
    }

    @Override
    public void deleteShared(ServletManager sm) throws GeneralException {

    }

    @Override
    public SharedApp share(Integer team_user_owner_id, Integer team_user_tenant_id, ServletManager sm) throws GeneralException {
        return null;
    }

    @Override
    public void modifyShareable(ServletManager sm, JSONObject editJson, SharedApp sharedApp) throws GeneralException {

    }

    @Override
    public void deleteShareable(ServletManager sm, SharedApp sharedApp) throws GeneralException {

    }
}
	