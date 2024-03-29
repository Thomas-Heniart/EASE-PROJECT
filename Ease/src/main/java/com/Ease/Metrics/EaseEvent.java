package com.Ease.Metrics;

import org.json.JSONObject;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "EASE_EVENT")
public class EaseEvent {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "team_id")
    private Integer team_id;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creation_date = new Date();

    @Column(name = "year")
    private Integer year = Calendar.getInstance().get(Calendar.YEAR);

    @Column(name = "week_of_year")
    private Integer week_of_year = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

    @Column(name = "day_of_year")
    private Integer day_of_year = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

    @Column(name = "name")
    private String name;

    @Column(name = "data")
    private String data;

    public EaseEvent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeek_of_year() {
        return week_of_year;
    }

    public void setWeek_of_year(Integer week_of_year) {
        this.week_of_year = week_of_year;
    }

    public Integer getDay_of_year() {
        return day_of_year;
    }

    public void setDay_of_year(Integer day_of_year) {
        this.day_of_year = day_of_year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setData(JSONObject data) {
        this.data = data.toString();
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getId());
        res.put("name", this.getName());
        res.put("user_id", this.getUser_id() == null ? -1 : this.getUser_id());
        res.put("team_id", this.getTeam_id() == null ? -1 : this.getTeam_id());
        res.put("data", this.getData() == null ? new JSONObject() : new JSONObject(this.getData()));
        return res;
    }

    public void setCreation_date(Calendar calendar) {
        this.setCreation_date(calendar.getTime());
        this.setYear(calendar.get(Calendar.YEAR));
        this.setWeek_of_year(calendar.get(Calendar.WEEK_OF_YEAR));
        this.setDay_of_year(calendar.get(Calendar.DAY_OF_YEAR));
    }

    private String getFrom() {
        return new JSONObject(this.getData()).optString("from");
    }

    public String getType() {
        return new JSONObject(this.getData()).optString("type");
    }

    private String getSubType() {
        return new JSONObject(this.getData()).optString("sub_type");
    }

    public boolean isFromDashboardClick() {
        return this.getFrom().equals("DashboardClick");
    }

    public boolean isFromExtension() {
        return this.getFrom().equals("Extension");
    }

    public boolean isFromFillIn() {
        return this.getFrom().equals("FillIn");
    }

    public boolean isFromCopy() {
        return this.getFrom().toLowerCase().contains("copy");
    }

    public boolean isClassicAppUsed() {
        return !this.isAnyAppUsed();
    }

    public boolean isAnyAppUsed() {
        return this.getType().equals("any") || this.getSubType().equals("any");
    }

    public boolean isSingle() {
        return this.getType().toLowerCase().contains("single");
    }

    public boolean isEnterprise() {
        return this.getType().toLowerCase().contains("enterprise");
    }

    public boolean isFromCatalog() {
        return this.getFrom().equalsIgnoreCase("Catalog");
    }

    public boolean isFromUpdate() {
        return this.getFrom().equalsIgnoreCase("Update");
    }

    public boolean isFromImportation() {
        return this.getFrom().equalsIgnoreCase("Importation");
    }

    public boolean isBookmark() {
        return this.getType().toLowerCase().contains("link") || this.getSubType().equalsIgnoreCase("link");
    }

    public boolean isClassic() {
        return this.getType().toLowerCase().contains("classic") || this.getSubType().equalsIgnoreCase("classic");
    }

    public boolean isSoftware() {
        return this.getType().toLowerCase().contains("software") || this.getSubType().equalsIgnoreCase("software");
    }

    public boolean isAny() {
        return this.getType().toLowerCase().contains("any") || this.getSubType().equalsIgnoreCase("any");
    }

    public JSONObject getJsonData() {
        return new JSONObject(this.getData());
    }
}
