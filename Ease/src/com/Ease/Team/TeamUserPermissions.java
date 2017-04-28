package com.Ease.Team;

import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

import javax.persistence.*;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity
@Table(name = "teamUserPermissions")
public class TeamUserPermissions {

    public enum Role {
        MEMBER(1),
        MODERATOR(2),
        ADMINISTRATOR(4);

        private int value;

        private Role(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "permissions")
    protected Integer permissions;

    public TeamUserPermissions(Integer permissions) {
        this.permissions = permissions;
    }

    public TeamUserPermissions() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Integer getPermissions() {
        return permissions;
    }

    public void setPermissions(Integer permissions) {
        System.out.println("Permissions value: " + permissions);
        this.permissions = permissions;
    }

    public boolean  haveRole(Role role) {
        return (this.permissions & role.getValue()) >= this.permissions;
    }
}
