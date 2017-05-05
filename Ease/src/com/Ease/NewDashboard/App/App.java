package com.Ease.NewDashboard.App;

import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManagerHibernate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 20/04/2017.
 */
@Entity
@Table(name = "apps")
@Inheritance(strategy = InheritanceType.JOINED)
public class App {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "type")
    protected String type;

    @Column(name = "insert_date")
    protected Date insert_date;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "app_info_id")
    protected AppInformation appInformation;

    public App(String type, Date insert_date, AppInformation appInformation) {
        this.type = type;
        this.insert_date = insert_date;
        this.appInformation = appInformation;
    }

    public App() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(Date insert_date) {
        this.insert_date = insert_date;
    }

    public AppInformation getAppInformation() {
        return appInformation;
    }

    public void setAppInformation(AppInformation appInformation) {
        this.appInformation = appInformation;
    }

    @Override
    public String toString() {
        return "Type: " + this.type + ", ";
    }

    public void edit(JSONObject editJson, ServletManagerHibernate sm) throws GeneralException {
        String name = (String) editJson.get("name");
        if (name == null)
            return;
        this.appInformation.setName(name);
    }
}
