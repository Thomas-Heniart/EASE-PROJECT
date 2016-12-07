package com.Ease.Context.Group;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.LinkAppInformation;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class GroupLinkApp extends GroupApp {
	
	public static GroupLinkApp loadGroupLinkApp(String db_id, Group group, GroupProfile groupProfile, AppPermissions permissions, AppInformation informations, boolean common, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT link_app_info_id FROM groupLinkApps WHERE group_app_id = " + db_id + ";");
		try {
			rs.next();
			LinkAppInformation linkAppInformations = LinkAppInformation.loadLinkAppInformation(db_id, sm);
			return new GroupLinkApp(db_id, groupProfile, group, permissions, informations, linkAppInformations, common);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	protected LinkAppInformation link_app_informations;

	public GroupLinkApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions permissions, AppInformation app_informations, LinkAppInformation link_app_informations, boolean common) {
		super(db_id, groupProfile, group, permissions, app_informations, common);
		this.link_app_informations = link_app_informations;
	}

	public LinkAppInformation getLinkAppInfo() {
		return this.link_app_informations;
	}

}
