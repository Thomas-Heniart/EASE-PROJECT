package com.Ease.Context.Catalog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.IdGenerator;
import com.Ease.Utils.ServletManager;

public class Sso {

	public enum Data{
		NOTHING,
		ID,
		NAME
	}
	
	public static List<Sso> loadSsos(DataBaseConnection db, ServletContext context) throws GeneralException {
		List<Sso> ssos = new LinkedList<Sso>();
		try {
			ResultSet rs = db.get("SELECT * FROM sso;");
			Sso sso;
			while (rs.next()) {
				int single_id = ((IdGenerator)context.getAttribute("idGenerator")).getNextId();
				sso = new Sso(rs.getString(Data.ID.ordinal()), rs.getString(Data.NAME.ordinal()), single_id);
				ssos.add(sso);
			}
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return ssos;
	}
	
	
	protected List<Website> websites;
	protected Map<String, Website> websitesDBmap;
	protected Map<Integer, Website> websitesIDmap;
	protected String		name;
	protected int			single_id;
	protected String		db_id;
	
	public Sso(String db_id, String name, int single_id) {
		this.name = name;
		this.websites = null;
		this.websitesDBmap = new HashMap<String, Website>();
		this.websitesIDmap = new HashMap<Integer, Website>();
		this.single_id = single_id;
		this.db_id = db_id;
	}
	
	public void addWebsite(Website site) {
		this.websites.add(site);
		this.websitesDBmap.put(site.getDb_id(), site);
		this.websitesIDmap.put(site.getSingleId(), site);
	}
	
	public int getSingleId() {
		return single_id;
	}
	
	public String getDbid() {
		return this.db_id;
	}
}
