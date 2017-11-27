package com.Ease.API.V1.Common;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.NewDashboard.*;
import com.Ease.User.User;
import com.Ease.User.UserEmail;
import com.Ease.User.UserFactory;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import com.mailjet.client.resource.ContactslistManageContact;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/common/Registration")
public class ServletRegistration extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            if (user != null)
                sm.setUser(null);
            String username = sm.getStringParam("username", true, false);
            String email = sm.getStringParam("email", true, false);
            String password = sm.getStringParam("password", false, false);
            String digits = sm.getStringParam("digits", false, true);
            String code = sm.getStringParam("code", false, true);
            Long registration_date = sm.getLongParam("registration_date", true, false);
            Boolean send_news = sm.getBooleanParam("newsletter", true, false);
            checkUsernameIntegrity(username);
            if (email == null || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid email");
            if (password == null || !Regex.isPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Password must be at least 8 characters, contains 1 uppercase, 1 lowercase and 1 digit.");
            if (registration_date == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid registration date");
            if ((digits == null || digits.length() != 6) && (code == null || code.equals("")))
                throw new HttpServletException(HttpStatus.BadRequest, "Missing parameter digits or code");
            if (send_news == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Newsletter cannot be null");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            if (code != null && !code.equals("")) {
                hibernateQuery.querySQLString("SELECT invitation_code FROM teamUsers WHERE teamUsers.email = ?");
                hibernateQuery.setParameter(1, email);
                String valid_code = (String) hibernateQuery.getSingleResult();
                if (valid_code == null)
                    throw new HttpServletException(HttpStatus.BadRequest, "No invitation for this email.");
                if (!valid_code.equals(code))
                    throw new HttpServletException(HttpStatus.BadRequest, "Invalid code.");
            } else {
                hibernateQuery.querySQLString("SELECT digits FROM userPendingRegistrations WHERE email = ?");
                hibernateQuery.setParameter(1, email);
                String db_digits = (String) hibernateQuery.getSingleResult();
                if (db_digits == null || db_digits.equals(""))
                    throw new HttpServletException(HttpStatus.BadRequest, "You didn't ask for an account.");
                if (!db_digits.equals(digits))
                    throw new HttpServletException(HttpStatus.BadRequest, "Invalid digits.");
            }
            User newUser = UserFactory.getInstance().createUser(email, username, password);
            sm.saveOrUpdate(newUser);
            UserEmail userEmail = new UserEmail(email, true, newUser);
            sm.saveOrUpdate(userEmail);
            newUser.addUserEmail(userEmail);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Profile profile_perso = new Profile(newUser, 0, 0, new ProfileInformation("Me"));
            /* Profile profile_pro = new Profile(newUser, 1, 0, new ProfileInformation("Pro"));
            sm.saveOrUpdate(profile_pro);
            newUser.addProfile(profile_pro); */
            sm.saveOrUpdate(profile_perso);

            newUser.addProfile(profile_perso);
            Website facebook = catalog.getWebsiteWithName("Facebook", hibernateQuery);
            Website gmail = catalog.getWebsiteWithName("Gmail", hibernateQuery);
            Website sncf = catalog.getWebsiteWithName("Voyages SNCF", hibernateQuery);
            ClassicApp facebookApp = new ClassicApp(new AppInformation(facebook.getName()), facebook);
            facebookApp.setProfile(profile_perso);
            facebookApp.setPosition(0);
            sm.saveOrUpdate(facebookApp);
            profile_perso.addApp(facebookApp);
            ClassicApp gmailApp = new ClassicApp(new AppInformation(gmail.getName()), gmail);
            gmailApp.setProfile(profile_perso);
            gmailApp.setPosition(1);
            sm.saveOrUpdate(gmailApp);
            profile_perso.addApp(gmailApp);
            ClassicApp sncfApp = new ClassicApp(new AppInformation(sncf.getName()), sncf);
            sncfApp.setProfile(profile_perso);
            sncfApp.setPosition(2);
            sm.saveOrUpdate(sncfApp);
            profile_perso.addApp(sncfApp);
            LinkApp lemonde = new LinkApp(new AppInformation("Le Monde"), new LinkAppInformation("http://www.lemonde.fr/", "https://logo.clearbit.com/lemonde.fr"));
            lemonde.setProfile(profile_perso);
            lemonde.setPosition(3);
            sm.saveOrUpdate(lemonde);
            profile_perso.addApp(lemonde);
            sm.setUser(newUser);
            String keyUser = newUser.getUserKeys().getDecipheredKeyUser(password);
            String privateKey = newUser.getUserKeys().getDecipheredPrivateKey(keyUser);
            Map<String, Object> userProperties = sm.getUserProperties(newUser.getDb_id());
            userProperties.put("keyUser", keyUser);
            userProperties.put("privateKey", privateKey);
            if (send_news) {
                MailJetBuilder mailJetBuilder = new MailJetBuilder(ContactslistManageContact.resource, 13300);
                mailJetBuilder.property(ContactslistManageContact.EMAIL, newUser.getEmail());
                mailJetBuilder.property(ContactslistManageContact.NAME, newUser.getUsername());
                mailJetBuilder.property(ContactslistManageContact.ACTION, "addnoforce");
                mailJetBuilder.post();
            }
            /* ((Map<String, User>) sm.getContextAttr("users")).put(email, newUser);
            ((Map<String, User>) sm.getContextAttr("sessionIdUserMap")).put(sm.getSession().getId(), newUser);
            ((Map<String, User>) sm.getContextAttr("sIdUserMap")).put(newUser.getSessionSave().getSessionId(), newUser); */
            sm.setSuccess(newUser.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void checkUsernameIntegrity(String username) throws HttpServletException {
        if (username == null || username.equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "Usernames can't be empty!");
        if (username.length() >= 22 || username.length() < 3)
            throw new HttpServletException(HttpStatus.BadRequest, "Sorry, usernames must be between 3 and 21 characters.");
        if (!username.equals(username.toLowerCase()) || !Regex.isValidUsername(username))
            throw new HttpServletException(HttpStatus.BadRequest, "Please choose a username that is all lowercase, containing only letters, numbers, periods, hyphens and underscores");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
