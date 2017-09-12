package com.Ease.Utils;

import java.util.concurrent.ThreadLocalRandom;

public class Metrics {

    private Long connection_number;

    public Metrics(DataBaseConnection db) {
        try {
            DatabaseRequest request = db.prepareRequest("SELECT metric_value FROM ease_metrics WHERE metric_name = ?");
            request.setString("app_connections");
            DatabaseResult rs = request.get();
            if (!rs.next())
                return;
            connection_number = Long.valueOf(rs.getString(1));
        } catch (GeneralException e) {
            e.printStackTrace();
        }
    }

    public void increaseConnection(DataBaseConnection db) throws HttpServletException {
        try {
            connection_number = connection_number + ThreadLocalRandom.current().nextLong(1, 16);
            DatabaseRequest request = db.prepareRequest("UPDATE ease_metrics SET metric_value = ? WHERE metric_name = ?");
            request.setString(connection_number.toString());
            request.setString("app_connections");
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public Long getConnection_number() {
        return this.connection_number;
    }


}
