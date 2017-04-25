package com.Ease.Website;

import javax.persistence.*;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Table(name = "Tags")
public class Tag {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "tag_name")
    protected String tagName;

    @Column(name = "color")
    protected int color;

    @Column(name = "priority")
    protected Integer priority;

    public Tag(String tagName, int color, Integer priority) {
        this.tagName = tagName;
        this.color = color;
        this.priority = priority;
    }

    public Tag() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
