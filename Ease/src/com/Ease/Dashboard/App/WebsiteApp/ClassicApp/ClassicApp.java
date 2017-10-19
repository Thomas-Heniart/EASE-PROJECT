package com.Ease.Dashboard.App.WebsiteApp.ClassicApp;

import com.Ease.Catalog.Website;
import com.Ease.Dashboard.App.*;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

public class ClassicApp extends WebsiteApp {

    public enum Data {
        NOTHING,
        ID,
        WEBSITE_APP_ID,
        ACCOUNT_ID
    }

	/*
     *
	 * Loader And Creator
	 * 
	 */

    public static ClassicApp loadClassicApp(Integer db_id, Profile profile, Integer position, AppInformation infos, String insertDate, Website site, Integer websiteAppDBid, ServletContext context, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * from classicApps WHERE website_app_id= ?;");
        request.setInt(websiteAppDBid);
        DatabaseResult rs = request.get();
        if (rs.next()) {
            Account account = Account.loadAccount(rs.getString(Data.ACCOUNT_ID.ordinal()), db);
            Integer classicDBid = rs.getInt(Data.ID.ordinal());
            return new ClassicApp(db_id, profile, position, infos, insertDate, site, websiteAppDBid, account, classicDBid);
        }
        throw new GeneralException(ServletManager.Code.InternError, "Classic app not complete in db.");
    }

    public static ClassicApp createClassicApp(Profile profile, Integer position, String name, Website site, Map<String, String> infos, User user, DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            Map<String, Object> elevator = new HashMap<String, Object>();
            Integer websiteAppDBid = WebsiteApp.createWebsiteApp(profile, position, name, "classicApp", site, elevator, db);
            Account account = Account.createAccount(false, infos, user.getKeys().getKeyUser(), db);
            DatabaseRequest request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?);");
            request.setInt(websiteAppDBid);
            request.setInt(account.getDBid());
            Integer classicDBid = request.set();
            if (user != null) {
                for (String info : infos.values()) {
                    if (Regex.isEmail(info) == true)
                        user.addEmailIfNeeded(info, db);
                }
            }
            db.commitTransaction(transaction);
            return new ClassicApp((Integer) elevator.get("appDBid"), profile, position, (AppInformation) elevator.get("appInfos"), (String) elevator.get("insertDate"), site, websiteAppDBid, account, classicDBid);
        } catch (GeneralException e) {
            e.printStackTrace();
            throw new HttpServletException(HttpStatus.InternError);
        }
    }

    public static ClassicApp createShareableClassicApp(String name, Website website, JSONObject account_information, TeamUser teamUser_owner, Integer reminderValue, PostServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        Integer websiteAppDBid = WebsiteApp.createWebsiteApp(null, null, name, "classicApp", website, elevator, db);
        Account account = Account.createShareableAccount(account_information, teamUser_owner.getDeciphered_teamKey(), reminderValue, db);
        DatabaseRequest request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?);");
        request.setInt(websiteAppDBid);
        request.setInt(account.getDBid());
        Integer classicDBid = request.set();
        db.commitTransaction(transaction);
        return new ClassicApp((Integer) elevator.get("appDBid"), null, null, (AppInformation) elevator.get("appInfos"), (String) elevator.get("insertDate"), website, websiteAppDBid, account, classicDBid);
    }

    public static App createClassicAppSameAs(Profile profile, int position, String name, Website site, ClassicApp sameApp, ServletManager sm, User user) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Map<String, Object> elevator = new HashMap<String, Object>();
        Integer websiteAppDBid = WebsiteApp.createWebsiteApp(profile, position, name, "classicApp", site, elevator, db);
        Account account = Account.createAccountSameAs(sameApp.getAccount(), false, sm);
        DatabaseRequest request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?);");
        request.setInt(websiteAppDBid);
        request.setInt(account.getDBid());
        Integer classicDBid = request.set();
        for (AccountInformation info : account.getAccountInformations()) {
            if (info.getInformationName().equals("login")) {
                String infoValue = info.getInformationValue();
                if (Regex.isEmail(infoValue) == true)
                    user.addEmailIfNeeded(infoValue, db);
            }
        }
        db.commitTransaction(transaction);
        return new ClassicApp((Integer) elevator.get("appDBid"), profile, position, (AppInformation) elevator.get("appInfos"), (String) elevator.get("insertDate"), site, websiteAppDBid, account, classicDBid);
    }

    public static ClassicApp createFromWebsiteApp(WebsiteApp websiteApp, String name, Map<String, String> infos, ServletManager sm, User user) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        Integer websiteAppDBid = websiteApp.getWebsiteAppDBid();
        DatabaseRequest request = db.prepareRequest("UPDATE websiteApps SET type='classicApp' WHERE id= ?;");
        request.setInt(websiteAppDBid);
        request.set();
        Account account = Account.createAccount(false, infos, websiteApp.getReminderIntervalValue(), websiteApp.getReminderIntervalType(), user.getKeys().getKeyUser(), db);
        request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?);");
        request.setInt(websiteAppDBid);
        request.setInt(account.getDBid());
        Integer classicDBid = request.set();
        ClassicApp newClassicApp = new ClassicApp(websiteApp.getDBid(), user.getDashboardManager().getProfileFromApp(websiteApp.getDBid()), websiteApp.getPosition(), websiteApp.getAppInformation(), websiteApp.getInsertDate(), websiteApp.getSite(), websiteAppDBid, account, classicDBid);
        user.getDashboardManager().replaceApp(newClassicApp);
        for (String info : infos.values()) {
            if (Regex.isEmail(info) == true)
                user.addEmailIfNeeded(info, db);
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
    protected Integer classicDBid;

    /* SharedApp Interface */
    protected boolean adminHasAccess;

    public ClassicApp(Integer db_id, Profile profile, Integer position, AppInformation infos, String insertDate, Website site, Integer websiteAppDBid, Account account, Integer classicDBid) {
        super(db_id, profile, position, infos, insertDate, site, websiteAppDBid);
        this.account = account;
        this.classicDBid = classicDBid;
    }

    public ClassicApp(Integer db_id, Profile profile, Integer position, AppInformation infos, String insertDate, Website site, Integer websiteAppDBid, Account account, Integer classicDBid, ShareableApp holder) {
        super(db_id, profile, position, infos, insertDate, site, websiteAppDBid, holder, null, null);
        this.account = account;
        this.classicDBid = classicDBid;
    }

    public void removeFromDB(DataBaseConnection db) throws GeneralException, HttpServletException {
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("SELECT * from logWithApps WHERE logWith_website_app_id = ?;");
        request.setInt(this.websiteAppDBid);
        DatabaseResult rs = request.get();
        if (rs.next())
            throw new HttpServletException(HttpStatus.BadRequest, "You must delete apps using this before remove it");
        request = db.prepareRequest("DELETE FROM classicApps WHERE id = ?;");
        request.setInt(classicDBid);
        request.set();
        account.removeFromDB(db);
        super.removeFromDB(db);
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

    public void edit(String name, Map<String, String> infos, ServletManager sm) throws GeneralException, HttpServletException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        this.setName(name, sm.getDB());
        for (AccountInformation info : this.account.getAccountInformations()) {
            if (Regex.isEmail(info.getInformationValue()) == true) {
                this.getProfile().getUser().removeEmailIfNeeded(info.getInformationValue(), sm);
            }
        }
        this.account.editInfos(infos, sm);
        for (String info : infos.values()) {
            if (Regex.isEmail(info) == true)
                this.getProfile().getUser().addEmailIfNeeded(info, db);
        }
        db.commitTransaction(transaction);
    }

    public boolean isClassicApp() {
        return true;
    }

    public JSONArray getJSON(ServletManager sm) throws GeneralException, HttpServletException {
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

    public JSONObject getRestJson() {
        JSONObject res = super.getRestJson();
        res.put("account_information", this.getAccount().getRestJson());
        res.put("type", "classicApp");
        return res;
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
    public void modifyShared(DataBaseConnection db, JSONObject editJson) throws HttpServletException {
            /* int transaction = db.startTransaction();
            super.modifyShared(db, editJson);
            this.getAccount().edit(editJson, db); */
        try {
            App holder = (App) this.getHolder();
            if (holder.isClassicApp()) {
                Boolean canSeeInformation = (Boolean) editJson.get("can_see_information");
                this.setCanSeeInformation(canSeeInformation, db);
            } else if (holder.isEmpty()) {
                this.getAccount().edit(editJson, db);
            }
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public void modifyShareable(DataBaseConnection db, JSONObject editJson) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            super.modifyShareable(db, editJson);
            this.getAccount().edit(editJson, db);
            for (SharedApp app : this.sharedApps.values())
                ((ClassicApp) app).getAccount().edit(editJson, db);
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
        Boolean canSeeInformation = (Boolean) params.get("can_see_information");
        if (canSeeInformation == null)
            canSeeInformation = false;
        elevator.put("can_see_information", canSeeInformation);
        Integer websiteAppId = WebsiteApp.createSharedWebsiteApp(this, elevator, team.getDb_id(), teamUser_tenant.getDb_id(), sm);
        String deciphered_teamKey = sm.getTeamUserForTeam(team).getDeciphered_teamKey();
        this.getAccount().decipherWithTeamKeyIfNeeded(deciphered_teamKey);
        Account sharedAccount = Account.createSharedAccount(this.getAccount().getAccountInformations(), deciphered_teamKey, sm.getDB());
        DatabaseRequest request = db.prepareRequest("INSERT INTO classicApps VALUES(NULL, ?, ?, NULL);");
        request.setInt(websiteAppId);
        request.setInt(sharedAccount.getDBid());
        Integer classicDBid = request.set();
        App sharedApp = new ClassicApp((Integer) elevator.get("appDBid"), null, null, (AppInformation) elevator.get("appInfos"), (String) elevator.get("insertDate"), this.getSite(), websiteAppId, sharedAccount, classicDBid, this);
        sharedApp.setAdminHasAccess(true, sm.getDB());
        db.commitTransaction(transaction);
        sharedApp.setReceived(false);
        sharedApp.setTeamUser_tenant(teamUser_tenant);
        sharedApp.setCanSeeInformation(canSeeInformation);
        return sharedApp;
    }

    @Override
    public JSONObject getShareableJson() throws HttpServletException {
        JSONObject res = super.getShareableJson();
        res.put("type", "simple");
        res.put("password_change_interval", this.getAccount().getPasswordChangeInterval());
        res.put("last_modification", this.getAccount().printLastUpdatedDate());
        res.put("password_must_be_updated", this.getAccount().mustUpdatePassword());
        res.put("account_information", this.getAccount().getInformationJsonWithoutPassword());
        res.put("password_filled", !this.getAccount().getInformationNamed("password").equals(""));
        JSONArray jsonArray = (JSONArray) res.get("receivers");
        for (Object object : jsonArray) {
            JSONObject sharedAppObject = (JSONObject) object;
            Integer shared_app_id = (Integer) sharedAppObject.get("shared_app_id");
            App sharedApp = (App) this.getSharedAppWithId(shared_app_id);
            sharedAppObject.put("can_see_information", sharedApp.canSeeInformation());
        }
        res.put("receivers", jsonArray);
        return res;
    }

    @Override
    public JSONObject getNeededParams(PostServletManager sm) throws HttpServletException {
        JSONObject res = super.getNeededParams(sm);
        Boolean canSeeInformation = sm.getBooleanParam("can_see_information", true, true);
        if (canSeeInformation == null)
            canSeeInformation = false;
        res.put("canSeeInformation", canSeeInformation);
        return res;
    }

    @Override
    public JSONObject getSharedJSON() {
        JSONObject res = super.getSharedJSON();
        App app_holder = (App) this.getHolder();
        /* MultiApp */
        if (app_holder.isEmpty()) {
            res.put("account_information", this.getAccount().getInformationJsonWithoutPassword());
            res.put("password_filled", !this.getAccount().getInformationNamed("password").equals(""));
        }

        /* SimpleApp */
        if (app_holder.isClassicApp())
            res.put("can_see_information", this.canSeeInformation());
        return res;
    }

    public Object getSearchJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("website_name", this.website.getName());
        jsonObject.put("id", this.getDBid());
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
    public void accept(DataBaseConnection db) throws HttpServletException {
        super.accept(db);
    }

}
