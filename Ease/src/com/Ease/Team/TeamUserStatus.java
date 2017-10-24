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
    protected Boolean reminder_three_days_sended = false;

    @Column(name = "first_app_received")
    protected Boolean first_app_received = false;

    @Column(name = "tuto_done")
    protected Boolean tuto_done = false;

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
}
