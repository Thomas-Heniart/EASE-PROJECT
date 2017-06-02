package com.Ease.Team;

import javax.persistence.*;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity
@Table(name = "teamUserRoles")
public class TeamUserRole {

    public enum Role {
        MEMBER(1),
        ADMINISTRATOR(2),
        OWNER(3);

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

    @Column(name = "role")
    protected Integer roleValue;

    @Transient
    protected Role role;

    public TeamUserRole(Integer roleValue) {
        this.roleValue = roleValue;
    }

    public TeamUserRole() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Integer getRoleValue() {
        return roleValue;
    }

    public void setRoleValue(Integer roleValue) {
        System.out.println("Role value: " + roleValue);
        this.roleValue = roleValue;
    }

    public void setRole(Role role) {
        this.setRoleValue(role.getValue());
    }

    public boolean haveRole(Role role) {
        return (this.roleValue & role.getValue()) >= role.getValue();
    }

    public boolean isAdmin() {
        return this.haveRole(Role.ADMINISTRATOR);
    }

    public boolean isOwner() {
        return this.haveRole(Role.OWNER);
    }

    public boolean isSuperior(TeamUserRole teamUserRole) {
        return this.getRoleValue() > teamUserRole.getRoleValue();
    }
}
