package com.Ease.Context.Group;

import com.Ease.Dashboard.App.AppInformation;
import com.Ease.Dashboard.App.LinkAppInformation;

public class GroupLinkApp extends GroupApp {

	protected LinkAppInformation link_app_informations;

	public GroupLinkApp(String db_id, GroupProfile groupProfile, Group group, AppPermissions permissions, String type,
			AppInformation app_informations, LinkAppInformation link_app_informations, boolean common) {
		super(db_id, groupProfile, group, permissions, type, app_informations, common);
		this.link_app_informations = link_app_informations;
	}

	public LinkAppInformation getLinkAppInfo() {
		return this.link_app_informations;
	}

}
