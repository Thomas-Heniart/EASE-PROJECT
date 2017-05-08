package com.Ease.Dashboard.App;

import com.Ease.Team.Channel;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;

/**
 * Created by thomas on 03/05/2017.
 */
public interface ShareableApp {
    public SharedApp share(Integer team_user_owner_id, Integer team_user_tenant_id, ServletManager sm) throws GeneralException;
    public SharedApp share(Channel channel, ServletManager sm);
    public void modifyShareable(ServletManager sm, JSONObject editJson, SharedApp sharedApp) throws GeneralException;
    public void deleteShareable(ServletManager sm, SharedApp sharedApp) throws GeneralException;
}
