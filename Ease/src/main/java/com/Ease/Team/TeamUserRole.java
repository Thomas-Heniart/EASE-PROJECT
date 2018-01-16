package com.Ease.Team;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity
@Table(name = "teamUserRoles")
public class TeamUserRole {

    private static final Map<Integer, Role> roleMap = new HashMap<Integer, Role>() {{
        put(Role.MEMBER.getValue(), Role.MEMBER);
        put(Role.ADMINISTRATOR.getValue(), Role.ADMINISTRATOR);
        put(Role.OWNER.getValue(), Role.OWNER);
    }};

    private static final Map<Integer, String> roleNames = new HashMap<Integer, String>() {{
        put(Role.MEMBER.getValue(), "Member");
        put(Role.ADMINISTRATOR.getValue(), "Admin");
        put(Role.OWNER.getValue(), "Owner");
    }};

    public static boolean isInferiorToOwner(Integer roleValue) {
        return roleValue < Role.OWNER.getValue();
    }

    public static boolean isValidValue(Integer role) {
        return roleMap.get(role) != null;
    }

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

    public void setRoleValue(Integer roleValue) throws HttpServletException {
        Role role = roleMap.get(roleValue);
        if (role == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This role does not exist.");
        this.roleValue = roleValue;
    }

    public void setRole(Role role) {
        this.roleValue = role.getValue();
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

    public boolean isSuperiorOrEquals(Integer roleValue) throws HttpServletException {
        Role role = roleMap.get(roleValue);
        if (role == null)
            throw new HttpServletException(HttpStatus.BadRequest, "This role does not exist.");
        return this.getRoleValue() >= role.getValue();
    }

    public String getRoleName() {
        return roleNames.get(this.getRoleValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamUserRole that = (TeamUserRole) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}
