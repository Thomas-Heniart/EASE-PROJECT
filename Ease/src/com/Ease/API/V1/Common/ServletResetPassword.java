package com.Ease.API.V1.Common;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.NewDashboard.LogWithApp;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.User.PasswordLost;
import com.Ease.User.User;
import com.Ease.User.UserKeys;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Map;

@WebServlet("/resetPassword")
public class ServletResetPassword extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getStringParam("email", true, false);
            String code = sm.getStringParam("linkCode", true, false);
            String password = sm.getStringParam("password", false, false);
            if (!Regex.isPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid password");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM User u WHERE u.email = :email");
            hibernateQuery.setParameter("email", email);
            User user = (User) hibernateQuery.getSingleResult();
            if (user == null)
                throw new HttpServletException(HttpStatus.Forbidden);
            hibernateQuery.queryString("SELECT p FROM PasswordLost p WHERE p.user_id = :id AND p.code = :code");
            hibernateQuery.setParameter("id", user.getDb_id());
            hibernateQuery.setParameter("code", code);
            PasswordLost passwordLost = (PasswordLost) hibernateQuery.getSingleResult();
            if (passwordLost == null)
                throw new HttpServletException(HttpStatus.BadRequest);
            String keyUser = AES.keyGenerator();
            String salt = AES.generateSalt();
            UserKeys userKeys = user.getUserKeys();
            userKeys.setKeyUser(AES.encryptUserKey(keyUser, password, salt));
            userKeys.setSaltPerso(salt);
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            userKeys.setPrivateKey(AES.encrypt(publicAndPrivateKey.getValue(), keyUser));
            userKeys.setPublicKey(publicAndPrivateKey.getKey());
            userKeys.setHashed_password(Hashing.hash(password));
            sm.saveOrUpdate(userKeys);
            ((Map<Integer, Map<String, Object>>) sm.getContextAttr("userIdMap")).remove(user.getDb_id());
            if (user.getJsonWebToken() != null) {
                Key secret = (Key) sm.getContextAttr("secret");
                user.getJsonWebToken().renew(keyUser, user.getDb_id(), secret);
                sm.saveOrUpdate(user.getJsonWebToken());
            }
            MailJetBuilder mailJetBuilder;
            for (TeamUser teamUser : user.getTeamUsers()) {
                teamUser.setDisabled(true);
                teamUser.setDisabledDate(new Date());
                sm.saveOrUpdate(teamUser);
                Team team = teamUser.getTeam();
                if (teamUser.isTeamOwner() && team.getTeamUsers().size() == 1) {
                    String teamKey = AES.keyGenerator();
                    teamUser.setTeamKey(AES.encrypt(teamKey, keyUser));
                    teamUser.setDisabled(false);
                    sm.saveOrUpdate(teamUser);
                    for (TeamCard teamCard : team.getTeamCardMap().values()) {
                        if (teamCard.isTeamSingleCard()) {
                            ((TeamSingleCard) teamCard).setAccount(null);
                            teamCard.getTeamCardReceiverMap().values().forEach(teamCardReceiver -> ((ClassicApp) teamCardReceiver.getApp()).setAccount(null));
                        } else if (teamCard.isTeamEnterpriseCard()) {
                            teamCard.getTeamCardReceiverMap().values().forEach(teamCardReceiver -> ((ClassicApp) teamCardReceiver.getApp()).setAccount(null));
                        }
                        sm.saveOrUpdate(teamCard);
                    }
                } else if (teamUser.isTeamOwner()) {
                    mailJetBuilder = new MailJetBuilder();
                    mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                    mailJetBuilder.setTemplateId(211068);
                    mailJetBuilder.addTo("benjamin@ease.space");
                    mailJetBuilder.addVariable("first_name", teamUser.getFirstName());
                    mailJetBuilder.addVariable("last_name", teamUser.getLastName());
                    mailJetBuilder.addVariable("team_name", team.getName());
                    mailJetBuilder.addVariable("team_email", teamUser.getEmail());
                    mailJetBuilder.addVariable("email", email);
                    mailJetBuilder.addVariable("phone_number", teamUser.getPhone_number());
                    mailJetBuilder.sendEmail();
                } else {
                    TeamUser admin = team.getTeamUserWithId(teamUser.getAdmin_id());
                    mailJetBuilder = new MailJetBuilder();
                    mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                    mailJetBuilder.addTo(admin.getEmail());
                    mailJetBuilder.setTemplateId(211034);
                    mailJetBuilder.addVariable("first_name", teamUser.getFirstName());
                    mailJetBuilder.addVariable("last_name", teamUser.getLastName());
                    mailJetBuilder.addVariable("team_name", team.getName());
                    mailJetBuilder.addVariable("link", Variables.URL_PATH + "#/teams/" + team.getDb_id() + "/@" + teamUser.getDb_id());
                    mailJetBuilder.sendEmail();
                    NotificationFactory.getInstance().createPasswordLostNotification(admin.getUser(), teamUser, team, sm.getUserWebSocketManager(admin.getUser().getDb_id()), hibernateQuery);
                }
            }
            user.getProfileSet().stream().flatMap(Profile::getApps).forEach(app -> {
                if (app.getTeamCardReceiver() != null) {

                } else if (app.isClassicApp()) {
                    ((ClassicApp) app).setAccount(null);
                    sm.saveOrUpdate(app);
                } else if (app.isLogWithApp()) {
                    ((LogWithApp) app).setLoginWith_app(null);
                    sm.saveOrUpdate(app);
                }
            });
            sm.deleteObject(passwordLost);
            sm.setSuccess("New password set");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getParam("email", true);
            String code = sm.getParam("code", true);
            if (email == null || email.equals("")) {
                throw new HttpServletException(HttpStatus.BadRequest, "Empty email.");
            } else if (code == null || code.equals("")) {
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid code.");
            }
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT id FROM users WHERE email = :email");
            hibernateQuery.setParameter("email", email);
            Integer userId = (Integer) hibernateQuery.getSingleResult();
            if (userId == null)
                throw new HttpServletException(HttpStatus.Forbidden);
            DataBaseConnection db = sm.getDB();
            DatabaseRequest databaseRequest = db.prepareRequest("SELECT * FROM passwordLost WHERE (NOW() <= DATE_ADD(dateOfRequest, INTERVAL 2 HOUR)) AND user_id = ? AND linkCode = ?;");
            databaseRequest.setInt(userId);
            databaseRequest.setString(code);
            DatabaseResult rs = databaseRequest.get();
            hibernateQuery.commit();
            if (rs.next())
                sm.setRedirectUrl("newPassword.jsp?email=" + email + "&linkCode=" + code + "");
            else
                sm.setRedirectUrl("passwordLost?codeExpiration=true");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();

    }
}
