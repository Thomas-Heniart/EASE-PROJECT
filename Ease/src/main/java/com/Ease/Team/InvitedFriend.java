package com.Ease.Team;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "TEAM_EMAIL_INVITED")
public class InvitedFriend {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public InvitedFriend() {

    }

    public InvitedFriend(String email, Team team) {
        this.email = email;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getId());
        res.put("email", this.getEmail());
        res.put("team_id", this.getTeam().getDb_id());
        return res;
    }
}