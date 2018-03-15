package com.Ease.Metrics;

import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EASE_EVENT")
public class EaseEvent {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "team_id")
    private Integer team_id;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation_date = new Date();

    @Column(name = "name")
    private String name;

    @Column(name = "data")
    private String data;

    public EaseEvent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getId());
        res.put("name", this.getName());
        res.put("user_id", this.getUser_id() == null ? -1 : this.getUser_id());
        res.put("team_id", this.getTeam_id() == null ? -1 : this.getTeam_id());
        res.put("data", this.getData() == null ? new JSONObject() : new JSONObject(this.getData()));
        return res;
    }
}
