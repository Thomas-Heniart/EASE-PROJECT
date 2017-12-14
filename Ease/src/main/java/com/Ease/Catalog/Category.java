package com.Ease.Catalog;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private Integer position;

    @OneToMany(mappedBy = "category")
    @MapKey(name = "db_id")
    private Map<Integer, Website> websiteMap = new ConcurrentHashMap<>();

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
        websites.put(this.websiteMap.keySet());
        res.put("websites", websites);
        res.put("id", this.getDb_id());
        res.put("name", this.getName());
        res.put("position", this.getPosition());
        return res;
    }

    public void addWebsite(Website website) {
        if (this.getWebsiteMap().containsKey(website.getDb_id()))
            return;
        this.getWebsiteMap().put(website.getDb_id(), website);
    }

    public void removeWebsite(Website website) {
        this.getWebsiteMap().remove(website.getDb_id());
    }

    public Collection<Website> getWebsites() {
        return this.getWebsiteMap().values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return db_id.equals(category.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}
