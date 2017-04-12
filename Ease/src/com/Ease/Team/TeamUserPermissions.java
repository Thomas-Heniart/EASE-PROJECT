package com.Ease.Team;

import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Created by thomas on 10/04/2017.
 */
public class TeamUserPermissions extends Permissions {

    public static enum Perm {
        NONE(1),
        MANAGE_USERS(2),
        MANAGE_APPS(4),
        ALL(255);

        private int value;

        private Perm(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static TeamUserPermissions createTeamUserPermissions(int permissions, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("INSERT INTO teamUserPermissions values(?, b'" + Integer.toBinaryString(permissions) + "');");
        request.setNull();
        String db_id = request.set().toString();
        return new TeamUserPermissions(db_id, permissions);
    }

    public static TeamUserPermissions createAdminPermissions(ServletManager sm) throws GeneralException {
        return createTeamUserPermissions(Perm.ALL.getValue(), sm);
    }

    public static TeamUserPermissions loadTeamUserPermissions(String permissions_id, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("SELECT permissions+0 FROM teamUserPermissions WHERE id = ?;");
        request.setInt(permissions_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "These permissions do not exist");
        return new TeamUserPermissions(permissions_id, rs.getInt(1));
    }

    public TeamUserPermissions(String db_id, int permissions) {
        this.db_id = db_id;
        this.permissions = permissions;
    }

    @Override
    public void setPermissions(int permissions, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("UPDATE teamUsersPermissions SET permissions = ? WHERE id ?;");
        request.setInt(permissions);
        request.setInt(this.db_id);
        request.set();
        this.permissions = permissions;
    }
}
