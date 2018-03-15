package com.Ease.NewDashboard;

import com.Ease.Catalog.Software;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.HttpServletException;
import org.json.JSONObject;

import java.util.Map;

public class AppFactory {
    private static AppFactory ourInstance = new AppFactory();

    public static AppFactory getInstance() {
        return ourInstance;
    }

    private AppFactory() {
    }

    public App createLinkApp(String name, String url, String img_url) {
        return new LinkApp(new AppInformation(name), new LinkAppInformation(url, img_url));
    }

    public App createLinkApp(String name, String url, String img_url, Profile profile) {
        App app = this.createLinkApp(name, url, img_url);
        app.setProfile(profile);
        app.setPosition(profile.getSize());
        return app;
    }

    public App createClassicApp(String name, Website website, String symmetric_key, Account account_to_copy, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromAccount(account_to_copy, hibernateQuery);
        App app = this.createClassicApp(name, website);
        app.setAccount(account);
        return app;
    }

    public App createClassicApp(String name, Website website, String symmetric_key, Map<String, String> account_information, Integer password_reminder_interval, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromMap(account_information, symmetric_key, password_reminder_interval, hibernateQuery);
        App app = this.createClassicApp(name, website);
        app.setAccount(account);
        return app;

    }

    public App createClassicApp(String name, Website website, String symmetric_key, JSONObject account_information, Integer password_reminder_interval, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromJson(account_information, symmetric_key, password_reminder_interval, hibernateQuery);
        App app = this.createClassicApp(name, website);
        app.setAccount(account);
        return app;
    }

    public App createClassicApp(String name, Website website, String symmetric_key, Map<String, String> account_information, HibernateQuery hibernateQuery) throws HttpServletException {
        return this.createClassicApp(name, website, symmetric_key, account_information, 0, hibernateQuery);
    }

    public App createClassicApp(String name, Website website) {
        return new ClassicApp(new AppInformation(name), website);
    }

    public App createAnyApp(String name, Website website) {
        return new AnyApp(new AppInformation(name), website, null);
    }

    public App createAnyApp(String name, Website website, String symmetric_key, Map<String, String> account_information, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromMap(account_information, symmetric_key, 0, hibernateQuery);
        return new AnyApp(new AppInformation(name), website, account);
    }

    public App createAnyApp(String name, Website website, String symmetric_key, Map<String, String> account_information, Integer reminder_interval, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromMap(account_information, symmetric_key, reminder_interval, hibernateQuery);
        return new AnyApp(new AppInformation(name), website, account);
    }

    public App createAnyApp(String name, Website website, String symmetric_key, JSONObject account_information, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromJson(account_information, symmetric_key, 0, hibernateQuery);
        return new AnyApp(new AppInformation(name), website, account);
    }

    public App createSoftwareApp(String name, Software software, String symmetric_key, Map<String, String> account_information, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromMap(account_information, symmetric_key, 0, hibernateQuery);
        return new SoftwareApp(new AppInformation(name), software, account);
    }

    public App createSoftwareApp(String name, Software software, String symmetric_key, JSONObject account_information, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromJson(account_information, symmetric_key, 0, hibernateQuery);
        return new SoftwareApp(new AppInformation(name), software, account);
    }

    public App createSoftwareApp(String name, Software software, String symmetric_key, JSONObject account_information, Integer password_reminder_interval, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromJson(account_information, symmetric_key, password_reminder_interval, hibernateQuery);
        return new SoftwareApp(new AppInformation(name), software, account);
    }

    public App createSoftwareApp(String name, Software software) {
        return new SoftwareApp(new AppInformation(name), software);
    }

    public App createSoftwareApp(String name, Software software, String symmetric_key, Account account_to_copy, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromAccount(account_to_copy, hibernateQuery);
        return new SoftwareApp(new AppInformation(name), software, account);
    }
}
