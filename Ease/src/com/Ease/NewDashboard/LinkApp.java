package com.Ease.NewDashboard;

import javax.persistence.*;

@Entity
@Table(name = "linkApps")
@PrimaryKeyJoinColumn(name = "id")
public class LinkApp extends App {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "link_app_info_id")
    private LinkAppInformation linkAppInformation;


    public LinkApp() {

    }

    public LinkApp(AppInformation appInformation, String type, LinkAppInformation linkAppInformation) {
        super(appInformation, type);
        this.linkAppInformation = linkAppInformation;
    }

    public LinkAppInformation getLinkAppInformation() {
        return linkAppInformation;
    }

    public void setLinkAppInformation(LinkAppInformation linkAppInformation) {
        this.linkAppInformation = linkAppInformation;
    }
}