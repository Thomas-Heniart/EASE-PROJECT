package com.Ease.Notification;

import com.Ease.Team.Team;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 12/06/2017.
 */
@Entity
@Table(name = "teamNotifications")
@PrimaryKeyJoinColumn(name = "id")
public abstract class TeamNotification extends Notification {

    @ManyToOne
    @JoinColumn(name = "team_id")
    protected Team team;


    public TeamNotification(String title, String content, String logo_path, Boolean seen, Date creation_date, String url, Team team) {
        super(title, content, logo_path, seen, creation_date, url);
        this.team = team;
    }

    public TeamNotification() {
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}