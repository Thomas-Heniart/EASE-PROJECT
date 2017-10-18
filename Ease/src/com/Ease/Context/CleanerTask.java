package com.Ease.Context;

import com.Ease.Utils.DataBase;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import org.apache.poi.ss.usermodel.DateUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.TimerTask;

public class CleanerTask extends TimerTask {

    @Override
    public void run() {
        try {
            DataBaseConnection db;
            try {
                db = new DataBaseConnection(DataBase.getConnection());
                DatabaseRequest request = db.prepareRequest("DELETE FROM jsonWebTokens WHERE expiration_date <= ?");
                request.setObject(new Date().getTime() - 2 * DateUtil.DAY_MILLISECONDS);
                request.set();
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
