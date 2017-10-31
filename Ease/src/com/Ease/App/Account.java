package com.Ease.App;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "lastUpdateDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_update;

    @Column(name = "reminderIntervalValue")
    private Integer reminder_interval;

    @Column(name = "publicKey")
    private String public_key;

    @Column(name = "privateKey")
    private String private_key;

    @Column(name = "mustBeReciphered")
    private boolean must_be_reciphered;

    public Account() {

    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }
}