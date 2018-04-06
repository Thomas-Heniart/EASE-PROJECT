package com.Ease.API.V1.Common;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamUser;
import com.Ease.User.JsonWebTokenFactory;
import com.Ease.User.User;
import com.Ease.User.UserEmail;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
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

@WebServlet("/api/v1/common/TeamOwnerRegistration")
public class ServletTeamOwnerRegistration extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            if (user != null)
                sm.setUser(null);
            String accessCode = sm.getStringParam("access_code", true, false);
            String email = sm.getStringParam("email", true, false);
            email = email.toLowerCase();
            String password = sm.getStringParam("password", false, false);
            String code = sm.getStringParam("code", false, false);
            if (!Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid email");
            if (password == null || !Regex.isPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Password must be at least 8 characters, contains 1 uppercase, 1 lowercase and 1 digit.");
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
                if (!newUser.getUserKeys().isGoodAccessCode(accessCode))
                    throw new HttpServletException(HttpStatus.BadRequest, "This link is no longer valid");
                newUser.finalizeRegistration(password, accessCode);
                teamUser.setState(2);
            } else
                throw new HttpServletException(HttpStatus.BadRequest, "You shouldn't be there.");
            sm.saveOrUpdate(newUser);
            UserEmail userEmail = new UserEmail(email, true, newUser);
            sm.saveOrUpdate(userEmail);
            newUser.addUserEmail(userEmail);
            sm.setUser(newUser);
            String keyUser = newUser.getUserKeys().getDecipheredKeyUser(password);
            String privateKey = newUser.getUserKeys().getDecipheredPrivateKey(keyUser);
            Map<String, Object> userProperties = sm.getUserProperties(newUser.getDb_id());
            userProperties.put("keyUser", keyUser);
            userProperties.put("privateKey", privateKey);
            Key secret = (Key) sm.getContextAttr("secret");
            newUser.setJsonWebToken(JsonWebTokenFactory.getInstance().createJsonWebToken(newUser.getDb_id(), newUser.getOptions().getConnection_lifetime(), keyUser, secret));
            sm.saveOrUpdate(newUser.getJsonWebToken());
            String jwt = newUser.getJsonWebToken().getJwt(keyUser);
            Cookie cookie = new Cookie("JWT", jwt);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, newUser.getOptions().getConnection_lifetime());
            calendar.set(Calendar.HOUR_OF_DAY, 3);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(Math.toIntExact(calendar.getTimeInMillis() - new Date().getTime()) / 1000);
            response.addCookie(cookie);
            newUser.getCookies().forEach(response::addCookie);
            JSONObject res = newUser.getJson();
            res.put("JWT", jwt);
            sm.setSuccess(res);
        } catch (Exception e) {
            e.printStackTrace();
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}