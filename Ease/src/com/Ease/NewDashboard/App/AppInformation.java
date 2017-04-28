package com.Ease.NewDashboard.App;

import javax.persistence.*;

/**
 * Created by thomas on 20/04/2017.
 */
@Entity
@Table(name = "AppsInformations")
public class AppInformation {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "name")
    protected String name;

    public AppInformation(String name) {
        this.name = name;
    }

    public AppInformation() {
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
}
