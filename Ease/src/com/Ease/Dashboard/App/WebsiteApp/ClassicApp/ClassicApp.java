package com.Ease.Dashboard.App.WebsiteApp.ClassicApp;

import java.util.HashMap;
import java.util.Map;

import com.Ease.Dashboard.App.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

import javax.servlet.ServletContext;

public class ClassicApp extends WebsiteApp {

    public enum Data {
        NOTHING,
        ID,
        WEBSITE_APP_ID,
        ACCOUNT_ID,
        GROUP_CLASSIC_APP_ID
    }

	/*
     *
	 * Loader And Creator
	 * 
	 */

    public static ClassicApp loadClassicApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, Website site, String websiteAppDBid, ServletContext context, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * from classicApps WHERE website_app_id= ?;");
        request.setInt(websiteAppDBid);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            Account account = Account.loadAccount(rs.getString(Data.ACCOUNT_ID.ordinal()), db);
            String classicDBid = rs.getString(Data.ID.ordinal());
            IdGenerator idGenerator = (IdGenerator) context.getAttribute("idGenerator");
            return new ClassicApp(db_id, profile, position, infos, groupApp, insertDate, idGenerator.getNextId(), site, websiteAppDBid, account, classicDBid);
        }
        throw new GeneralException(ServletManager.Code.InternError, "Classic app not complete in db.");
    }

    public static ClassicApp createClassicApp(Profile profile, Integer position, String name, Website site, Map<String, String> infos, ServletManager sm, User user) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String websiteAppDBid = WebsiteApp.createWebsiteApp(profile, position, name, "classicApp", site, elevator, sm);
        Account account = Account.createAccount(false, infos, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppDBid);
        request.setInt(account.getDBid());
        String classicDBid = request.set().toString();
        if (user != null) {
            for (String info : infos.values()) {
                if (Regex.isEmail(info) == true) {
                    user.addEmailIfNeeded(info, sm);
                }
            }
        }
        db.commitTransaction(transaction);
        return new ClassicApp((String) elevator.get("appDBid"), profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("registrationDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), site, websiteAppDBid, account, classicDBid);
    }

    public static App createClassicAppSameAs(Profile profile, int position, String name, Website site, ClassicApp sameApp, ServletManager sm, User user) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String websiteAppDBid = WebsiteApp.createWebsiteApp(profile, position, name, "classicApp", site, elevator, sm);
        Account account = Account.createAccountSameAs(sameApp.getAccount(), false, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppDBid);
        request.setInt(account.getDBid());
        String classicDBid = request.set().toString();
        for (AccountInformation info : account.getAccountInformations()) {
            if (info.getInformationName().equals("login")) {
                String infoValue = info.getInformationValue();
                if (Regex.isEmail(infoValue) == true)
                    user.addEmailIfNeeded(infoValue, sm);
            }
        }
        db.commitTransaction(transaction);
        return new ClassicApp((String) elevator.get("appDBid"), profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("registrationDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), site, websiteAppDBid, account, classicDBid);
    }

    public static ClassicApp createFromWebsiteApp(WebsiteApp websiteApp, String name, Map<String, String> infos, ServletManager sm, User user) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        String websiteAppDBid = websiteApp.getWebsiteAppDBid();
        DatabaseRequest request = db.prepareRequest("UPDATE websiteApps SET type='classicApp' WHERE id= ?;");
        request.setInt(websiteAppDBid);
        request.set();
        Account account = Account.createAccount(false, infos, websiteApp.getReminderIntervalValue(), websiteApp.getReminderIntervalType(), sm);
        request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppDBid);
        request.setInt(account.getDBid());
        String classicDBid = request.set().toString();
        ClassicApp newClassicApp = new ClassicApp(websiteApp.getDBid(), user.getDashboardManager().getProfileFromApp(websiteApp.getSingleId()), websiteApp.getPosition(), websiteApp.getAppInformation(), null, websiteApp.getInsertDate(), websiteApp.getSingleId(), websiteApp.getSite(), websiteAppDBid, account, classicDBid);
        user.getDashboardManager().replaceApp(newClassicApp);
        for (String info : infos.values()) {
            if (Regex.isEmail(info) == true) {
                user.addEmailIfNeeded(info, sm);
            }
        }
        db.commitTransaction(transaction);
        return newClassicApp;
    }

	
	/*
     *
	 * Constructor
	 * 
	 */

    protected Account account;
    protected String classicDBid;

    public ClassicApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid, Account account, String classicDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id, site, websiteAppDBid);
        this.account = account;
        this.classicDBid = classicDBid;
    }

    public ClassicApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid, Account account, String classicDBid, boolean shareable, boolean shared, ShareableApp holder) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id, site, websiteAppDBid, shareable, shared, holder);
        this.account = account;
        this.classicDBid = classicDBid;
    }

    public void removeFromDB(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM classicApps WHERE id = ?;");
        request.setInt(classicDBid);
        request.set();
        if ((this.groupApp == null || this.groupApp.isCommon() == false) && (this.getHolder() == null || this.getAccount() != ((ClassicApp) this.getHolder()).getAccount()))
            account.removeFromDB(sm);
        super.removeFromDB(sm);
        this.website.decrementRatio(db);
        db.commitTransaction(transaction);
    }

    public Account getAccount() {
        return account;
    }
	/*
	 * 
	 * Getter And Setter
	 *
	 */
	
	
	
	/*
	 * 
	 * Utils
	 * 
	 */

    public void edit(String name, Map<String, String> infos, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        this.setName(name, sm);
        for (AccountInformation info : this.account.getAccountInformations()) {
            if (Regex.isEmail(info.getInformationValue()) == true) {
                this.getProfile().getUser().removeEmailIfNeeded(info.getInformationValue(), sm);
            }
        }
        if (this.groupApp == null || (!this.groupApp.isCommon() && this.groupApp.getPerms().havePermission(AppPermissions.Perm.EDIT.ordinal()))) {
            this.account.editInfos(infos, sm);
			/*if (password != null && !password.equals(""))
				this.account.setPassword(password, this.getProfile().getUser(), sm);*/
        }
        for (String info : infos.values()) {
            if (Regex.isEmail(info) == true) {
                this.getProfile().getUser().addEmailIfNeeded(info, sm);
            }
        }
        db.commitTransaction(transaction);
    }

    public boolean isClassicApp() {
        return true;
    }

    public JSONArray getJSON(ServletManager sm) throws GeneralException {
        JSONArray infos = super.getJSON(sm);
        JSONObject websiteInfos = (JSONObject) infos.get(0);
        websiteInfos.put("user", this.account.getJSON(sm));
        websiteInfos.put("type", "ClassicApp");
        websiteInfos.put("app_name", this.informations.getName());
        websiteInfos.put("website_name", this.website.getName());
        return infos;
    }

    public JSONObject getJSON() {
        JSONObject res = super.getJSON();
        res.put("account_information", this.account.getJSON());
        return res;
    }

    public void fillJson(JSONObject json) {
        super.fillJson(json);
        json.put("accountInformations", this.account.getJSON());
        json.put("login", this.account.getInformationNamed("login"));
        json.put("type", "classicApp");
        json.put("website_name", this.website.getName());
    }

    public void setPassword(String password, ServletManager sm) throws GeneralException {
        this.account.setPassword(password, this.getProfile().getUser(), sm);
    }

    /* For sancho le robot */
    public boolean isEmpty() {
        return false;
    }

    public JSONArray getInformationWithoutPasswordJson() {
        return this.account.getInformationWithoutPasswordJson();
    }

    @Override
    public void modifyShared(ServletManager sm, JSONObject editJson) throws GeneralException {
        if (!this.havePerm(AppPermissions.Perm.EDIT))
            throw new GeneralException(ServletManager.Code.ClientError, "You cannot edit this app");
        this.getHolder().modifyShareable(sm, editJson, this);
    }

    @Override
    public void deleteShared(ServletManager sm) throws GeneralException {
        if (((ClassicApp) this.getHolder()).getAccount() == this.getAccount())
            throw new GeneralException(ServletManager.Code.ClientError, "You can't delete this app.");
        this.removeFromDB(sm);
    }

    @Override
    public void modifyShareable(ServletManager sm, JSONObject editJson, SharedApp sharedApp) throws GeneralException {
        this.getAccount().edit(editJson, sm);
        for (SharedApp app : this.sharedApps)
            ((ClassicApp) app).getAccount().edit(editJson, sm);
    }

    @Override
    public void deleteShareable(ServletManager sm, SharedApp sharedApp) throws GeneralException {
        for (SharedApp sharedApp1 : this.sharedApps)
            sharedApp1.deleteShared(sm);
        this.removeFromDB(sm);
    }

    @Override
    public SharedApp share(TeamUser teamUser_owner, TeamUser teamUser_tenant, Channel channel, Team team, JSONObject params, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<>();
        String websiteAppId = WebsiteApp.createSharedWebsiteApp(this, elevator, team.getDb_id(), channel == null ? null : channel.getDb_id(), teamUser_tenant.getDb_id(), sm);
        JSONArray accountInformationArray = (JSONArray) params.get("accountInformation");
        if (accountInformationArray == null || accountInformationArray.isEmpty())
            throw new GeneralException(ServletManager.Code.ClientError, "Account information shouldn't be empty or null");
        Map<String, String> accountInformationMap = new HashMap<>();
        for (Object accountInformationObj : accountInformationArray) {
            JSONObject accountInformation = (JSONObject) accountInformationObj;
            String info_name = (String) accountInformation.get("info_name");
            String info_value = (String) accountInformation.get("info_value");
            accountInformationMap.put(info_name, info_value);
        }
        Account account = Account.createSharedAccount(accountInformationMap, teamUser_owner.getDeciphered_teamKey(), sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppId);
        request.setInt(account.getDBid());
        String classicDBid = request.set().toString();
        db.commitTransaction(transaction);
        App sharedApp = new ClassicApp((String) elevator.get("appDBid"), null, null, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("registrationDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), this.getSite(), websiteAppId, this.account, classicDBid, false, true, this);
        sharedApp.setReceived(false);
        return sharedApp;
    }

    @Override
    public JSONObject getShareableJson() throws GeneralException {
        JSONObject res = super.getShareableJson();
        res.put("type", "simple");
        return res;
    }

    @Override
    public JSONObject getSharedJSON() {
        JSONObject res = super.getSharedJSON();
        JSONArray information = this.account.getInformationWithoutPasswordJson();
        res.put("information", information);
        return res;
    }

}
