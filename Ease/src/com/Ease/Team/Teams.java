package com.Ease.Team;


import javax.persistence.*;

/**
 * Created by thomas on 19/04/2017.
 */
@Entity
@Table(name = "Teams")
public class Teams {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "name")
    protected String name;

    public Teams(String name) {
        this.name = name;

    }

    public Teams() {

    }

    public String getName() {
        return name;
    }

    public Integer getDb_id() {
        return this.db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
