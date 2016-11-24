package com.Ease.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class WebsiteInformation {
	
	public static List<WebsiteInformation> loadInformations(String website_id, ServletManager sm) throws GeneralException {
		DataBaseConnection db = sm.getDB();
		List<WebsiteInformation> website_informations = new LinkedList<WebsiteInformation>();
		ResultSet rs = db.get("SELECT information_name, information_type FROM websitesInformations WHERE website_id=" + website_id + ";");
		try {
			while (rs.next()) {
				website_informations.add(new WebsiteInformation(rs.getString(1), rs.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		return website_informations;
	}
	
	protected String information_name;
	protected String information_value;
	
	public WebsiteInformation(String information_name, String information_value) {
		this.information_name = information_name;
		this.information_value = information_value;
	}

	public String getInformation_name() {
		return information_name;
	}

	public void setInformation_name(String information_name) {
		this.information_name = information_name;
	}

	public String getInformation_value() {
		return information_value;
	}

	public void setInformation_value(String information_value) {
		this.information_value = information_value;
	}
}
