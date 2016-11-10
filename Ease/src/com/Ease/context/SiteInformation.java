package com.Ease.context;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SiteInformation {
	protected String informationName;
	protected String informationType;

	public SiteInformation(ResultSet rs) {
		try {
			informationName = rs.getString(1);
			informationType = rs.getString(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getInformationName() {
		return informationName;
	}

	public String getInformationType() {
		return informationType;
	}
}
