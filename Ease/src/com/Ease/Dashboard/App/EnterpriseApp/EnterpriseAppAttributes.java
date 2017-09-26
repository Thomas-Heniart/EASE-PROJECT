package com.Ease.Dashboard.App.EnterpriseApp;

import com.Ease.Utils.*;

public class EnterpriseAppAttributes {

    public static EnterpriseAppAttributes createEnterpriseAppAttributes(Integer enterprise_app_id, Boolean fill_in_switch, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("INSERT INTO enterpriseAppAttributes VALUES (null, ?, ?);");
            request.setInt(enterprise_app_id);
            request.setBoolean(fill_in_switch);
            Integer db_id = request.set();
            return new EnterpriseAppAttributes(db_id, fill_in_switch);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static EnterpriseAppAttributes loadEnterpriseAppAttributes(Integer enterprise_app_id, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("SELECT * FROM enterpriseAppAttributes WHERE enterprise_app_id = ?;");
            request.setInt(enterprise_app_id);
            DatabaseResult result = request.get();
            if (!result.next())
                throw new HttpServletException(HttpStatus.InternError, "Not possible");
            return new EnterpriseAppAttributes(result.getInt("id"), result.getBoolean("fill_in_switch"));
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    protected Integer db_id;
    protected Boolean fill_in_switch;

    public EnterpriseAppAttributes(Integer db_id, Boolean fill_in_switch) {
        this.db_id = db_id;
        this.fill_in_switch = fill_in_switch;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Boolean getFill_in_switch() {
        return fill_in_switch;
    }

    public void setFill_in_switch(Boolean fill_in_switch) {
        this.fill_in_switch = fill_in_switch;
    }

    public void setFill_in_switch(Boolean fill_in_switch, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("UPDATE enterpriseAppAttributes SET fill_in_switch = ? WHERE id = ?");
            request.setBoolean(fill_in_switch);
            request.setInt(this.getDb_id());
            request.set();
            this.setFill_in_switch(fill_in_switch);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }
}
