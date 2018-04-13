package com.Ease.API.V1.User;

import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
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

@WebServlet("/api/v1/users/EditConnectionLifetime")
public class ServletEditConnectionLifetime extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer connectionLifetime = sm.getIntParam("connection_lifetime", true, false);
            User user = sm.getUser();
            if (connectionLifetime < 1)
                throw new HttpServletException(HttpStatus.BadRequest, "Connection lifetime cannot be under one day");
            user.getOptions().setConnection_lifetime(connectionLifetime);
            sm.saveOrUpdate(user.getOptions());
            Key secret = (Key) sm.getContextAttr("secret");
            if (user.getJsonWebToken() != null)
                user.getJsonWebToken().renew(sm.getKeyUser(), user, secret, user.getOptions().getConnection_lifetime());
            sm.saveOrUpdate(user.getJsonWebToken());
            String jwt = user.getJsonWebToken().getJwt(sm.getKeyUser());
            Cookie cookie = new Cookie("JWT", jwt);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, user.getOptions().getConnection_lifetime());
            calendar.set(Calendar.HOUR_OF_DAY, 4);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            cookie.setPath("/");
            cookie.setMaxAge(Math.toIntExact(calendar.getTimeInMillis() - new Date().getTime()) / 1000);
            response.addCookie(cookie);
            JSONObject res = user.getJson();
            res.put("JWT", jwt);
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        } finally {
            sm.sendResponse();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
