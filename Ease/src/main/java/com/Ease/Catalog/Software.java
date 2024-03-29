package com.Ease.Catalog;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "software")
public class Software {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "name")
    private String name;

    @Column(name = "folder")
    private String folder;

    @Column(name = "logo_url")
    private String logo_url;

    @Column(name = "logo_version")
    private Integer logo_version = 0;

    @OneToMany(mappedBy = "software")
    private Set<SoftwareConnectionInformation> softwareConnectionInformationSet = ConcurrentHashMap.newKeySet();

    public Software() {

    }

    public Software(String name, String folder) {
        this.name = name;
        this.folder = folder;
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

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public Integer getLogo_version() {
        return logo_version;
    }

    public void setLogo_version(Integer logo_version) {
        this.logo_version = logo_version;
    }

    public Set<SoftwareConnectionInformation> getSoftwareConnectionInformationSet() {
        return softwareConnectionInformationSet;
    }

    public void setSoftwareConnectionInformationSet(Set<SoftwareConnectionInformation> softwareConnectionInformationSet) {
        this.softwareConnectionInformationSet = softwareConnectionInformationSet;
    }

    public String getLogo() {
        return this.getLogo_url() == null ? ("/resources/software/" + this.getFolder() + "/logo.png?v=" + this.getLogo_version()) : this.getLogo_url();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Software software = (Software) o;

        return db_id != null ? db_id.equals(software.db_id) : software.db_id == null;
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }

    @Override
    public String toString() {
        return "Software{" +
                "db_id=" + db_id +
                ", name='" + name + '\'' +
                ", folder='" + folder + '\'' +
                ", logo_url='" + logo_url + '\'' +
                '}';
    }

    public void addSoftwareConnectionInformation(SoftwareConnectionInformation softwareConnectionInformation) {
        this.getSoftwareConnectionInformationSet().add(softwareConnectionInformation);
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("name", this.getName());
        res.put("logo", this.getLogo());
        JSONObject connection_information = new JSONObject();
        this.getSoftwareConnectionInformationSet().forEach(softwareConnectionInformation -> connection_information.put(softwareConnectionInformation.getInformation_name(), softwareConnectionInformation.getJson()));
        res.put("connection_information", connection_information);
        return res;
    }

    public Map<String, String> getInformationNeeded(JSONObject account_information) throws HttpServletException {
        Map<String, String> res = new HashMap<>();
        for (SoftwareConnectionInformation softwareConnectionInformation : this.getSoftwareConnectionInformationSet()) {
            String value = account_information.optString(softwareConnectionInformation.getInformation_name());
            if (value == null || value.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Missing information: " + softwareConnectionInformation.getInformation_name());
            res.put(softwareConnectionInformation.getInformation_name(), value);
        }
        if (res.size() != this.getSoftwareConnectionInformationSet().size())
            throw new HttpServletException(HttpStatus.BadRequest, "Some parameters are missing");
        return res;
    }

    public JSONObject getAllCredentialsFromJson(JSONObject account_information) throws HttpServletException {
        JSONObject res = new JSONObject();
        for (SoftwareConnectionInformation softwareConnectionInformation : this.getSoftwareConnectionInformationSet()) {
            String value = account_information.optString(softwareConnectionInformation.getInformation_name());
            if (value == null || value.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Missing information: " + softwareConnectionInformation.getInformation_name());
            res.put(softwareConnectionInformation.getInformation_name(), value);
        }
        if (res.length() != this.getSoftwareConnectionInformationSet().size())
            throw new HttpServletException(HttpStatus.BadRequest, "Some credentials are missing");
        return res;
    }

    public JSONObject getPresentCredentialsFromJson(JSONObject account_information) {
        JSONObject res = new JSONObject();
        for (SoftwareConnectionInformation softwareConnectionInformation : this.getSoftwareConnectionInformationSet()) {
            String name = softwareConnectionInformation.getInformation_name();
            String value = account_information.optString(name, null);
            if (value == null || value.isEmpty())
                continue;
            res.put(name, value);
        }
        return res;
    }

    public boolean isDifferentConnectionInformation(JSONObject connection_information) {
        if (connection_information.length() != this.getSoftwareConnectionInformationSet().size())
            return true;
        for (SoftwareConnectionInformation softwareConnectionInformation : this.getSoftwareConnectionInformationSet()) {
            if (!connection_information.has(softwareConnectionInformation.getInformation_name()))
                return true;
        }
        return false;
    }
}