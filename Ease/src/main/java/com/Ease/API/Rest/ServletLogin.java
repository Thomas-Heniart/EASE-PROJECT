package com.Ease.API.Rest;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamUser;
import com.Ease.User.JsonWebTokenFactory;
import com.Ease.User.User;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
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

@WebServlet("/api/rest/Connection")
public class ServletLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getStringParam("email", true, false).toLowerCase();
            String password = sm.getStringParam("password", true, false);
            if (!Regex.isEmail(email) || password.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong email or password.");
                /* String key = (String) sm.getContextAttr("privateKey");
                password = RSA.Decrypt(password, key); */
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM User u WHERE u.email = :email");
            hibernateQuery.setParameter("email", email);
            User user = (User) hibernateQuery.getSingleResult();
            if (user == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong email or password.");
            String keyUser = user.getUserKeys().getDecipheredKeyUser(password);
            sm.getUserProperties(user.getDb_id()).put("keyUser", keyUser);
            for (TeamUser teamUser : user.getTeamUsers()) {
                sm.initializeTeamWithContext(teamUser.getTeam());
                if (teamUser.isVerified() && teamUser.isDisabled() && teamUser.getTeamKey() != null) {
                    teamUser.setTeamKey(AES.encrypt(RSA.Decrypt(teamUser.getTeamKey(), user.getUserKeys().getDecipheredPrivateKey(keyUser)), keyUser));
                    teamUser.setDisabled(false);
                    sm.saveOrUpdate(teamUser);
                }
                if (teamUser.isVerified() && !teamUser.isDisabled()) {
                    String teamKey = teamUser.getDecipheredTeamKey(keyUser);
                    sm.getTeamProperties(teamUser.getTeam().getDb_id()).put("teamKey", teamKey);
                }
            }
            Key secret = (Key) sm.getContextAttr("secret");
            if (user.getJsonWebToken() == null) {
                user.setJsonWebToken(JsonWebTokenFactory.getInstance().createJsonWebToken(user, keyUser, secret));
                sm.saveOrUpdate(user.getJsonWebToken());
            } else {
                if (user.getJsonWebToken().getExpiration_date() < new Date().getTime()) {
                    user.getJsonWebToken().renew(keyUser, user, secret, user.getOptions().getConnection_lifetime());
                    sm.saveOrUpdate(user.getJsonWebToken());
                }
            }
            String jwt = user.getJsonWebToken().getJwt(keyUser);
            Cookie cookie = new Cookie("JWT", jwt);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, user.getOptions().getConnection_lifetime());
            calendar.set(Calendar.HOUR_OF_DAY, 4);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            cookie.setMaxAge(Math.toIntExact(calendar.getTimeInMillis() - new Date().getTime()) / 1000);
            user.getCookies().forEach(response::addCookie);
            response.addCookie(cookie);
            sm.setUser(user);
            JSONObject res = user.getJson();
            res.put("JWT", jwt);
            sm.setSuccess(res);
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
