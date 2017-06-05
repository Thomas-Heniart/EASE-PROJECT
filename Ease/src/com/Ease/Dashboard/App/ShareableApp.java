package com.Ease.Dashboard.App;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * Created by thomas on 03/05/2017.
 */
public interface ShareableApp {
    public SharedApp share(TeamUser teamUser_owner, TeamUser teamUser_tenant, Channel channel, Team team, JSONObject params, PostServletManager sm) throws GeneralException, HttpServletException;

    public void modifyShareable(ServletManager sm, JSONObject editJson, SharedApp sharedApp) throws GeneralException;

    public void deleteShareable(ServletManager sm, SharedApp sharedApp) throws GeneralException;

    public TeamUser getTeamUser_owner();

    public List<TeamUser> getTeamUser_tenants();

    public List<SharedApp> getSharedApps();

    public void setTeamUser_owner(TeamUser teamUser_owner);

    public Channel getChannel();

    public void setChannel(Channel channel);

    public void addSharedApp(SharedApp app);

    public void setSharedApps(List<SharedApp> sharedApps);

    public SharedApp getSharedAppWithId(Integer sharedApp_id) throws HttpServletException;

    public JSONObject getShareableJson() throws GeneralException;

    public JSONObject getNeededParams(PostServletManager sm) throws GeneralException;

    public void setDescription(String description);

    public String getDescription();
}
