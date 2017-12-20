package com.Ease.Catalog;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sso")
public class Sso {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "name")
    private String name;

    @Column(name = "img_path")
    private String imgPath;

    @OneToMany(mappedBy = "sso", orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @MapKey(name = "db_id")
    private Map<Integer, Website> websiteMap = new ConcurrentHashMap<>();

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

    public Map<Integer, Website> getWebsiteMap() {
        return websiteMap;
    }

    public void setWebsiteMap(Map<Integer, Website> websiteMap) {
        this.websiteMap = websiteMap;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("name", this.getName());
        res.put("logo", this.getImgPath());
        JSONArray websites = new JSONArray();
        websites.put(this.getWebsiteMap().keySet());
        res.put("websites", websites);
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sso sso = (Sso) o;

        return db_id.equals(sso.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}
