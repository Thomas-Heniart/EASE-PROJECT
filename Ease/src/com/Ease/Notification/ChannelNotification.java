package com.Ease.Notification;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 12/06/2017.
 */
@Entity
@Table(name = "channelNotifications")
@PrimaryKeyJoinColumn(name = "id")
public class ChannelNotification extends TeamNotification {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer id;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    protected Channel channel;

    public ChannelNotification(String title, String content, String logo_path, Boolean seen, Date creation_date, String url, Team team, Channel channel) {
        super(title, content, logo_path, seen, creation_date, url, team);
        this.channel = channel;
    }

    public ChannelNotification() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}