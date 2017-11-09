package com.Ease.API.V1.Common;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.NewDashboard.*;
import com.Ease.Utils.*;
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
                user.logoutFromSession(sm.getSession().getId(), sm.getServletContext(), sm.getDB());
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
                hibernateQuery.querySQLString("SELECT code FROM pendingTeamInvitations JOIN teamUsers ON pendingTeamInvitations.teamUser_id = teamUsers.id WHERE teamUsers.email = ?");
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
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            User newUser = User.createUser(email, username, password, registration_date, sm.getServletContext(), db);
            if (send_news) {
                MailJetBuilder mailJetBuilder = new MailJetBuilder(ContactslistManageContact.resource, 13300);
                mailJetBuilder.property(ContactslistManageContact.EMAIL, newUser.getEmail());
                mailJetBuilder.property(ContactslistManageContact.NAME, newUser.getFirstName());
                mailJetBuilder.property(ContactslistManageContact.ACTION, "addnoforce");
                mailJetBuilder.post();
            }
            sm.setUser(newUser);
            ((Map<String, User>) sm.getContextAttr("users")).put(email, newUser);
            ((Map<String, User>) sm.getContextAttr("sessionIdUserMap")).put(sm.getSession().getId(), newUser);
            ((Map<String, User>) sm.getContextAttr("sIdUserMap")).put(newUser.getSessionSave().getSessionId(), newUser);
            //Profile school_profile = null;
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            /* if (email.endsWith("@iscparis.com")) {
                school_profile = newUser.getDashboardManager().addProfile("ISC Paris", "#7D0056", db);
                Website myIsc = catalog.getWebsiteWithName("My ISC");
                school_profile.addEmptyApp(myIsc.getName(), myIsc, db);
                Website moodle = catalog.getWebsiteWithName("Moodle");
                school_profile.addEmptyApp(moodle.getName(), moodle, db);
                Website jobTeaser = catalog.getWebsiteWithName("JobTeaser ISC");
                school_profile.addEmptyApp("JobTeaser", jobTeaser, db);
                Website iagora = catalog.getWebsiteWithName("Iagora");
                school_profile.addEmptyApp(iagora.getName(), iagora, db);
                Website talentoday = catalog.getWebsiteWithName("Talentoday");
                school_profile.addEmptyApp(talentoday.getName(), talentoday, db);
                Website scholarvox = catalog.getWebsiteWithName("Scholarvox");
                school_profile.addEmptyApp(scholarvox.getName(), scholarvox, db);
                Website housing_center = catalog.getWebsiteWithName("Housing Center");
                school_profile.addEmptyApp(housing_center.getName(), housing_center, db);
                Website centralTest = catalog.getWebsiteWithName("CentralTest");
                school_profile.addEmptyApp(centralTest.getName(), centralTest, db);
            } else if (email.endsWith("@ieseg.fr")) {
                school_profile = newUser.getDashboardManager().addProfile("IESEG", "#FFC300", db);
                Website ieseg_online = catalog.getWebsiteWithName("IESEG Online");
                school_profile.addEmptyApp(ieseg_online.getName(), ieseg_online, db);
                Website ieseg_network = catalog.getWebsiteWithName("Ieseg Network");
                school_profile.addEmptyApp(ieseg_network.getName(), ieseg_network, db);
                Website jobTeaser = catalog.getWebsiteWithName("JobTeaser Ieseg");
                school_profile.addEmptyApp("JobTeaser", jobTeaser, db);
                Website unify = catalog.getWebsiteWithName("Unify Iéseg");
                school_profile.addEmptyApp(unify.getName(), unify, db);
                Website office_mail = catalog.getWebsiteWithName("Office365 Mails");
                school_profile.addEmptyApp(office_mail.getName(), office_mail, db);
            } else if (email.endsWith("@edhec.com")) {
                school_profile = newUser.getDashboardManager().addProfile("EDHEC", "#A51B35", db);
                Website aurion = catalog.getWebsiteWithName("Aurion");
                school_profile.addEmptyApp(aurion.getName(), aurion, db);
                Website blackboard = catalog.getWebsiteWithName("Blackboard");
                school_profile.addEmptyApp(blackboard.getName(), blackboard, db);
                Website jobTeaser = catalog.getWebsiteWithName("JobTeaser Edhec");
                school_profile.addEmptyApp("JobTeaser", jobTeaser, db);
                Website officeMail = catalog.getWebsiteWithName("Office365 Mails");
                school_profile.addEmptyApp(officeMail.getName(), officeMail, db);
                Website print = catalog.getWebsiteWithName("Everyon Print");
                school_profile.addEmptyApp("Everyone Print", print, db);
                Website workplace = catalog.getWebsiteWithName("Workplace");
                school_profile.addEmptyApp(workplace.getName(), workplace, db);
            } else { */
            newUser.initializeDashboardManager(hibernateQuery);
            ProfileInformation perso_information = new ProfileInformation("Me");
            sm.saveOrUpdate(perso_information);
            ProfileInformation pro_information = new ProfileInformation("Pro");
            sm.saveOrUpdate(pro_information);
            Profile profile_perso = new Profile(Integer.valueOf(newUser.getDBid()), 0, 0, perso_information);
            Profile profile_pro = new Profile(Integer.valueOf(newUser.getDBid()), 1, 0, pro_information);
            sm.saveOrUpdate(profile_perso);
            sm.saveOrUpdate(profile_pro);
            newUser.getDashboardManager().addProfile(profile_perso);
            newUser.getDashboardManager().addProfile(profile_pro);
            Website facebook = catalog.getWebsiteWithName("Facebook");
            Website gmail = catalog.getWebsiteWithName("Gmail");
            Website sncf = catalog.getWebsiteWithName("Voyages SNCF");
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
            newUser.getDashboardManager().addApp(facebookApp);
            newUser.getDashboardManager().addApp(gmailApp);
            newUser.getDashboardManager().addApp(sncfApp);
            newUser.getDashboardManager().addApp(lemonde);
            //}
            db.commitTransaction(transaction);
            sm.setSuccess(newUser.getJson());

        } catch (GeneralException e) {
            sm.setError(new HttpServletException(HttpStatus.BadRequest, e.getMsg()));
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
