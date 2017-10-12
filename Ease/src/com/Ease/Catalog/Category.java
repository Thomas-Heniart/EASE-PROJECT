package com.Ease.Catalog;

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

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, orphanRemoval = true)
    @MapKey(name = "db_id")
    protected Map<Integer, Website> websiteMap = new ConcurrentHashMap<>();

    public Category() {

    }

    public Category(String name) {
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

    public Map<Integer, Website> getWebsiteMap() {
        return websiteMap;
    }

    public void setWebsiteMap(Map<Integer, Website> websiteMap) {
        this.websiteMap = websiteMap;
    }
}
