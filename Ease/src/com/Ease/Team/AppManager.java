package com.Ease.Team;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 13/06/2017.
 */
public class AppManager {
    protected List<ShareableApp> shareableApps = new LinkedList<>();

    protected Map<Integer, ShareableApp> shareableAppMap = new HashMap<>();

    protected List<SharedApp> sharedApps = new LinkedList<>();

    protected Map<Integer, SharedApp> sharedAppMap = new HashMap<>();

    protected Map<TeamUser, List<SharedApp>> teamUserAndSharedAppMap = new HashMap<>();

    public AppManager() {

    }

    /* ======================================= */
    /* ======= Part about shared apps ======== */
    /* ======================================= */

    public void addSharedApp(SharedApp sharedApp) {
        this.sharedApps.add(sharedApp);
        this.sharedAppMap.put(Integer.valueOf(((App) sharedApp).getDBid()), sharedApp);
        TeamUser teamUser_tenant = sharedApp.getTeamUser_tenant();
        List<SharedApp> sharedAppList = this.teamUserAndSharedAppMap.get(teamUser_tenant);
        if (sharedAppList == null)
            sharedAppList = new LinkedList<>();
        sharedAppList.add(sharedApp);
        this.teamUserAndSharedAppMap.put(teamUser_tenant, sharedAppList);
    }

    public SharedApp getSharedApp(Integer sharedApp_id) throws HttpServletException {
        SharedApp sharedApp = this.sharedAppMap.get(sharedApp_id);
        if (sharedApp == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No shared app with this id.");
        return sharedApp;
    }

    public void removeSharedApp(SharedApp sharedApp) {
        this.sharedApps.remove(sharedApp);
        this.sharedAppMap.remove(Integer.valueOf(((App) sharedApp).getDBid()));
        this.teamUserAndSharedAppMap.get(sharedApp.getTeamUser_tenant()).remove(sharedApp);
    }

    public void removeSharedAppsForTeamUser(TeamUser teamUser, DataBaseConnection db) throws HttpServletException {
        List<SharedApp> sharedApps = this.getSharedAppsForTeamUser(teamUser);
        this.removeSharedApps(sharedApps, db);
    }

    public void removeSharedApps(List<SharedApp> sharedApps, DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            for (SharedApp sharedApp : sharedApps) {
                this.removeSharedApp(sharedApp);
                sharedApp.deleteShared(db);
            }
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public List<SharedApp> getSharedAppsForTeamUser(TeamUser teamUser) {
        return this.teamUserAndSharedAppMap.get(teamUser);
    }

    public void setSharedApps(List<SharedApp> sharedApps) {
        for (SharedApp sharedApp : sharedApps)
            this.addSharedApp(sharedApp);
    }

    /* ======================================= */
    /* ===== Part about shareable apps ======= */
    /* ======================================= */

    public List<ShareableApp> getShareableApps() {
        return shareableApps;
    }

    public void setShareableApps(List<ShareableApp> shareableApps) {
        for (ShareableApp shareableApp : shareableApps)
            this.addShareableApp(shareableApp);
    }

    public void addShareableApp(ShareableApp shareableApp) {
        this.shareableApps.add(shareableApp);
        this.shareableAppMap.put(Integer.valueOf(((App) shareableApp).getDBid()), shareableApp);
    }

    public ShareableApp getShareableAppWithId(Integer db_id) throws HttpServletException {
        ShareableApp shareableApp = this.shareableAppMap.get(db_id);
        if (shareableApp == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This shareable app does not exist.");
        return shareableApp;
    }

    public void removeShareableApp(ShareableApp shareableApp) {
        this.shareableApps.remove(shareableApp);
        this.shareableAppMap.remove(Integer.valueOf(((App) shareableApp).getDBid()));
    }

    public void removeShareableApp(ShareableApp shareableApp, DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            this.removeSharedApps(shareableApp.getSharedApps(), db);
            this.removeShareableApp(shareableApp);
            shareableApp.deleteShareable(db);
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }

    public void removeShareableApps(List<ShareableApp> shareableApps, DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            for (ShareableApp shareableApp : shareableApps)
                this.removeShareableApp(shareableApp, db);
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }
}
