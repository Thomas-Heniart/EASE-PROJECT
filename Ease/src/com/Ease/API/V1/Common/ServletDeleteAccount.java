package com.Ease.API.V1.Common;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.User.User;
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

@WebServlet("/api/v1/common/DeleteAccount")
public class ServletDeleteAccount extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String password = sm.getStringParam("password", false, false);
            if (password == null || password.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Password does not not match.");
            password = sm.decipher(password);
            User user = sm.getUser();
            if (!user.getUserKeys().isGoodPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Password does not match.");
            if (!user.getTeamUsers().isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "It is not possible to delete your account as long as you are part of a team. Please delete your team (or ask your admin to be deleted of your team) before deleting your personal Ease.space account.");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("DELETE FROM passwordLost WHERE user_id = :id");
            hibernateQuery.setParameter("id", user.getDb_id());
            hibernateQuery.executeUpdate();
            /* Delete app first then delete user */
            sm.deleteObject(user);
            sm.getSession().invalidate();
            sm.setSuccess("Account deleted");
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
