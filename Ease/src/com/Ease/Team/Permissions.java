package com.Ease.Team;

import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Created by thomas on 10/04/2017.
 */
public abstract class Permissions {
    protected String db_id;
    protected int permissions;

    public String getDb_id() {
        return db_id;
    }

    public boolean havePermission(int permissions) {
        return (this.permissions >> permissions) % 2 == 1;
    }

    public abstract void setPermissions(int permissions, ServletManager sm) throws GeneralException;
}
