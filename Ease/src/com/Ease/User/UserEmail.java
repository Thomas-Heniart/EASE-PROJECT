package com.Ease.User;

import javax.persistence.*;

@Entity
@Table(name = "usersEmails")
public class UserEmail {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "email")
    private String email;

    @Column(name = "verified")
    private boolean verified = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserEmail() {

    }

    public UserEmail(String email) {
        this.email = email;
    }

    public UserEmail(String email, boolean verified) {
        this.email = email;
        this.verified = verified;
    }

    public UserEmail(String email, boolean verified, User user) {
        this.email = email;
        this.verified = verified;
        this.user = user;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEmail userEmail = (UserEmail) o;

        return db_id.equals(userEmail.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}