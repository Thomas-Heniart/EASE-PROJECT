package com.Ease.API.V1.Common;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamUser;
import com.Ease.User.JsonWebTokenFactory;
import com.Ease.User.User;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@WebServlet("/api/v1/common/Connection")
public class ServletConnection extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int max_attempts = 10;
    private static final long expiration_time = 5; // 5 minutes
    private static final long ONE_MINUTE_IN_MILLIS = 60000;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getStringParam("email", true, true);
            String password = sm.getStringParam("password", false, true);
            String client_ip = IpUtils.getIpAddr(request);
            DataBaseConnection db = sm.getDB();
            addIpInDataBase(client_ip, db);
            if (!canConnect(client_ip, db))
                throw new HttpServletException(HttpStatus.Forbidden, "Too much attempts to connect. Please retry in 5 minutes.");
            if (email == null || !Regex.isEmail(email) || password == null || password.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong email or password.");
            String key = (String) sm.getContextAttr("privateKey");
            password = RSA.Decrypt(password, key);
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
                if (teamUser.isVerified()) {
                    String teamKey = teamUser.getDecipheredTeamKey(keyUser);
                    sm.getTeamProperties(teamUser.getTeam().getDb_id()).put("teamKey", teamKey);
                }
            }
            Key secret = (Key) sm.getContextAttr("secret");
            if (user.getJsonWebToken() == null) {
                user.setJsonWebToken(JsonWebTokenFactory.getInstance().createJsonWebToken(user.getDb_id(), keyUser, secret));
                sm.saveOrUpdate(user.getJsonWebToken());
            } else {
                if (user.getJsonWebToken().getExpiration_date() < new Date().getTime()) {
                    user.getJsonWebToken().renew(keyUser, user.getDb_id(), secret);
                    sm.saveOrUpdate(user.getJsonWebToken());
                }
            }
            removeIpFromDataBase(client_ip, db);
            String jwt = user.getJsonWebToken().getJwt(keyUser);
            Cookie cookie = new Cookie("JWT", jwt);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 4);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            cookie.setPath("/");
            cookie.setMaxAge(Math.toIntExact(calendar.getTimeInMillis() - new Date().getTime()) / 1000);
            response.addCookie(cookie);
            user.getCookies().forEach(response::addCookie);
            sm.setUser(user);
            JSONObject res = user.getJson();
            res.put("JWT", jwt);
            sm.setSuccess(res);
        } catch (HttpServletException e) {
            sm.setError(new HttpServletException(HttpStatus.BadRequest, "Wrong email or password."));
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    public void addIpInDataBase(String client_ip, DataBaseConnection db) throws HttpServletException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM askingIps WHERE ip= ?;");
        request.setString(client_ip);
        DatabaseResult rs = request.get();
        if (rs.next())
            return;
        int transaction = db.startTransaction();
        request = db.prepareRequest("INSERT INTO askingIps values (NULL, ?, 0, ?, ?);");
        request.setString(client_ip);
        request.setString(getCurrentTime());
        request.setString(getExpirationTime());
        request.set();
        db.commitTransaction(transaction);
    }

    public String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getExpirationTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(new Date(date.getTime() + (expiration_time * ONE_MINUTE_IN_MILLIS)));
    }

    public void removeIpFromDataBase(String client_ip, DataBaseConnection db) throws HttpServletException {
        DatabaseRequest request = db.prepareRequest("DELETE FROM askingIps WHERE ip = ?;");
        request.setString(client_ip);
        request.set();
    }

    public boolean canConnect(String client_ip, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("SELECT attempts, expirationDate FROM askingIps WHERE ip= ?;");
            request.setString(client_ip);
            DatabaseResult rs = request.get();
            int attempts = 0;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date expirationDate = new Date();

            if (rs.next()) {
                attempts = Integer.parseInt(rs.getString(1));
                expirationDate = dateFormat.parse(rs.getString(2));
            }
            return attempts < max_attempts || expirationDate.compareTo(new Date()) <= 0;
        } catch (Exception e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }
}
