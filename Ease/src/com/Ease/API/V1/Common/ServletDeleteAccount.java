package com.Ease.API.V1.Common;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/common/DeleteAccount")
public class ServletDeleteAccount extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String password = sm.getStringParam("password", false);
            if (password == null || password.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Password does not not match.");
            String key = (String) sm.getContextAttr("privateKey");
            password = RSA.Decrypt(password, key);
            User user = sm.getUser();
            if (!user.getKeys().isGoodPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Password does not match.");
            if (!user.getTeamUsers().isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "It is not possible to delete your account as long as you are part of a team. Please delete your team (or ask your admin to be deleted of your team) before deleting your personal Ease.space account.");
            user.deleteFromDb(sm.getDB());
            user.logoutFromSession(sm.getSession().getId(), sm.getServletContext(), sm.getDB());
            Map<String, User> users = (Map<String, User>) sm.getContextAttr("users");
            users.remove(user.getEmail());
            sm.setSuccess("Account deleted");
            sm.getSession().invalidate();
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
