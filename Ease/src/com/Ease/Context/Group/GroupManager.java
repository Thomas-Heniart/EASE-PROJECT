package com.Ease.Context.Group;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Dashboard.App.GroupApp;
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class GroupManager {
	
	
	public static GroupManager getGroupManager(ServletContext context) {
		return ((GroupManager)context.getAttribute("groupManager"));
	}
	public static GroupManager getGroupManager(ServletManager sm) {
		return ((GroupManager)sm.getContextAttr("groupManager"));
	}
	
	protected List<Infrastructure> infras;
	
	protected Map<String, Group> DBmapGroup;
	protected Map<Integer, Group> IDmapGroup;
	
	protected Map<String, Infrastructure> DBmapInfra;
	protected Map<Integer, Infrastructure> IDmapInfra;
	
	protected Map<String, GroupProfile> DBmapGroupProfile;
	protected Map<Integer, GroupProfile> IDmapGroupProfile;
	
	protected Map<String, GroupApp> DBmapGroupApp;
	protected Map<Integer, GroupApp> IDmapGroupApp;
	
	public GroupManager() {
		this.infras = new LinkedList<Infrastructure>();
		
		this.DBmapGroup = new HashMap<String, Group>();
		this.IDmapGroup = new HashMap<Integer, Group>();
		
		this.DBmapInfra = new HashMap<String, Infrastructure>();
		this.IDmapInfra = new HashMap<Integer, Infrastructure>();
		
		this.DBmapGroupProfile = new HashMap<String, GroupProfile>();
		this.IDmapGroupProfile = new HashMap<Integer, GroupProfile>();
		
		this.DBmapGroupApp = new HashMap<String, GroupApp>();
		this.IDmapGroupApp = new HashMap<Integer, GroupApp>();
	}
	
	public void add(Group group) {
		DBmapGroup.put(group.getDBid(), group);
		IDmapGroup.put(group.getSingleId(), group);
		group.getInfra().addGroup(group);
	}
	public void add(Infrastructure infra) {
		DBmapInfra.put(infra.getDBid(), infra);
		IDmapInfra.put(infra.getSingleId(), infra);
		infras.add(infra);
	}
	public void add(GroupProfile groupProfile) {
		DBmapGroupProfile.put(groupProfile.getDBid(), groupProfile);
		IDmapGroupProfile.put(groupProfile.getSingleId(), groupProfile);
	}
	public void add(GroupApp groupApp) {
		DBmapGroupApp.put(groupApp.getDBid(), groupApp);
		IDmapGroupApp.put(groupApp.getSingleId(), groupApp);
	}
	
	public void remove(Group group) {
		DBmapGroup.remove(group.getDBid());
		IDmapGroup.remove(group.getSingleId());
	}
	public void remove(Infrastructure infra) {
		infras.remove(infra);
		DBmapInfra.remove(infra.getDBid());
		IDmapInfra.remove(infra.getSingleId());
	}
	public void remove(GroupProfile groupProfile) {
		DBmapGroupProfile.remove(groupProfile.getDBid());
		IDmapGroupProfile.remove(groupProfile.getSingleId());
	}
	public void remove(GroupApp groupApp) {
		DBmapGroupApp.remove(groupApp.getDBid());
		IDmapGroupApp.remove(groupApp.getSingleId());
	}
	
	public Group getGroupFromDBid(String db_id) throws GeneralException {
		Group group;
		if ((group = DBmapGroup.get(db_id)) == null)
			throw new GeneralException(ServletManager.Code.InternError, "This group dosen't exist.");
		return group;
	}
	public Group getGroupFromSingleID(Integer single_id) throws GeneralException {
		Group group;
		if ((group = IDmapGroup.get(single_id)) == null)
			throw new GeneralException(ServletManager.Code.InternError, "This group dosen't exist.");
		return group;
	}
	
	public Infrastructure getInfraFromDBid(String db_id) throws GeneralException {
		Infrastructure infra;
		if ((infra = DBmapInfra.get(db_id)) == null)
			throw new GeneralException(ServletManager.Code.InternError, "This infra dosen't exist.");
		return infra;
	}
	public Infrastructure getInfraFromSingleID(Integer single_id) throws GeneralException {
		Infrastructure infra;
		if ((infra = IDmapInfra.get(single_id)) == null)
			throw new GeneralException(ServletManager.Code.InternError, "This infra dosen't exist.");
		return infra;
	}
	
	public GroupProfile getGroupProfileFromDBid(String db_id) throws GeneralException {
		GroupProfile groupProfile;
		if ((groupProfile = DBmapGroupProfile.get(db_id)) == null)
			throw new GeneralException(ServletManager.Code.InternError, "This groupProfile dosen't exist.");
		return groupProfile;
	}
	public GroupProfile getGroupProfileFromSingleID(Integer single_id) throws GeneralException {
		GroupProfile groupProfile;
		if ((groupProfile = IDmapGroupProfile.get(single_id)) == null)
			throw new GeneralException(ServletManager.Code.InternError, "This groupProfile dosen't exist.");
		return groupProfile;
	}
	
	public GroupApp getGroupAppFromDBid(String db_id) throws GeneralException {
		GroupApp groupApp;
		if ((groupApp = DBmapGroupApp.get(db_id)) == null)
			throw new GeneralException(ServletManager.Code.InternError, "This groupApp dosen't exist.");
		return groupApp;
	}
	public GroupApp getGroupAppFromSingleID(Integer single_id) throws GeneralException {
		GroupApp groupApp;
		if ((groupApp = IDmapGroupApp.get(single_id)) == null)
			throw new GeneralException(ServletManager.Code.InternError, "This groupApp dosen't exist.");
		return groupApp;
	}
	
	public List<Infrastructure> getInfras() {
		return infras;
	}
}
