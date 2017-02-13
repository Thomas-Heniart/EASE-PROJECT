package com.Ease.Context.Catalog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class WebsiteInformation {
	
	public static WebsiteInformation createInformation(String website_id, String name, String type, DataBaseConnection db) throws GeneralException {
		String db_id = db.set("INSERT INTO websitesInformations VALUES (null, "+website_id+", '"+ name +"', '"+ type +"');").toString();
		return new WebsiteInformation(db_id, name, type);
	}
	
	public static List<WebsiteInformation> loadInformations(String website_id, DataBaseConnection db) throws GeneralException {
		List<WebsiteInformation> website_informations = new LinkedList<WebsiteInformation>();
		ResultSet rs = db.get("SELECT id, information_name, information_type FROM websitesInformations WHERE website_id=" + website_id + ";");
		try {
			while (rs.next()) {
				website_informations.add(new WebsiteInformation(rs.getString(1), rs.getString(2), rs.getString(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return website_informations;
	}
	
	protected String db_id;
	protected String information_name;
	protected String information_value;
	
	public WebsiteInformation(String db_id, String information_name, String information_value) {
		this.db_id = db_id;
		this.information_name = information_name;
		this.information_value = information_value;
	}

	public String getInformationName() {
		return information_name;
	}

	public void setInformation_name(String information_name) {
		this.information_name = information_name;
	}

	public String getInformationValue() {
		return information_value;
	}

	public void setInformation_value(String information_value) {
		this.information_value = information_value;
	}

	public void refresh(ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		ResultSet rs = db.get("SELECT information_name, information_type FROM websitesInformations WHERE id = " + this.db_id + ";");
		try {
			if (!rs.next())
				throw new GeneralException(ServletManager.Code.InternError, "This information does not exist");
			this.information_name = rs.getString(1);
			this.information_value = rs.getString(2);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
	}
}
