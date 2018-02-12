package com.Ease.API.V1.Common;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Mail.MailjetContactWrapper;
import com.Ease.Team.TeamUser;
import com.Ease.User.JsonWebTokenFactory;
import com.Ease.User.User;
import com.Ease.User.UserEmail;
import com.Ease.User.UserFactory;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.Hashing;
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

@WebServlet("/api/v1/common/TeamRegistration")
public class ServletTeamRegistration extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            if (user != null)
                sm.setUser(null);
            String access_code = sm.getStringParam("access_code", true, false);
            String email = sm.getStringParam("email", true, false);
            String password = sm.getStringParam("password", false, false);
            String code = sm.getStringParam("code", false, false);
            String phone_number = sm.getStringParam("phone_number", true, false);
            Boolean send_news = sm.getBooleanParam("newsletter", true, true);
            String first_name = sm.getStringParam("first_name", true, true);
            String last_name = sm.getStringParam("last_name", true, true);
            if (first_name == null)
                first_name = "";
            if (last_name == null)
                last_name = "";
            if (send_news == null)
                send_news = false;
            if (email == null || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid email");
            if (password == null || !Regex.isPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Password must be at least 8 characters, contains 1 uppercase, 1 lowercase and 1 digit.");
            if (phone_number.isEmpty() || phone_number.length() > 255 || !Regex.isPhoneNumber(phone_number))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid phone number");
            if (!first_name.isEmpty() && !Regex.isValidName(first_name))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid first name");
            if (!last_name.isEmpty() && !Regex.isValidName(last_name))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid last name");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            User newUser;
            hibernateQuery.queryString("SELECT t FROM TeamUser t WHERE t.email = :email AND t.invitation_code = :code");
            hibernateQuery.setParameter("email", email);
            hibernateQuery.setParameter("code", code);
            TeamUser teamUser = (TeamUser) hibernateQuery.getSingleResult();
            if (teamUser == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No invitation for this email.");
            sm.initializeTeamWithContext(teamUser.getTeam());
            if (teamUser.getArrival_date() != null && teamUser.getArrival_date().getTime() > new Date().getTime())
                throw new HttpServletException(HttpStatus.BadRequest, "This is not the moment of your registration");
            newUser = teamUser.getUser();
            if (newUser != null) {
                if (!newUser.getUserKeys().isGoodAccessCode(access_code))
                    throw new HttpServletException(HttpStatus.BadRequest, "This link is no longer valid");
                newUser.getUserKeys().setHashed_password(Hashing.hash(password));
                newUser.getUserKeys().setKeyUser(AES.encryptUserKey(newUser.getUserKeys().getDecipheredKeyUser(access_code), password, newUser.getUserKeys().getSaltPerso()));
                newUser.getUserKeys().setAccess_code_hash(null);
                newUser.getUserStatus().setRegistered(true);
                newUser.getUserStatus().setOnboarding_step(1);
                newUser.getPersonalInformation().setFirst_name(first_name);
                newUser.getPersonalInformation().setLast_name(last_name);
                newUser.getPersonalInformation().setPhone_number(phone_number);
                sm.saveOrUpdate(newUser);
                UserEmail userEmail = new UserEmail(email, true, newUser);
                sm.saveOrUpdate(userEmail);
                newUser.addUserEmail(userEmail);
                teamUser.setState(1);
            } else {
                String username = sm.getStringParam("username", true, false);
                username = username.toLowerCase();
                checkUsernameIntegrity(username);
                newUser = UserFactory.getInstance().createUser(email, username, password, first_name, last_name, phone_number);
                newUser.getUserStatus().setRegistered(true);
                sm.saveOrUpdate(newUser);
                UserEmail userEmail = new UserEmail(email, true, newUser);
                sm.saveOrUpdate(userEmail);
                newUser.addUserEmail(userEmail);
            }
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