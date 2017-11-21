package com.Ease.API.V1.Schools;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.NewDashboard.AppInformation;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.ProfileInformation;
import com.Ease.User.User;
import com.Ease.User.UserFactory;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import com.mailjet.client.resource.ContactslistManageContact;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/RegistrationIeseg")
public class ServletRegistrationIeseg extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            String username = sm.getStringParam("username", true, true);
            String email = sm.getStringParam("email", true, true);
            String password = sm.getStringParam("password", false, true);
            String digits = sm.getStringParam("digits", false, true);
            Long registration_date = sm.getLongParam("registration_date", true, true);
            Boolean send_news = sm.getBooleanParam("newsletter", true, true);
            JSONObject errors = new JSONObject();
            if (username == null || username.length() < 2 || username.length() > 30)
                errors.put("username", "Invalid username");
            if (email == null || !Regex.isEmail(email) || !email.endsWith("@ieseg.fr"))
                errors.put("email", "Invalid email");
            if (password == null || !Regex.isPassword(password))
                errors.put("password", "Invalid password");
            if (registration_date == null)
                errors.put("registration_date", "Invalid registration date");
            if (digits == null || digits.length() != 6)
                throw new HttpServletException(HttpStatus.BadRequest, "Missing parameter digits or code");
            if (send_news == null)
                errors.put("newsletter", "Newsletter cannot be null");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT digits FROM userPendingRegistrations WHERE email = ?");
            hibernateQuery.setParameter(1, email);
            String db_digits = (String) hibernateQuery.getSingleResult();
            if (db_digits == null || db_digits.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "You didn't ask for an account.");
            if (!db_digits.equals(digits))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid digits.");
            if (!errors.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, errors);
            User newUser = UserFactory.getInstance().createUser(email, username, password);
            sm.saveOrUpdate(newUser);
            if (send_news) {
                MailJetBuilder mailJetBuilder = new MailJetBuilder(ContactslistManageContact.resource, 13300);
                mailJetBuilder.property(ContactslistManageContact.EMAIL, newUser.getEmail());
                mailJetBuilder.property(ContactslistManageContact.NAME, newUser.getUsername());
                mailJetBuilder.property(ContactslistManageContact.ACTION, "addnoforce");
                mailJetBuilder.post();
            }
            sm.setUser(newUser);
            /* ieseg profile */
            Profile ieseg_profile = new Profile(newUser, 1, 0, new ProfileInformation("IESEG"));

            /* ieseg apps in profile */
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website ieseg_online = catalog.getWebsiteWithName("IESEG Online", hibernateQuery);
            ClassicApp ieseg_onlineApp = new ClassicApp(new AppInformation(ieseg_online.getName()), ieseg_online);
            ieseg_onlineApp.setProfile(ieseg_profile);
            ieseg_onlineApp.setPosition(0);
            sm.saveOrUpdate(ieseg_onlineApp);
            ieseg_profile.addApp(ieseg_onlineApp);
            Website jobTeaser = catalog.getWebsiteWithName("JobTeaser Ieseg", hibernateQuery);
            ClassicApp jobTeaserApp = new ClassicApp(new AppInformation("JobTeaser"), jobTeaser);
            jobTeaserApp.setProfile(ieseg_profile);
            jobTeaserApp.setPosition(1);
            sm.saveOrUpdate(jobTeaserApp);
            ieseg_profile.addApp(jobTeaserApp);
            Website unify = catalog.getWebsiteWithName("Unify Iéseg", hibernateQuery);
            ClassicApp unifyApp = new ClassicApp(new AppInformation(unify.getName()), unify);
            unifyApp.setProfile(ieseg_profile);
            unifyApp.setPosition(2);
            sm.saveOrUpdate(unifyApp);
            ieseg_profile.addApp(unifyApp);
            Website office_mail = catalog.getWebsiteWithName("Office365 Mails", hibernateQuery);
            ClassicApp office_mailApp = new ClassicApp(new AppInformation(office_mail.getName()), office_mail);
            office_mailApp.setProfile(ieseg_profile);
            office_mailApp.setPosition(3);
            sm.saveOrUpdate(office_mailApp);
            ieseg_profile.addApp(office_mailApp);
            sm.setSuccess(newUser.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
