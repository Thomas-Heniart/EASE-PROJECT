package com.Ease.NewDashboard.App.LinkApp;

import com.Ease.NewDashboard.App.App;
import com.Ease.NewDashboard.App.AppInformation;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManagerHibernate;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 21/04/2017.
 */

@Entity
@Table(name = "linkApps")
@PrimaryKeyJoinColumn(name = "id")
public class LinkApp extends App {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "link_app_info_id")
    protected LinkAppInformation linkAppInformation;

    public LinkApp(String type, Date insert_date, AppInformation appInformation, LinkAppInformation linkAppInformation) {
        super(type, insert_date, appInformation);
        this.linkAppInformation = linkAppInformation;
    }

    public LinkApp() {
    }

    public LinkAppInformation getLinkAppInformation() {
        return linkAppInformation;
    }

    public void setLinkAppInformation(LinkAppInformation linkAppInformation) {
        this.linkAppInformation = linkAppInformation;
    }

    @Override
    public String toString() {
        return super.toString() + this.linkAppInformation.toString();
    }

    @Override
    public void edit(JSONObject editJson, ServletManagerHibernate sm) throws GeneralException {
        super.edit(editJson, sm);
        String url = (String) editJson.get("url");
        if (url == null)
            return;
        this.linkAppInformation.setUrl(url);
    }
}
