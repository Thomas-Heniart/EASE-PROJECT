package com.Ease.NewDashboard.App.WebsiteApp.ClassicApp;

import com.Ease.NewDashboard.App.AppInformation;
import com.Ease.NewDashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManagerHibernate;
import com.Ease.Website.Website;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by thomas on 25/04/2017.
 */
@Entity
@Table(name = "ClassicApps")
@PrimaryKeyJoinColumn(name = "id")
public class ClassicApp extends WebsiteApp {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    protected Account account;

    public ClassicApp(AppInformation appInformation, Website website, Account account) {
        super(appInformation, website, "classicApp");
        this.account = account;
    }

    public ClassicApp() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Canonical name: " + this.getClass().getCanonicalName() + ", Type name: " + this.getClass().getTypeName();
    }

    @Override
    public void edit(JSONObject editJson, ServletManagerHibernate sm) throws GeneralException {
        super.edit(editJson, sm);
        for (String info_name : this.website.getWebsiteInformationNames()) {
            String info_value = (String) editJson.get(info_name);
            if (info_value == null)
                continue;;
            this.account.editInformation(info_name, info_value, sm);
        }
    }
}
