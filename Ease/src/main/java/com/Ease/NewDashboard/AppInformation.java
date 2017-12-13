package com.Ease.NewDashboard;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppInformation that = (AppInformation) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}