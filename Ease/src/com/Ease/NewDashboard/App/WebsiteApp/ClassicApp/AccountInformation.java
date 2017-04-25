package com.Ease.NewDashboard.App.WebsiteApp.ClassicApp;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 25/04/2017.
 */
@Entity
@Table(name = "accountsInformations")
public class AccountInformation {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "information_name")
    String information_name;

    @Column(name = "information_value")
    String information_value;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    protected Account account;

    public AccountInformation(String information_name, String information_value, Account account) {
        this.information_name = information_name;
        this.information_value = information_value;
        this.account = account;
    }

    public AccountInformation(String information_name, String information_value) {
        this.information_name = information_name;
        this.information_value = information_value;
    }

    public AccountInformation() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getInformation_name() {
        return information_name;
    }

    public void setInformation_name(String information_name) {
        this.information_name = information_name;
    }

    public String getInformation_value() {
        return information_value;
    }

    public void setInformation_value(String information_value) {
        this.information_value = information_value;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static List<AccountInformation> createInformationsFromMapForAccount(Map<String, String> infos, Account account) {
        List<AccountInformation> accountInformationList = new LinkedList<AccountInformation>();
        for (Map.Entry<String, String> entry : infos.entrySet())
            accountInformationList.add(new AccountInformation(entry.getKey(), entry.getValue(), account));
        return accountInformationList;
    }
}
