package com.Ease.Website;

import javax.persistence.*;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Table(name = "Sso")
public class Sso {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "name")
    protected String name;

    @Column(name = "img_path")
    protected String imgPath;

    public Sso(String name, String imgPath) {
        this.name = name;
        this.imgPath = imgPath;
    }

    public Sso() {
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
