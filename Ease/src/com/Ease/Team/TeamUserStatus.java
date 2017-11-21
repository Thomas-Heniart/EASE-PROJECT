package com.Ease.Team;

import javax.persistence.*;

/**
 * Created by thomas on 06/07/2017.
 */
@Entity
@Table(name = "teamUserStatus")
public class TeamUserStatus {

    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "reminder_three_days_sended")
    private Boolean reminder_three_days_sended = false;

    @Column(name = "first_app_received")
    private Boolean first_app_received = false;

    @Column(name = "tuto_done")
    private Boolean tuto_done = false;

    @Column(name = "invitation_sent")
    private boolean invitation_sent = false;

    @Column(name = "profile_created")
    private boolean profile_created = false;

    public TeamUserStatus() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Boolean reminder_three_days_sended() {
        return this.reminder_three_days_sended;
    }

    public void setReminder_three_days_sended(Boolean reminder_three_days_sended) {
        this.reminder_three_days_sended = reminder_three_days_sended;
    }

    public Boolean first_app_received() {
        return first_app_received;
    }

    public void setFirst_app_received(Boolean first_app_received) {
        this.first_app_received = first_app_received;
    }

    public Boolean tuto_done() {
        return tuto_done;
    }

    public void setTuto_done(Boolean tuto_done) {
        this.tuto_done = tuto_done;
    }

    public boolean isInvitation_sent() {
        return invitation_sent;
    }

    public void setInvitation_sent(boolean invitation_sent) {
        this.invitation_sent = invitation_sent;
    }

    public boolean isProfile_created() {
        return profile_created;
    }

    public void setProfile_created(boolean profile_created) {
        this.profile_created = profile_created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamUserStatus that = (TeamUserStatus) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}
