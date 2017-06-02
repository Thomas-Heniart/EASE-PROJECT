package com.Ease.Dashboard.App.WebsiteApp.ClassicApp;

import java.util.HashMap;
import java.util.Map;

import com.Ease.Dashboard.App.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;

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
        String websiteAppDBid = WebsiteApp.createWebsiteApp(profile, position, name, "classicApp", site, elevator, db);
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
        return new ClassicApp((String) elevator.get("appDBid"), profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), site, websiteAppDBid, account, classicDBid);
    }

    public static ClassicApp createShareableClassicApp(String name, Website website, Map<String, String> accountInformationMap, TeamUser teamUser_owner, Integer reminderValue, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String websiteAppDBid = WebsiteApp.createWebsiteApp(null, null, name, "classicApp", website, elevator, db);
        Account account = Account.createShareableAccount(accountInformationMap, teamUser_owner.getDeciphered_teamKey(), reminderValue, db);
        DatabaseRequest request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppDBid);
        request.setInt(account.getDBid());
        String classicDBid = request.set().toString();
        db.commitTransaction(transaction);
        return new ClassicApp((String) elevator.get("appDBid"), null, null, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), website, websiteAppDBid, account, classicDBid);
    }

    public static App createClassicAppSameAs(Profile profile, int position, String name, Website site, ClassicApp sameApp, ServletManager sm, User user) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        String websiteAppDBid = WebsiteApp.createWebsiteApp(profile, position, name, "classicApp", site, elevator, db);
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
        return new ClassicApp((String) elevator.get("appDBid"), profile, position, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), site, websiteAppDBid, account, classicDBid);
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

    /* SharedApp Interface */
    protected boolean adminHasAccess;

    public ClassicApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid, Account account, String classicDBid) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id, site, websiteAppDBid);
        this.account = account;
        this.classicDBid = classicDBid;
    }

    public ClassicApp(String db_id, Profile profile, Integer position, AppInformation infos, GroupApp groupApp, String insertDate, int single_id, Website site, String websiteAppDBid, Account account, String classicDBid, ShareableApp holder) {
        super(db_id, profile, position, infos, groupApp, insertDate, single_id, site, websiteAppDBid, holder, null, null);
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
        if (((App) this.getHolder()).isClassicApp())
            this.getHolder().modifyShareable(sm, editJson, this);
        else
            this.getAccount().edit(editJson, sm);
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
    public SharedApp share(TeamUser teamUser_owner, TeamUser teamUser_tenant, Channel channel, Team team, JSONObject params, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<>();
        String websiteAppId = WebsiteApp.createSharedWebsiteApp(this, elevator, team.getDb_id(), channel == null ? null : channel.getDb_id(), teamUser_tenant.getDb_id(), sm);
        String deciphered_teamKey = sm.getTeamUserForTeam(team).getDeciphered_teamKey();
        this.getAccount().decipherWithTeamKeyIfNeeded(deciphered_teamKey);
        Account sharedAccount = Account.createSharedAccount(this.getAccount().getAccountInformations(), deciphered_teamKey, sm.getDB());
        DatabaseRequest request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppId);
        request.setInt(sharedAccount.getDBid());
        String classicDBid = request.set().toString();
        db.commitTransaction(transaction);
        App sharedApp = new ClassicApp((String) elevator.get("appDBid"), null, null, (AppInformation) elevator.get("appInfos"), null, (String) elevator.get("insertDate"), ((IdGenerator) sm.getContextAttr("idGenerator")).getNextId(), this.getSite(), websiteAppId, sharedAccount, classicDBid, this);
        sharedApp.setAdminHasAccess(adminHasAccess, sm.getDB());
        sharedApp.setReceived(false);
        sharedApp.setTeamUser_tenant(teamUser_tenant);
        return sharedApp;
    }

    @Override
    public JSONObject getShareableJson() throws GeneralException {
        JSONObject res = super.getShareableJson();
        res.put("type", "simple");
        res.put("password_change_interval", this.getAccount().getPasswordChangeInterval());
        res.put("last_modification", this.getAccount().getLastUpdatedDate());
        JSONArray accountInformationArray = new JSONArray();
        for (AccountInformation accountInformation : this.getAccount().getAccountInformations()) {
            JSONObject tmp = new JSONObject();
            tmp.put("info_name", accountInformation.getInformationName());
            tmp.put("info_value", accountInformation.getInformationValue());
            accountInformationArray.add(tmp);
        }
        res.put("account_information", accountInformationArray);
        return res;
    }

    @Override
    public JSONObject getNeededParams(ServletManager sm) throws GeneralException {
        return new JSONObject();
    }

    @Override
    public JSONObject getSharedJSON() {
        JSONObject res = super.getSharedJSON();
        JSONArray information = this.account.getInformationJson();
        res.put("information", information);
        return res;
    }

    public Object getSearchJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("website_name", this.website.getName());
        jsonObject.put("id", this.single_id);
        jsonObject.put("logo", this.website.getLogo());
        jsonObject.put("profile_name", this.profile.getName());
        jsonObject.put("login", this.getAccount().getInformationNamed("login"));
        return jsonObject;
    }

    public JSONObject getJsonWithoutId() {
        JSONObject jsonObject = super.getJsonWithoutId();
        jsonObject.put("information", this.account.getInformationJson());
        return jsonObject;
    }

    @Override
    public boolean adminHasAccess() {
        return this.adminHasAccess;
    }

    @Override
    public void setAdminHasAccess(boolean b) {
        this.adminHasAccess = b;
    }

    @Override
    public void setAdminHasAccess(boolean b, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("UPDATE sharedApps SET adminHasAccess = ? WHERE id = ?");
        request.setBoolean(b);
        request.setInt(this.getDBid());
        request.set();
        this.setAdminHasAccess(b);
    }

}
