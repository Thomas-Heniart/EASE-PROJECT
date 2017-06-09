package com.Ease.Dashboard.App;

import com.Ease.Utils.*;

/**
 * Created by thomas on 08/05/2017.
 */
public class SharedAppPermissions {
    public enum Permissions {
        SHOW_PASSWORD(1),
        EDIT_PASSWORD(3);

        private int value;

        private Permissions(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    protected Integer db_id;
    protected Integer permissions;

    public SharedAppPermissions(Integer db_id, Integer permissions) {
        this.db_id = db_id;
        this.permissions = permissions;
    }

    public boolean havePermission(Permissions permission) {
        return (this.permissions & permission.getValue()) == permission.getValue();
    }

    public void addPermission(Permissions permission, DataBaseConnection db) throws HttpServletException {
        try {
            if (this.havePermission(permission))
                return;
            DatabaseRequest request = db.prepareRequest("UPDATE sharedAppPermissions SET permissions = ? WHERE id = ?;");
            request.setInt(this.permissions + permission.getValue());
            request.setInt(this.db_id);
            request.set();
            this.permissions += permission.getValue();
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void removePermission(Permissions permission, DataBaseConnection db) throws HttpServletException {
        try {
            if (!this.havePermission(permission))
                return;
            DatabaseRequest request = db.prepareRequest("UPDATE sharedAppPermissions SET permissions = ? WHERE id = ?;");
            request.setInt(this.permissions - permission.getValue());
            request.setInt(this.db_id);
            request.set();
            this.permissions -= permission.getValue();
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }
}
