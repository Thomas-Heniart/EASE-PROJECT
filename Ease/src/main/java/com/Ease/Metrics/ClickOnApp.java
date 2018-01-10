package com.Ease.Metrics;

import com.Ease.Hibernate.HibernateQuery;

import javax.persistence.*;

@Entity
@Table(name = "metricClickOnApp")
public class ClickOnApp {

    public static ClickOnApp getMetricForApp(Integer app_id, int year, int week_of_year, HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT c FROM ClickOnApp c WHERE c.app_id = :app_id AND c.week_of_year = :week AND c.year = :year");
        hibernateQuery.setParameter("year", year);
        hibernateQuery.setParameter("week", week_of_year);
        hibernateQuery.setParameter("app_id", app_id);
        ClickOnApp metric = (ClickOnApp) hibernateQuery.getSingleResult();
        if (metric == null) {
            metric = new ClickOnApp(app_id, year, week_of_year);
            hibernateQuery.querySQLString("SELECT team_id FROM teamCards JOIN teamCardReceivers ON teamCardReceivers.teamCard_id = teamCards.id WHERE teamCardReceivers.app_id = :id");
            hibernateQuery.setParameter("id", app_id);
            Integer team_id = (Integer) hibernateQuery.getSingleResult();
            if (team_id != null)
                metric.setTeam_id(team_id);
        }
        return metric;
    }

    public static void incrementClickOnApp(Integer app_id, int year, int week_of_year, int day_number, HibernateQuery hibernateQuery) {
        ClickOnApp metric = getMetricForApp(app_id, year, week_of_year, hibernateQuery);
        metric.incrementDay(day_number);
        hibernateQuery.saveOrUpdateObject(metric);
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long db_id;

    @Column(name = "app_id")
    private Integer app_id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "week_of_year")
    private Integer week_of_year;

    @Column(name = "day_0")
    private Integer day_one = 0;

    @Column(name = "day_1")
    private Integer day_two = 0;

    @Column(name = "day_2")
    private Integer day_three = 0;

    @Column(name = "day_3")
    private Integer day_four = 0;

    @Column(name = "day_4")
    private Integer day_five = 0;

    @Column(name = "day_5")
    private Integer day_six = 0;

    @Column(name = "day_6")
    private Integer day_seven = 0;

    @Column(name = "team_id")
    private Integer team_id;

    @Transient
    private Integer[] days;

    public ClickOnApp() {

    }

    private ClickOnApp(Integer app_id, Integer year, Integer week_of_year) {
        this.app_id = app_id;
        this.year = year;
        this.week_of_year = week_of_year;
    }

    public Long getDb_id() {
        return db_id;
    }

    public void setDb_id(Long db_id) {
        this.db_id = db_id;
    }

    public Integer getApp_id() {
        return app_id;
    }

    public void setApp_id(Integer app_id) {
        this.app_id = app_id;
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

    public Integer getDay_one() {
        return day_one;
    }

    public void setDay_one(Integer day_one) {
        this.day_one = day_one;
    }

    public Integer getDay_two() {
        return day_two;
    }

    public void setDay_two(Integer day_two) {
        this.day_two = day_two;
    }

    public Integer getDay_three() {
        return day_three;
    }

    public void setDay_three(Integer day_three) {
        this.day_three = day_three;
    }

    public Integer getDay_four() {
        return day_four;
    }

    public void setDay_four(Integer day_four) {
        this.day_four = day_four;
    }

    public Integer getDay_five() {
        return day_five;
    }

    public void setDay_five(Integer day_five) {
        this.day_five = day_five;
    }

    public Integer getDay_six() {
        return day_six;
    }

    public void setDay_six(Integer day_six) {
        this.day_six = day_six;
    }

    public Integer getDay_seven() {
        return day_seven;
    }

    public void setDay_seven(Integer day_seven) {
        this.day_seven = day_seven;
    }

    public Integer getTeam_id() {
        return team_id;
    }

    public void setTeam_id(Integer team_id) {
        this.team_id = team_id;
    }

    public Integer[] getDays() {
        if (days == null)
            days = new Integer[]{this.getDay_one(), this.getDay_two(), this.getDay_three(), this.getDay_four(), this.getDay_five(), this.getDay_six(), this.getDay_seven()};
        return days;
    }

    private void incrementDay(int day_number) {
        switch (day_number) {
            case 1:
                this.setDay_seven(this.getDay_seven() + 1);
                break;
            case 2:
                this.setDay_one(this.getDay_one() + 1);
                break;
            case 3:
                this.setDay_two(this.getDay_two() + 1);
                break;
            case 4:
                this.setDay_three(this.getDay_three() + 1);
                break;
            case 5:
                this.setDay_four(this.getDay_four() + 1);
                break;
            case 6:
                this.setDay_five(this.getDay_five() + 1);
                break;
            case 7:
                this.setDay_six(this.getDay_six() + 1);
                break;
            default:
                break;
        }
    }

    public boolean hasBeenClickedForDays(int number_of_days) {
        int days_clicked = 0;
        if (this.day_one > 0)
            days_clicked++;
        if (this.day_two > 0)
            days_clicked++;
        if (this.day_three > 0)
            days_clicked++;
        if (this.day_four > 0)
            days_clicked++;
        if (this.day_five > 0)
            days_clicked++;
        if (this.day_six > 0)
            days_clicked++;
        if (this.day_seven > 0)
            days_clicked++;
        return days_clicked >= number_of_days;
    }
}