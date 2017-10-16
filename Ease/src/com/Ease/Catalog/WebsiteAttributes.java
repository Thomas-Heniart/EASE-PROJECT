package com.Ease.Catalog;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Table(name = "WebsiteAttributes")
public class WebsiteAttributes {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "addedDate")
    protected Date addedDate;

    @Column(name = "new")
    protected boolean new_website;

    @Column(name = "public")
    protected boolean public_website;

    @Column(name = "integrated")
    protected boolean integrated;

    public WebsiteAttributes(Date addedDate, Boolean new_website, Boolean public_website, Boolean integrated) {
        this.addedDate = addedDate;
        this.new_website = new_website;
        this.public_website = public_website;
        this.integrated = integrated;
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
}
