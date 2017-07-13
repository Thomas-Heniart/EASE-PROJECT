package com.Ease.Notification;

import com.Ease.Team.TeamUser;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 12/06/2017.
 */
@Entity
@Table(name = "teamUserNotifications")
@PrimaryKeyJoinColumn(name = "id")
public class TeamUserNotification extends Notification {

    @ManyToOne
    @JoinColumn(name = "team_user_id")
    protected TeamUser teamUser;


    public TeamUserNotification(String content, String logo_path, Boolean seen, Date creation_date, String url, TeamUser teamUser) {
        super(content, logo_path, seen, creation_date, url);
        this.teamUser = teamUser;
    }

    public TeamUserNotification() {
    }

    public TeamUser getTeamUser() {
        return teamUser;
    }

    public void setTeamUser(TeamUser teamUser) {
        this.teamUser = teamUser;
    }

    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("team_user_id", this.getTeamUser().getDb_id());
        return res;
    }
}