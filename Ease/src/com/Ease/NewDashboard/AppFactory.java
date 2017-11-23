package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
import com.Ease.Utils.HttpServletException;
import org.json.simple.JSONObject;

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

    public App createClassicApp(String name, Website website, String symmetric_key, Account account_to_copy) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromAccount(account_to_copy, symmetric_key);
        App app = this.createClassicApp(name, website);
        ((ClassicApp) app).setAccount(account);
        return app;
    }

    public App createClassicApp(String name, Website website, String symmetric_key, Map<String, String> account_information, Integer password_reminder_interval) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromMap(account_information, symmetric_key, password_reminder_interval);
        App app = this.createClassicApp(name, website);
        ((ClassicApp) app).setAccount(account);
        return app;

    }

    public App createClassicApp(String name, Website website, String symmetric_key, JSONObject account_information, Integer password_reminder_interval) throws HttpServletException {
        Account account = AccountFactory.getInstance().createAccountFromJson(account_information, symmetric_key, password_reminder_interval);
        App app = this.createClassicApp(name, website);
        ((ClassicApp) app).setAccount(account);
        return app;
    }

    public App createClassicApp(String name, Website website, String symmetric_key, Map<String, String> account_information) throws HttpServletException {
        return this.createClassicApp(name, website, symmetric_key, account_information, 0);
    }

    public App createClassicApp(String name, Website website) {
        return new ClassicApp(new AppInformation(name), website);
    }
}
