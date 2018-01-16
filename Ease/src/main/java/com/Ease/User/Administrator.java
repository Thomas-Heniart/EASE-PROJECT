package com.Ease.User;

import javax.persistence.*;

@Entity
@Table(name = "admins")
public class Administrator {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private String db_id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Administrator() {

    }

    public Administrator(User user) {
        this.user = user;
    }

    public String getDb_id() {
        return db_id;
    }

    public void setDb_id(String db_id) {
        this.db_id = db_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
