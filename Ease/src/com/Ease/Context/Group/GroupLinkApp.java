package com.Ease.Context.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.LinkAppInformation;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class GroupLinkApp extends GroupApp {

	protected LinkAppInformation link_app_informations;

	public GroupLinkApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions permissions, String type,
			AppInformation app_informations, LinkAppInformation link_app_informations, boolean common) {
		super(db_id, groupProfile, group, permissions, type, app_informations, common);
		this.link_app_informations = link_app_informations;
	}
	
	public GroupLinkApp(String db_id, ServletManager sm) throws GeneralException {
		super (db_id, sm);
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT link_app_info_id FROM groupLinkApps WHERE group_app_id = " + db_id  + ";");
		try {
			rs.next();
			this.link_app_informations = LinkAppInformation.loadLinkAppInformation(rs.getString(1), sm);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
	}

	public LinkAppInformation getLinkAppInfo() {
		return this.link_app_informations;
	}

}
