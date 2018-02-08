package com.Ease.API.V1.Common;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Mail.MailjetContactWrapper;
import com.Ease.User.JsonWebTokenFactory;
import com.Ease.User.User;
import com.Ease.User.UserEmail;
import com.Ease.User.UserFactory;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import com.mailjet.client.resource.ContactslistManageContact;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
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
            username = username.toLowerCase();
            String email = sm.getStringParam("email", true, false);
            String password = sm.getStringParam("password", false, false);
            String digits = sm.getStringParam("digits", false, false);
            String phone_number = sm.getStringParam("phone_number", true, false);
            String first_name = sm.getStringParam("first_name", true, true);
            String last_name = sm.getStringParam("last_name", true, true);
            if (first_name == null)
                first_name = "";
            if (last_name == null)
                last_name = "";
            checkUsernameIntegrity(username);
            if (email == null || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid email");
            if (password == null || !Regex.isPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Password must be at least 8 characters, contains 1 uppercase, 1 lowercase and 1 digit.");
            if (digits.length() != 6)
                throw new HttpServletException(HttpStatus.BadRequest, "Missing parameter digits");
            if (phone_number.isEmpty() || phone_number.length() > 255 || !Regex.isPhoneNumber(phone_number))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid phone number");
            if (!first_name.isEmpty() && !Regex.isValidName(first_name))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid first name");
            if (!last_name.isEmpty() && !Regex.isValidName(last_name))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid last name");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT digits, newsletter FROM userPendingRegistrations WHERE email = :email");
            hibernateQuery.setParameter("email", email);
            Object[] objects = (Object[]) hibernateQuery.getSingleResult();
            if (objects == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You didn't ask for an account.");
            String db_digits = (String) objects[0];
            if (db_digits == null || db_digits.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "You didn't ask for an account.");
            if (!db_digits.equals(digits))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid digits.");
            boolean send_news = (Boolean) objects[1];
            User newUser = UserFactory.getInstance().createUser(email, username, password, "", "", phone_number);
            newUser.getUserStatus().setOnboarding_step(1);
            newUser.getPersonalInformation().setFirst_name(first_name);
            newUser.getPersonalInformation().setLast_name(last_name);
            sm.saveOrUpdate(newUser);
            UserEmail userEmail = new UserEmail(email, true, newUser);
            sm.saveOrUpdate(userEmail);
            newUser.addUserEmail(userEmail);
            newUser.getUserStatus().setNew_feature_seen(true);
            sm.setUser(newUser);
            String keyUser = newUser.getUserKeys().getDecipheredKeyUser(password);
            String privateKey = newUser.getUserKeys().getDecipheredPrivateKey(keyUser);
            Map<String, Object> userProperties = sm.getUserProperties(newUser.getDb_id());
            userProperties.put("keyUser", keyUser);
            userProperties.put("privateKey", privateKey);
            Key secret = (Key) sm.getContextAttr("secret");
            newUser.setJsonWebToken(JsonWebTokenFactory.getInstance().createJsonWebToken(newUser.getDb_id(), keyUser, secret));
            sm.saveOrUpdate(newUser.getJsonWebToken());
            String jwt = newUser.getJsonWebToken().getJwt(keyUser);
            Cookie cookie = new Cookie("JWT", jwt);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 4);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(Math.toIntExact(calendar.getTimeInMillis() - new Date().getTime()) / 1000);
            response.addCookie(cookie);
            newUser.getCookies().forEach(response::addCookie);
            if (send_news) {
                MailJetBuilder mailJetBuilder = new MailJetBuilder(ContactslistManageContact.resource, 13300);
                mailJetBuilder.property(ContactslistManageContact.EMAIL, newUser.getEmail());
                mailJetBuilder.property(ContactslistManageContact.NAME, newUser.getUsername());
                mailJetBuilder.property(ContactslistManageContact.ACTION, "addnoforce");
                mailJetBuilder.post();
            }
            MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
            mailjetContactWrapper.updateUserData(newUser);
            mailjetContactWrapper.updateUserContactLists(newUser);
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setTemplateId(287036);
            mailJetBuilder.addVariable("url", Variables.URL_PATH);
            mailJetBuilder.addTo(newUser.getEmail());
            mailJetBuilder.setFrom("contact@ease.space", "Ease.Space");
            mailJetBuilder.sendEmail();
            JSONObject res = newUser.getJson();
            res.put("JWT", jwt);
            sm.setSuccess(res);
        } catch (Exception e) {
            e.printStackTrace();
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