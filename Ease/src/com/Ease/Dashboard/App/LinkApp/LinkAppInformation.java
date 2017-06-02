package com.Ease.Dashboard.App.LinkApp;

import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;

public class LinkAppInformation {

    public enum LoadData {
        NOTHING,
        ID,
        LINK,
        IMG_URL
    }

	/*
     *
	 * Loader And Creator
	 * 
	 */

    public static LinkAppInformation createLinkAppInformation(String link, String imgUrl, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("INSERT INTO linkAppInformations values (NULL, ?, ?);");
        request.setString(link);
        request.setString(imgUrl);
        int db_id = request.set();
        return new LinkAppInformation(String.valueOf(db_id), link, imgUrl);
    }

    public static String createLinkAppInformationForUnconnected(String link, String imgUrl, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("INSERT INTO linkAppInformations values (NULL, ?, ?);");
        request.setString(link);
        request.setString(imgUrl);
        int db_id = request.set();
        return String.valueOf(db_id);
    }

    public static LinkAppInformation loadLinkAppInformation(String db_id, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM linkAppInformations WHERE id = ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        rs.next();
        return new LinkAppInformation(db_id, rs.getString(LoadData.LINK.ordinal()), rs.getString(LoadData.IMG_URL.ordinal()));
    }

	/*
	 * 
	 * Constructor
	 * 
	 */

    protected String db_id;
    protected String link;
    protected String imgUrl;

    public LinkAppInformation(String db_id, String link, String imgUrl) {
        this.db_id = db_id;
        this.link = link;
        this.imgUrl = imgUrl;
    }

    public void removeFromDb(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("DELETE FROM linkAppInformations WHERE id = ?;");
        request.setInt(db_id);
        request.set();
    }
	
	/*
	 * 
	 * Getter And Setter
	 * 
	 */

    public String getDb_id() {
        return this.db_id;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE linkAppInformations SET url = ? WHERE id = ?;");
        request.setString(link);
        request.setInt(db_id);
        request.set();
        this.link = link;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE linkAppInformations SET img_url = ? WHERE id = ?;");
        request.setString(imgUrl);
        request.setInt(db_id);
        request.set();
        this.imgUrl = imgUrl;
    }

    public void edit(JSONObject editJson, ServletManager sm) throws GeneralException {
        String imgUrl = (String) editJson.get("imgUrl");
        String url = (String) editJson.get("url");
        if (url != null && !url.equals(""))
            this.setLink(url, sm);
        if (imgUrl != null && !imgUrl.equals(""))
            this.setImgUrl(imgUrl, sm);
    }
}
