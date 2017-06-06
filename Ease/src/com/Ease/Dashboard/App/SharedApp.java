package com.Ease.Dashboard.App;

import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;

/**
 * Created by thomas on 03/05/2017.
 */
public interface SharedApp {
    public void modifyShared(ServletManager sm, JSONObject editJson) throws GeneralException;

    public ShareableApp getHolder();

    public void setHolder(ShareableApp shareableApp);

    public void deleteShared(DataBaseConnection db) throws HttpServletException;

    public void setTeamUser_tenant(TeamUser teamUserWithId);

    public TeamUser getTeamUser_tenant();

    public JSONObject getSharedJSON();

    /* Only for ClassicApp, Exception else */

    public void setAdminHasAccess(boolean b);

    public void setAdminHasAccess(boolean b, DataBaseConnection db) throws GeneralException;

    public boolean adminHasAccess();
}
