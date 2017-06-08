package com.Ease.Context.Catalog;

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class WebsiteInformation {

    public static WebsiteInformation createInformation(String website_id, String name, String type, String priority, String placeholder, String placeholder_icon, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("INSERT INTO websitesInformations VALUES (null, ?, ?, ?, ?, ?, ?);");
        request.setInt(website_id);
        request.setString(name);
        request.setString(type);
        request.setInt(priority);
        request.setString(placeholder);
        request.setString(placeholder_icon);
        String db_id = request.set().toString();
        return new WebsiteInformation(db_id, name, type, placeholder, placeholder_icon, Integer.parseInt(priority));
    }

    public static List<WebsiteInformation> loadInformations(String website_id, DataBaseConnection db) throws GeneralException {
        List<WebsiteInformation> website_informations = new LinkedList<WebsiteInformation>();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM websitesInformations WHERE website_id= ? ORDER BY priority;");
        request.setInt(website_id);
        DatabaseResult rs = request.get();
        while (rs.next())
            website_informations.add(new WebsiteInformation(rs.getString("id"), rs.getString("information_name"), rs.getString("information_type"), rs.getString("placeholder"), rs.getString("placeholder_icon"), rs.getInt("priority")));
        return website_informations;
    }

    protected String db_id;
    protected String information_name;
    protected String information_type;
    protected String placeholder;
    protected String placeholder_icon;
    protected Integer priority;

    public WebsiteInformation(String db_id, String information_name, String information_type, String placeholder, String placeholder_icon, Integer priority) {
        this.db_id = db_id;
        this.information_name = information_name;
        this.information_type = information_type;
        this.placeholder = placeholder;
        this.placeholder_icon = placeholder_icon;
        this.priority = priority;
    }

    public String getInformationName() {
        return information_name;
    }

    public void setInformation_name(String information_name) {
        this.information_name = information_name;
    }

    public String getInformationType() {
        return information_type;
    }

    public void setInformation_value(String information_value) {
        this.information_type = information_value;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public String getPlaceholder_icon() {
        return this.placeholder_icon;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void refresh(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT information_name, information_type, placeholder, placeholder_icon FROM websitesInformations WHERE id = ?;");
        request.setInt(this.db_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.InternError, "This information does not exist");
        this.information_name = rs.getString(1);
        this.information_type = rs.getString(2);
        this.placeholder = rs.getString(3);
        this.placeholder_icon = rs.getString(4);
        this.priority = rs.getInt(5);
    }

    public JSONObject getJson() {
        JSONObject res = this.getInformationJson();
        res.put("name", this.information_name);
        res.put("type", this.information_type);
        res.put("placeholder", this.placeholder);
        res.put("placeholderIcon", this.placeholder_icon);
        return res;
    }

    public JSONObject getInformationJson() {
        JSONObject res = new JSONObject();
        res.put("type", this.information_type);
        res.put("placeholder", this.placeholder);
        res.put("placeholderIcon", this.placeholder_icon);
        res.put("priority", this.priority);
        return res;
    }
}
