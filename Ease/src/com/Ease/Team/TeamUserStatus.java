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

    public TeamUserStatus() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public boolean reminder_three_days_sended() {
        return this.reminder_three_days_sended;
    }

    public Boolean getReminder_three_days_sended() {
        return reminder_three_days_sended;
    }

    public void setReminder_three_days_sended(Boolean reminder_three_days_sended) {
        this.reminder_three_days_sended = reminder_three_days_sended;
    }
}
