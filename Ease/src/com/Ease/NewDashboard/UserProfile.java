package com.Ease.NewDashboard;

import com.Ease.NewDashboard.Profile.Profile;
import com.Ease.NewDashboard.User.User;

import javax.persistence.*;

/**
 * Created by thomas on 21/04/2017.
 */
@Entity
@Table(name = "UserAndProfileMap")
public class UserProfile {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @OneToOne
    @JoinColumn(name="user_id")
    protected User user;

    @OneToOne
    @JoinColumn(name = "profile_id")
    protected Profile profile;

    public UserProfile(User user, Profile profile) {
        this.user = user;
        this.profile = profile;
    }

    public UserProfile() {
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
