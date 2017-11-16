package com.Ease.Catalog;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Table(name = "websiteAttributes")
public class WebsiteAttributes {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "addedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate = new Date();

    @Column(name = "new")
    private boolean new_website = true;

    @Column(name = "public")
    private boolean public_website;

    @Column(name = "integrated")
    private boolean integrated = false;

    public WebsiteAttributes(Boolean public_website) {
        this.public_website = public_website;
    }

    public WebsiteAttributes() {

    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public boolean isNew_website() {
        return new_website;
    }

    public void setNew_website(boolean new_website) {
        this.new_website = new_website;
    }

    public boolean isPublic_website() {
        return public_website;
    }

    public void setPublic_website(boolean public_website) {
        this.public_website = public_website;
    }

    public boolean isIntegrated() {
        return integrated;
    }

    public void setIntegrated(boolean integrated) {
        this.integrated = integrated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebsiteAttributes that = (WebsiteAttributes) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}
