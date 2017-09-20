package com.Ease.Dashboard.App;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.List;

/**
 * Created by thomas on 03/05/2017.
 */
public interface ShareableApp {
    public SharedApp share(TeamUser teamUser_tenant, Team team, JSONObject params, PostServletManager sm) throws GeneralException, HttpServletException;

    public void modifyShareable(DataBaseConnection db, JSONObject editJson) throws HttpServletException;

    public void deleteShareable(DataBaseConnection db) throws HttpServletException;

    public List<TeamUser> getTeamUser_tenants();

    public Collection<SharedApp> getSharedApps();

    public Channel getChannel();

    public void setChannel(Channel channel);

    public void addSharedApp(SharedApp app);

    public void setSharedApps(List<SharedApp> sharedApps);

    public SharedApp getSharedAppWithId(Integer sharedApp_id) throws HttpServletException;

    public JSONObject getShareableJson() throws HttpServletException;

    public JSONObject getNeededParams(PostServletManager sm) throws HttpServletException;

    public void setDescription(String description);

    public void setDescription(String description, DataBaseConnection db) throws HttpServletException;

    public String getDescription();

    public void setOrigin(String origin_type, Integer origin_id, Integer team_id);

    public JSONObject getOrigin();

    public void removeSharedApp(SharedApp sharedApp);

    public void addPendingTeamUser(TeamUser teamUser);

    public void addPendingTeamUser(TeamUser teamUser, DataBaseConnection db) throws HttpServletException;

    public Collection<TeamUser> getPendingTeamUsers();

    public void removePendingTeamUser(TeamUser teamUser, DataBaseConnection db) throws HttpServletException;
}
