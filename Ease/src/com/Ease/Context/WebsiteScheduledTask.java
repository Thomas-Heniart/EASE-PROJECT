package com.Ease.Context;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Utils.DataBase;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;

import java.util.TimerTask;

public class WebsiteScheduledTask extends TimerTask {

    private Catalog catalog;

    public WebsiteScheduledTask(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void run() {
        try {
            DataBaseConnection db = new DataBaseConnection(DataBase.getConnection());
            DatabaseRequest request = db.prepareRequest("SELECT websites.id FROM websites JOIN websiteAttributes ON (websites.website_attributes_id = websiteAttributes.id) WHERE addedDate <= CURRENT_TIMESTAMP - INTERVAL 4 DAY AND new = 1;");
            DatabaseResult rs = request.get();
            if (!rs.next()) {
                catalog.updateWebsitePostions();
                return;
            }
            int transaction = db.startTransaction();
            do {
                Website website = catalog.getWebsiteWithId(rs.getInt(1));
                System.out.println(website.getName() + " not new anymore");
                website.setNew(false, db);
            } while (rs.next());
            catalog.updateWebsitePostions();
            db.commitTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
