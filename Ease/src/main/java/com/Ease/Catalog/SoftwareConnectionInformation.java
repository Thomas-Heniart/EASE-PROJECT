package com.Ease.Catalog;

import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "softwareConnectionInformation")
public class SoftwareConnectionInformation {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @ManyToOne
    @JoinColumn(name = "software_id")
    private Software software;

    @Column(name = "information_name")
    private String information_name;

    @Column(name = "information_type")
    private String information_type;

    @Column(name = "priority")
    private Integer priority;

    public SoftwareConnectionInformation() {

    }

    public SoftwareConnectionInformation(Software software, String information_name, String information_type, Integer priority) {
        this.software = software;
        this.information_name = information_name;
        this.information_type = information_type;
        this.priority = priority;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
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

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("information_type", this.getInformation_type());
        res.put("priority", this.getPriority());
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SoftwareConnectionInformation that = (SoftwareConnectionInformation) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }

    @Override
    public String toString() {
        return "SoftwareConnectionInformation{" +
                "db_id=" + db_id +
                ", software=" + software +
                ", information_name='" + information_name + '\'' +
                ", information_type='" + information_type + '\'' +
                ", priority=" + priority +
                '}';
    }
}