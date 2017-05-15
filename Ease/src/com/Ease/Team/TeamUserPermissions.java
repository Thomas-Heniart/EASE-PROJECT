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
        MODERATOR(3),
        ADMINISTRATOR(7);

        private int value;

        private Role(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static String getRoleNameByValue(int value) {
            for (Role role : Role.values()) {
                if (value == role.getValue()) return role.name();
            }
            return null;
        }
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "permissions")
    protected Integer permissions;

    @Transient
    protected Role role;

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

    public boolean haveRole(Role role) {
        return (this.permissions & role.getValue()) >= role.getValue();
    }

    public boolean hasAdminPermissions() {
        return this.haveRole(Role.ADMINISTRATOR);
    }

    public String getRole() {
        return Role.getRoleNameByValue(this.permissions);
    }
}
