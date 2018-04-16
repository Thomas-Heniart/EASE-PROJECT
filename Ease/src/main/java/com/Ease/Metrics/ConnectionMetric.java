package com.Ease.Metrics;

import com.Ease.Hibernate.HibernateQuery;

import javax.persistence.*;

@Entity
@Table(name = "METRIC_CONNECTION")
public class ConnectionMetric {

    public static ConnectionMetric getMetricOrNull(Integer user_id, Integer year, Integer day_of_year, HibernateQuery hibernateQuery) {
        hibernateQuery.queryString("SELECT m FROM ConnectionMetric m WHERE m.user_id = :user_id AND m.year = :year AND m.day_of_year = :day_of_year");
        hibernateQuery.setParameter("user_id", user_id);
        hibernateQuery.setParameter("year", year);
        hibernateQuery.setParameter("day_of_year", day_of_year);
        ConnectionMetric metric = (ConnectionMetric) hibernateQuery.getSingleResult();
        return metric;
    }

    public static ConnectionMetric getMetric(Integer user_id, Integer year, Integer day_of_year, HibernateQuery hibernateQuery) {
        ConnectionMetric metric = getMetricOrNull(user_id, year, day_of_year, hibernateQuery);
        if (metric == null) {
            metric = new ConnectionMetric(user_id, year, day_of_year);
            hibernateQuery.saveOrUpdateObject(metric);
        }
        return metric;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "day_of_year")
    private Integer day_of_year;

    @Column(name = "connected")
    private boolean connected = false;

    public ConnectionMetric() {
    }

    public ConnectionMetric(Integer user_id, Integer year, Integer day_of_year) {
        this.user_id = user_id;
        this.year = year;
        this.day_of_year = day_of_year;
    }

    public ConnectionMetric(Integer user_id, Integer year, Integer day_of_year, boolean connected) {
        this.user_id = user_id;
        this.year = year;
        this.day_of_year = day_of_year;
        this.connected = connected;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getDay_of_year() {
        return day_of_year;
    }

    public void setDay_of_year(Integer day_of_year) {
        this.day_of_year = day_of_year;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
}
