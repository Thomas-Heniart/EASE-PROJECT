package com.Ease.NewDashboard.User;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.*;

/**
 * Created by thomas on 21/04/2017.
 */
@Entity
@Table(name = "options")
public class Options {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "background_picked")
    protected Boolean background_picked;

    @Column(name = "infinite_session")
    protected Boolean infinite_session;

    @Column(name = "homepage_state")
    protected Boolean homepage_state;

    public Options(Boolean background_picked, Boolean infinite_session, Boolean homepage_state) {
        this.background_picked = background_picked;
        this.infinite_session = infinite_session;
        this.homepage_state = homepage_state;
    }

    public Options() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Boolean getBackground_picked() {
        return background_picked;
    }

    public void setBackground_picked(Boolean background_picked) {
        this.background_picked = background_picked;
    }

    public Boolean getInfinite_session() {
        return infinite_session;
    }

    public void setInfinite_session(Boolean infinite_session) {
        this.infinite_session = infinite_session;
    }

    public Boolean getHomepage_state() {
        return homepage_state;
    }

    public void setHomepage_state(Boolean homepage_state) {
        this.homepage_state = homepage_state;
    }

    public static Options createDefaultOptions() {
        return new Options(false, false, false);
    }
}
