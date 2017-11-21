package com.Ease.API.V1.Common;

import com.Ease.User.User;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/Logout")
public class ServletLogout extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Cookie cookies[] = sm.getRequest().getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ((cookie.getName()).compareTo("sId") == 0) {
                        cookie.setValue("");
                        cookie.setMaxAge(0);
                        sm.getResponse().addCookie(cookie);
                    } else if ((cookie.getName()).compareTo("sTk") == 0) {
                        cookie.setValue("");
                        cookie.setMaxAge(0);
                        sm.getResponse().addCookie(cookie);
                    }
                }
            }
            sm.getSession().invalidate();
            sm.setSuccess("Logout successfully");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
