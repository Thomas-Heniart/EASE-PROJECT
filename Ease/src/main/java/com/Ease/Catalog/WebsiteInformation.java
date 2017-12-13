package com.Ease.Catalog;

import org.json.JSONObject;

import javax.persistence.*;

/**
 * Created by thomas on 25/04/2017.
 */
@Entity
@Table(name = "websitesInformations")
public class WebsiteInformation {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "information_name")
    private String information_name;

    @Column(name = "information_type")
    private String information_type;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "placeholder")
    private String placeholder;

    @Column(name = "placeholder_icon")
    private String placeholder_icon;

    @ManyToOne
    @JoinColumn(name = "website_id")
    protected Website website;

    public WebsiteInformation(String information_name, String information_type, Integer priority, String placeholder, String placeholder_icon, Website website) {
        this.information_name = information_name;
        this.information_type = information_type;
        this.priority = priority;
        this.placeholder = placeholder;
        this.placeholder_icon = placeholder_icon;
        this.website = website;
    }

    public WebsiteInformation(String information_name, String information_type, Integer priority, String placeholder, String placeholder_icon) {
        this.information_name = information_name;
        this.information_type = information_type;
        this.priority = priority;
        this.placeholder = placeholder;
        this.placeholder_icon = placeholder_icon;
    }

    public WebsiteInformation() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getInformation_name() {
        return information_name;
    }

    public void setInformation_name(String information_name) {
        this.information_name = information_name;
    }

    public String getInformation_type() {
        return information_type;
    }

    public void setInformation_type(String information_type) {
        this.information_type = information_type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPlaceholder_icon() {
        return placeholder_icon;
    }

    public void setPlaceholder_icon(String placeholder_icon) {
        this.placeholder_icon = placeholder_icon;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("type", this.getInformation_type());
        res.put("placeholder", this.getPlaceholder());
        res.put("placeholderIcon", this.getPlaceholder_icon());
        res.put("priority", this.getPriority());
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebsiteInformation that = (WebsiteInformation) o;

        return db_id.equals(that.db_id);
    }
}
