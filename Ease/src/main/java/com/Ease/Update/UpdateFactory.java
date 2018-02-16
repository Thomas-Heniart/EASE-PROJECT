package com.Ease.Update;

import com.Ease.Catalog.Website;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import org.json.JSONObject;

public class UpdateFactory {
    private static UpdateFactory ourInstance = new UpdateFactory();

    public static UpdateFactory getInstance() {
        return ourInstance;
    }

    private UpdateFactory() {
    }

    private Update createUpdate(User user, JSONObject account_information) throws HttpServletException {
        Update update = new Update();
        update.setUser(user);
        UpdateAccount updateAccount = new UpdateAccount();
        String publicKey = user.getUserKeys().getPublicKey();
        for (Object o : account_information.keySet()) {
            String key = (String) o;
            String value = account_information.getString(key);
            updateAccount.addUpdateAccountInformation(new UpdateAccountInformation(key, RSA.Encrypt(value, publicKey), updateAccount, value));
        }
        update.setUpdateAccount(updateAccount);
        return update;
    }

    public Update createUpdate(User user, JSONObject account_information, Website website) throws HttpServletException {
        Update update = this.createUpdate(user, account_information);
        if (website.getWebsiteAttributes().isIntegrated())
            update.setWebsite(website);
        else
            update.setUrl(website.getLogin_url());
        return update;
    }

    public Update createUpdate(User user, JSONObject account_information, String url) throws HttpServletException {
        Update update = this.createUpdate(user, account_information);
        update.setUrl(url);
        return update;
    }

    public Update createUpdate(User user, JSONObject account_information, WebsiteApp websiteApp) throws HttpServletException {
        Update update = this.createUpdate(user, account_information);
        update.setApp(websiteApp);
        if (websiteApp.getTeamCardReceiver() != null)
            update.setTeamCard(websiteApp.getTeamCardReceiver().getTeamCard());
        update.setWebsite(websiteApp.getWebsite());
        return update;
    }

    public Update createUpdate(User user, JSONObject account_information, TeamSingleCard teamSingleCard, TeamUser teamUser) throws HttpServletException {
        Update update = this.createUpdate(user, account_information);
        update.setWebsite(teamSingleCard.getWebsite());
        update.setTeamCard(teamSingleCard);
        update.setTeamUser(teamUser);
        return update;
    }
}
