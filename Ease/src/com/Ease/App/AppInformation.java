package com.Ease.App;

import javax.persistence.*;

@Entity
@Table(name = "appsInformations")
public class AppInformation {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "name")
    private String name;

    public AppInformation() {

    }

    public AppInformation(String name) {
        this.name = name;
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