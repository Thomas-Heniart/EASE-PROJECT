package com.Ease.dashboard;

public abstract class Permissions {
	
	
	protected String	db_id;
	protected String	group_id;
	protected int		perms;
	
	public Permissions(String db_id, String group_id, int perms) {
		this.db_id = db_id;
		this.group_id = group_id;
		this.perms = perms;
	}
	
	public boolean havePermission(int perm) {
		if ((perms >> perm) % 2 == 1){
			return true;
		}
		return false;
	}
	
	public String getDBid() {
		return db_id;
	}
	public String getGroupId() {
		return group_id;
	}
}
