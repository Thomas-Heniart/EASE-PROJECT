package com.Ease.Notification;

import com.Ease.Team.TeamUser;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 12/06/2017.
 */
@Entity
@Table(name = "teamUserNotifications")
@PrimaryKeyJoinColumn(name = "id")
public class TeamUserNotification extends TeamNotification {

    @ManyToOne
    @JoinColumn(name = "team_user_id")
    protected TeamUser teamUser;


    public TeamUserNotification(String title, String content, String logo_path, Boolean seen, Date creation_date, String url, TeamUser teamUser) {
        super(title, content, logo_path, seen, creation_date, url, teamUser.getTeam());
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
}