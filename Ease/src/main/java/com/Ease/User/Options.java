package com.Ease.User;

import javax.persistence.*;

@Entity
@Table(name = "options")
public class Options {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "background_picked")
    private boolean background_picked = false;

    @Column(name = "infinite_session")
    private boolean infinite_session = false;

    @Column(name = "homepage_state")
    private boolean homepage_state = false;

    public Options() {

    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public boolean isBackground_picked() {
        return background_picked;
    }

    public void setBackground_picked(boolean background_picked) {
        this.background_picked = background_picked;
    }

    public boolean isInfinite_session() {
        return infinite_session;
    }

    public void setInfinite_session(boolean infinite_session) {
        this.infinite_session = infinite_session;
    }

    public boolean isHomepage_state() {
        return homepage_state;
    }

    public void setHomepage_state(boolean homepage_state) {
        this.homepage_state = homepage_state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Options options = (Options) o;

        return db_id.equals(options.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}