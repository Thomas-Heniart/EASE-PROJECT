package com.Ease.Team;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
        put(1, Role.MEMBER);
        put(2, Role.ADMINISTRATOR);
        put(3, Role.OWNER);
    }};

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
        System.out.println("Role value: " + roleValue);
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

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(this.db_id);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TeamUser))
            return false;
        TeamUser teamUser = (TeamUser) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.db_id, teamUser.db_id);
        return eb.isEquals();
    }
}
