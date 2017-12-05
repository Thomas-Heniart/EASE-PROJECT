package com.Ease.API.V1.Dashboard;

import com.Ease.NewDashboard.*;
import com.Ease.Team.Team;
import com.Ease.User.User;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/dashboard/GetApps")
public class ServletGetApps extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            JSONArray res = new JSONArray();
            for (App app : user.getApps()) {
                String symmetric_key = null;
                String team_key = null;
                if (app.getTeamCardReceiver() != null) {
                    Team team = app.getTeamCardReceiver().getTeamCard().getTeam();
                    if (!sm.getTeamUser(team).isDisabled())
                        team_key = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
                } else {
                    symmetric_key = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
                    if (app.isLogWithApp()) {
                        WebsiteApp websiteApp = ((LogWithApp) app).getLoginWith_app();
                        if (websiteApp != null) {
                            if (websiteApp.getTeamCardReceiver() != null) {
                                Team team = websiteApp.getTeamCardReceiver().getTeamCard().getTeam();
                                if (!sm.getTeamUser(team).isDisabled())
                                    team_key = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
                            }
                        }
                    }
                }
                if ((symmetric_key != null && !symmetric_key.equals("")) || (team_key != null && !team_key.equals(""))) {
                    if (app.isClassicApp() && symmetric_key != null && !symmetric_key.equals("")) {
                        ClassicApp classicApp = (ClassicApp) app;
                        Account account = classicApp.getAccount();
                        if (account != null) {
                            if (account.getPrivate_key() == null || account.getPublic_key() == null || account.getPrivate_key().equals("") || account.getPublic_key().equals("")) {
                                Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
                                account.setPublic_key(publicAndPrivateKey.getKey());
                                account.setPrivate_key(AES.encrypt(publicAndPrivateKey.getValue(), symmetric_key));
                                sm.saveOrUpdate(account);
                                for (AccountInformation accountInformation : account.getAccountInformationSet()) {
                                    String value = accountInformation.getInformation_value();
                                    if (accountInformation.getInformation_name().equals("password"))
                                        value = AES.decrypt(value, symmetric_key);
                                    if (value == null)
                                        value = "";
                                    accountInformation.setInformation_value(RSA.Encrypt(value, account.getPublic_key()));
                                    sm.saveOrUpdate(accountInformation);
                                }
                            }
                        }
                    } else if (app.isLogWithApp() && symmetric_key != null && !symmetric_key.equals("")) {
                        LogWithApp logWithApp = (LogWithApp) app;
                        if (logWithApp.getLoginWith_app().isClassicApp()) {
                            ClassicApp classicApp = (ClassicApp) logWithApp.getLoginWith_app();
                            Account account = classicApp.getAccount();
                            if (account != null) {
                                if (account.getPrivate_key() == null || account.getPublic_key() == null || account.getPrivate_key().equals("") || account.getPublic_key().equals("")) {
                                    Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
                                    account.setPublic_key(publicAndPrivateKey.getKey());
                                    account.setPrivate_key(AES.encrypt(publicAndPrivateKey.getValue(), symmetric_key));
                                    sm.saveOrUpdate(account);
                                    for (AccountInformation accountInformation : account.getAccountInformationSet()) {
                                        String value = accountInformation.getInformation_value();
                                        if (accountInformation.getInformation_name().equals("password"))
                                            value = AES.decrypt(value, symmetric_key);
                                        if (value == null)
                                            value = "";
                                        accountInformation.setInformation_value(RSA.Encrypt(value, account.getPublic_key()));
                                        sm.saveOrUpdate(accountInformation);
                                    }
                                }
                            }
                        }
                    }
                    app.decipher(symmetric_key, team_key);
                }
                res.add(app.getJson());
            }
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
