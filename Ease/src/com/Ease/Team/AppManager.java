package com.Ease.Team;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 13/06/2017.
 */
public class AppManager {
    protected Map<Integer, ShareableApp> shareableApps = new ConcurrentHashMap<>();

    protected Map<Integer, SharedApp> sharedApps = new ConcurrentHashMap<>();

    public AppManager() {

    }

    /* ======================================= */
    /* ======= Part about shared apps ======== */
    /* ======================================= */

    public Map<Integer, SharedApp> getSharedApps() {
        if (this.sharedApps == null)
            sharedApps = new ConcurrentHashMap<>();
        return sharedApps;
    }

    public void addSharedApp(SharedApp sharedApp) {
        App app = (App) sharedApp;
        this.sharedApps.put(app.getDBid(), sharedApp);
    }

    public void addSharedApps(Collection<SharedApp> sharedApps) {
        for (SharedApp sharedApp : sharedApps)
            this.addSharedApp(sharedApp);
    }

    public SharedApp getSharedApp(Integer sharedApp_id) throws HttpServletException {
        SharedApp sharedApp = this.sharedApps.get(sharedApp_id);
        if (sharedApp == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No shared app with this id.");
        return sharedApp;
    }

    public void removeSharedApp(SharedApp sharedApp) {
        App app = (App) sharedApp;
        this.sharedApps.remove(app.getDBid());
    }

    public void removeSharedAppsForTeamUser(TeamUser teamUser, DataBaseConnection db) throws HttpServletException {
        List<SharedApp> sharedApps = this.getSharedAppsForTeamUser(teamUser);
        this.removeSharedApps(sharedApps, db);
    }

    public void removeSharedApps(List<SharedApp> sharedApps, DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            for (SharedApp sharedApp : sharedApps) {
                sharedApp.deleteShared(db);
                ShareableApp shareableApp = sharedApp.getHolder();
                shareableApp.removeSharedApp(sharedApp);
            }
            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public List<SharedApp> getSharedAppsForTeamUser(TeamUser teamUser) {
        List<SharedApp> sharedApps = new LinkedList<>();
        for (SharedApp sharedApp : this.getSharedApps().values()) {
            if (sharedApp.getTeamUser_tenant() == teamUser)
                sharedApps.add(sharedApp);
        }
        return sharedApps;
    }

    public void setSharedApps(List<SharedApp> sharedApps) {
        for (SharedApp sharedApp : sharedApps)
            this.addSharedApp(sharedApp);
    }

    /* ======================================= */
    /* ===== Part about shareable apps ======= */
    /* ======================================= */

    public synchronized Map<Integer, ShareableApp> getShareableApps() {
        if (shareableApps == null)
            shareableApps = new ConcurrentHashMap<>();
        return shareableApps;
    }

    public void setShareableApps(List<ShareableApp> shareableApps) {
        for (ShareableApp shareableApp : shareableApps)
            this.addShareableApp(shareableApp);
    }

    public void addShareableApp(ShareableApp shareableApp) {
        App app = (App) shareableApp;
        this.shareableApps.put(app.getDBid(), shareableApp);
    }

    public ShareableApp getShareableAppWithId(Integer db_id) throws HttpServletException {
        ShareableApp shareableApp = this.shareableApps.get(db_id);
        if (shareableApp == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This shareable app does not exist.");
        return shareableApp;
    }

    public void removeShareableApp(ShareableApp shareableApp) {
        App app = (App) shareableApp;
        this.shareableApps.remove(app.getDBid());
    }

    public void removeShareableApp(ShareableApp shareableApp, DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            for (Integer id : shareableApp.getSharedApps().keySet())
                this.getSharedApps().remove(id);
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

    public void removeSharedAppsForTeamUserInChannel(TeamUser teamUser_to_remove, Channel channel, DataBaseConnection db) throws HttpServletException {
        List<SharedApp> sharedApps = this.getSharedAppsForTeamUser(teamUser_to_remove);
        List<SharedApp> sharedAppsToRemove = new LinkedList<>();
        for (SharedApp sharedApp : sharedApps) {
            if (sharedApp.getHolder().getChannel() == channel)
                sharedAppsToRemove.add(sharedApp);
        }
        for (SharedApp sharedApp : sharedAppsToRemove) {
            ShareableApp holder = sharedApp.getHolder();
            this.removeSharedApp(sharedApp);
            holder.removeSharedApp(sharedApp);
            sharedApp.deleteShared(db);
        }
    }
}
