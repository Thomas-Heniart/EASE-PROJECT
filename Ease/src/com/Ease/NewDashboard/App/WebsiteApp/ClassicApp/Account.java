package com.Ease.NewDashboard.App.WebsiteApp.ClassicApp;

import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManagerHibernate;

import javax.persistence.*;
import java.util.List;

/**
 * Created by thomas on 25/04/2017.
 */
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    List<AccountInformation> accountInformationList;

    public Account(List<AccountInformation> accountInformationList) {
        this.accountInformationList = accountInformationList;
    }

    public Account() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public List<AccountInformation> getAccountInformationList() {
        return accountInformationList;
    }

    public void setAccountInformationList(List<AccountInformation> accountInformationList) {
        this.accountInformationList = accountInformationList;
    }

    public void editInformation(String info_name, String info_value, ServletManagerHibernate sm) throws GeneralException {
        for (AccountInformation accountInformation : this.getAccountInformationList()) {
            if (accountInformation.getInformation_name().equals(info_name)) {
                if (info_name.equals("password"))
                    accountInformation.setInformation_value(sm.getUser().getKeys().encrypt(info_value));
                else
                    accountInformation.setInformation_value(info_value);
            }
        }
    }
}
