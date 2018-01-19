package com.Ease.Team.Onboarding;

import com.Ease.API.RestEasy.Serializer.OnboardingRoomDeserializer;
import com.Ease.API.RestEasy.Serializer.OnboardingRoomSerializer;
import com.Ease.Catalog.Website;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@JsonSerialize(using = OnboardingRoomSerializer.class)
@JsonDeserialize(using = OnboardingRoomDeserializer.class)
@Entity
@Table(name = "ONBOARDING_ROOM")
public class OnboardingRoom {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "example")
    private String example;

    @ManyToMany
    @JoinTable(name = "ONBOARDING_ROOM_WEBSITE", joinColumns = @JoinColumn(name = "onboarding_room_id"), inverseJoinColumns = @JoinColumn(name = "website_id"))
    private Set<Website> websiteSet = ConcurrentHashMap.newKeySet();

    public OnboardingRoom() {

    }

    public OnboardingRoom(String name, String example, Set<Website> websiteSet) {
        this.name = name;
        this.websiteSet = websiteSet;
        this.example = example;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public Set<Website> getWebsiteSet() {
        return websiteSet;
    }

    public void setWebsiteSet(Set<Website> websiteSet) {
        this.websiteSet = websiteSet;
    }

    public void addWebsite(Website website) {
        this.getWebsiteSet().add(website);
    }

    public void removeWebsite(Website website) {
        this.getWebsiteSet().remove(website);
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getId());
        res.put("name", this.getName());
        res.put("example", this.getExample());
        JSONArray website_ids = new JSONArray();
        this.getWebsiteSet().forEach(website -> website_ids.put(website.getDb_id()));
        res.put("website_ids", website_ids);
        return res;
    }
}