package com.Ease.API.V1.Schools;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.*;
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
import java.util.Map;

@WebServlet("/api/v1/common/RegistrationEdhec")
public class ServletRegistrationEdhec extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            if (user != null)
                user.logoutFromSession(sm.getSession().getId(), sm.getServletContext(), sm.getDB());
            String username = sm.getStringParam("username", true);
            String email = sm.getStringParam("email", true);
            String password = sm.getStringParam("password", false);
            String digits = sm.getStringParam("digits", false);
            Long registration_date = sm.getLongParam("registration_date", true);
            Boolean send_news = sm.getBooleanParam("newsletter", true);
            JSONObject errors = new JSONObject();
            if (username == null || username.length() < 2 || username.length() > 30)
                errors.put("username", "Invalid username");
            if (email == null || !Regex.isEmail(email) || !email.endsWith("@iscparis.com"))
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

            Profile edhecProfile = newUser.getDashboardManager().addProfile("ISC Paris", "#373B60", db);

            /* Edhec apps in profile */
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website aurion = catalog.getWebsiteWithName("Aurion");
            edhecProfile.addEmptyApp(aurion.getName(), aurion, db);
            Website blackboard = catalog.getWebsiteWithName("Blackboard");
            edhecProfile.addEmptyApp(blackboard.getName(), blackboard, db);
            Website jobTeaser = catalog.getWebsiteWithName("JobTeaser Edhec");
            edhecProfile.addEmptyApp("JobTeaser", jobTeaser, db);
            Website officeMail = catalog.getWebsiteWithName("Office365 Mails");
            edhecProfile.addEmptyApp(officeMail.getName(), officeMail, db);
            Website print = catalog.getWebsiteWithName("Everyon Print");
            edhecProfile.addEmptyApp("Everyone Print", print, db);
            Website workplace = catalog.getWebsiteWithName("Workplace");
            edhecProfile.addEmptyApp(workplace.getName(), workplace, db);

            db.commitTransaction(transaction);
            sm.setSuccess(newUser.getJson());

        } catch (GeneralException e) {
            sm.setError(new HttpServletException(HttpStatus.BadRequest, e.getMsg()));
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
