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

    public App createLinkApp(String name, String url, String imgUrl) {
        return new LinkApp(new AppInformation(name), new LinkAppInformation(url, imgUrl));
    }

    public App createLinkApp(String name, String url, String imgUrl, Profile profile) {
        App app = this.createLinkApp(name, url, imgUrl);
        app.setProfile(profile);
        app.setPosition(profile.getSize());
        return app;
    }

    public App createClassicApp(String name, Website website, Account accountToCopy, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromAccount(accountToCopy, hibernateQuery);
        return new ClassicApp(new AppInformation(name), website, account);
    }

    public App createClassicApp(String name, Website website, String symmetricKey, Map<String, String> accountInformation, Integer passwordReminderInterval, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromMap(accountInformation, symmetricKey, passwordReminderInterval, hibernateQuery);
        return new ClassicApp(new AppInformation(name), website, account);
    }

    public App createClassicApp(String name, Website website, String symmetricKey, JSONObject accountInformation, Integer passwordReminderInterval, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromJson(accountInformation, symmetricKey, passwordReminderInterval, hibernateQuery);
        return new ClassicApp(new AppInformation(name), website, account);
    }

    public App createClassicApp(String name, Website website, Account account) {
        return new ClassicApp(new AppInformation(name), website, account);
    }

    public App createClassicApp(String name, Website website, String symmetricKey, Map<String, String> accountInformation, HibernateQuery hibernateQuery) throws HttpServletException {
        return this.createClassicApp(name, website, symmetricKey, accountInformation, 0, hibernateQuery);
    }

    public App createClassicApp(String name, Website website) {
        App app = new ClassicApp(new AppInformation(name), website);
        app.setNewApp(false);
        return app;
    }

    public App createAnyApp(String name, Website website) {
        App app = new AnyApp(new AppInformation(name), website, null);
        app.setNewApp(false);
        return app;
    }

    public App createAnyApp(String name, Website website, String symmetricKey, Map<String, String> accountInformation, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromMap(accountInformation, symmetricKey, 0, hibernateQuery);
        return new AnyApp(new AppInformation(name), website, account);
    }

    public App createAnyApp(String name, Website website, String symmetricKey, Map<String, String> accountInformation, Integer reminderInterval, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromMap(accountInformation, symmetricKey, reminderInterval, hibernateQuery);
        return new AnyApp(new AppInformation(name), website, account);
    }

    public App createAnyApp(String name, Website website, String symmetricKey, JSONObject accountInformation, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromJson(accountInformation, symmetricKey, 0, hibernateQuery);
        return new AnyApp(new AppInformation(name), website, account);
    }

    public App createAnyApp(String name, Website website, Account account) {
        return new AnyApp(new AppInformation(name), website, account);
    }

    public App createSoftwareApp(String name, Software software, String symmetricKey, Map<String, String> accountInformation, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromMap(accountInformation, symmetricKey, 0, hibernateQuery);
        return new SoftwareApp(new AppInformation(name), software, account);
    }

    public App createSoftwareApp(String name, Software software, String symmetricKey, JSONObject accountInformation, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromJson(accountInformation, symmetricKey, 0, hibernateQuery);
        return new SoftwareApp(new AppInformation(name), software, account);
    }

    public App createSoftwareApp(String name, Software software, String symmetricKey, JSONObject accountInformation, Integer passwordReminderInterval, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromJson(accountInformation, symmetricKey, passwordReminderInterval, hibernateQuery);
        return new SoftwareApp(new AppInformation(name), software, account);
    }

    public App createSoftwareApp(String name, Software software) {
        App app = new SoftwareApp(new AppInformation(name), software);
        app.setNewApp(false);
        return app;
    }

    public App createSoftwareApp(String name, Software software, Account accountToCopy, HibernateQuery hibernateQuery) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromAccount(accountToCopy, hibernateQuery);
        return new SoftwareApp(new AppInformation(name), software, account);
    }

    public App createSoftwareApp(String name, Software software, Account account) {
        return new SoftwareApp(new AppInformation(name), software, account);
    }
}
