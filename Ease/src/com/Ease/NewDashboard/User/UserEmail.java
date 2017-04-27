package com.Ease.NewDashboard.User;

import javax.persistence.*;

/**
 * Created by thomas on 26/04/2017.
 */
@Entity
@Table(name = "UsersEmails")
public class UserEmail {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    @Column(name = "email")
    protected String email;

    @Column(name = "verified")
    protected Boolean verified;

    public UserEmail(User user, String email, Boolean verified) {
        this.user = user;
        this.email = email;
        this.verified = verified;
    }

    public UserEmail(String email, Boolean verified) {
        this.email = email;
        this.verified = verified;
    }

    public UserEmail() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
