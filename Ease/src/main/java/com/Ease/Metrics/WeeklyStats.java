package com.Ease.Metrics;

import com.Ease.Hibernate.HibernateQuery;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "WEEKLY_STATS")
public class WeeklyStats {

    public static WeeklyStats retrieveWeeklyStats(Integer year, Integer week_of_year, HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT w FROM WeeklyStats w WHERE w.year = :year AND w.week = :week_of_year");
        hibernateQuery.setParameter("year", year);
        hibernateQuery.setParameter("week_of_year", week_of_year);
        WeeklyStats weeklyStats = (WeeklyStats) hibernateQuery.getSingleResult();
        if (weeklyStats == null)
            weeklyStats = new WeeklyStats(year, week_of_year);
        return weeklyStats;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "week_of_year")
    private Integer week;

    @Column(name = "new_companies")
    private Integer new_companies;

    @Column(name = "new_users")
    private Integer new_users;

    @Column(name = "new_apps")
    private Integer new_apps;

    @Column(name = "new_team_apps")
    private Integer new_team_apps;

    @Column(name = "passwords_killed")
    private Integer passwords_killed;

    @Column(name = "active_users")
    private Integer active_users;

    @Column(name = "active_teams")
    private Integer active_teams;

    public WeeklyStats() {
    }

    private WeeklyStats(Integer year, Integer week) {
        this.year = year;
        this.week = week;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public Integer getNew_companies() {
        return new_companies;
    }

    public void setNew_companies(Integer new_companies) {
        this.new_companies = new_companies;
    }

    public Integer getNew_users() {
        return new_users;
    }

    public void setNew_users(Integer new_users) {
        this.new_users = new_users;
    }

    public Integer getNew_apps() {
        return new_apps;
    }

    public void setNew_apps(Integer new_apps) {
        this.new_apps = new_apps;
    }

    public Integer getNew_team_apps() {
        return new_team_apps;
    }

    public void setNew_team_apps(Integer new_team_apps) {
        this.new_team_apps = new_team_apps;
    }

    public Integer getPasswords_killed() {
        return passwords_killed;
    }

    public void setPasswords_killed(Integer passwords_killed) {
        this.passwords_killed = passwords_killed;
    }

    public Integer getActive_users() {
        return active_users;
    }

    public void setActive_users(Integer active_users) {
        this.active_users = active_users;
    }

    public Integer getActive_teams() {
        return active_teams;
    }

    public void setActive_teams(Integer active_teams) {
        this.active_teams = active_teams;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("new_companies", new_companies);
        res.put("new_users", new_users);
        res.put("new_apps", new_apps);
        res.put("new_team_apps", new_team_apps);
        res.put("passwords_killed", passwords_killed);
        res.put("active_users", active_users);
        res.put("active_teams", active_teams);
        res.put("week", week + " - " + year);
        return res;
    }

    public void updateValues(Integer new_companies, Integer new_users, Integer new_apps, Integer new_team_apps, Integer passwords_killed, Integer active_users, Integer active_teams) {
        this.setNew_companies(new_companies);
        this.setNew_users(new_users);
        this.setNew_apps(new_apps);
        this.setNew_team_apps(new_team_apps);
        this.setPasswords_killed(passwords_killed);
        this.setActive_users(active_users);
        this.setActive_teams(active_teams);
    }
}
