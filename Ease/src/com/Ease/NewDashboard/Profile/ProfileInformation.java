package com.Ease.NewDashboard.Profile;

import javax.persistence.*;

/**
 * Created by thomas on 20/04/2017.
 */
@Entity
@Table(name = "ProfileInfo")
public class ProfileInformation {

    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "name")
    protected String name;

    @Column(name = "color")
    protected String color;

    public ProfileInformation(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public ProfileInformation() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
