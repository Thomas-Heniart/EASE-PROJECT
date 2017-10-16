package com.Ease.Catalog;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "name")
    protected String name;

    @Column(name = "position")
    protected Integer position;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, orphanRemoval = true)
    @MapKey(name = "db_id")
    protected Map<Integer, Website> websiteMap = new ConcurrentHashMap<>();

    public Category() {

    }

    public Category(String name, Integer position) {
        this.name = name;
        this.position = position;
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

    public Map<Integer, Website> getWebsiteMap() {
        return websiteMap;
    }

    public void setWebsiteMap(Map<Integer, Website> websiteMap) {
        this.websiteMap = websiteMap;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        JSONArray websites = new JSONArray();
        websites.addAll(this.websiteMap.keySet());
        res.put("websites", websites);
        res.put("id", this.getDb_id());
        res.put("name", this.getName());
        res.put("position", this.getPosition());
        return res;
    }
}
