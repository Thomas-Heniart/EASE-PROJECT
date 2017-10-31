package com.Ease.App;

import javax.persistence.*;

@Entity
@Table(name = "apps")
@Inheritance(strategy=InheritanceType.JOINED)
public class App {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_info_id")
    private AppInformation appInformation;

    @Column(name = "type")
    private String type;

    public App() {

    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public AppInformation getAppInformation() {
        return appInformation;
    }

    public void setAppInformation(AppInformation appInformation) {
        this.appInformation = appInformation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}