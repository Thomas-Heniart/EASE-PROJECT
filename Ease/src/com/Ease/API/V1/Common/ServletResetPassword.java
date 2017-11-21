package com.Ease.API.V1.Common;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.NewDashboard.LogWithApp;
import com.Ease.NewDashboard.Profile;
import com.Ease.User.User;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/ResetPassword")
public class ServletResetPassword extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getStringParam("email", true, false);
            String code = sm.getStringParam("code", true, false);
            String password = sm.getStringParam("password", false, false);
            if (!Regex.isPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid password");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM User u WHERE u.email = :email");
            hibernateQuery.setParameter("email", email);
            User user = (User) hibernateQuery.getSingleResult();
            user.getProfileSet().stream().flatMap(Profile::getApps).forEach(app -> {
                if (app.getTeamCardReceiver() != null) {

                } else if (app.isClassicApp()) {
                    ((ClassicApp) app).setAccount(null);
                    sm.saveOrUpdate(app);
                } else if (app.isLogWithApp()) {
                    ((LogWithApp) app).setLoginWith_app(null);
                    sm.saveOrUpdate(app);
                }
            });
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getParam("email", true);
            String code = sm.getParam("code", true);
            if (email == null || email.equals("")) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong email or password.");
            } else if (code == null || code.equals("")) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong informations.");
            }
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT id FROM users WHERE email = :email");
            hibernateQuery.setParameter("email", email);
            Integer userId = (Integer) hibernateQuery.getSingleResult();
            if (userId == null)
                throw new HttpServletException(HttpStatus.Forbidden);
            DataBaseConnection db = sm.getDB();
            DatabaseRequest databaseRequest = db.prepareRequest("SELECT * FROM passwordLost WHERE (NOW() <= DATE_ADD(dateOfRequest, INTERVAL 2 HOUR)) AND user_id = ? AND linkCode = ?;");
            databaseRequest.setInt(userId);
            databaseRequest.setString(code);
            DatabaseResult rs = databaseRequest.get();
            if (rs.next())
                sm.setRedirectUrl("newPassword.jsp?email=" + email + "&linkCode=" + code + "");
            else
                sm.setRedirectUrl("passwordLost?codeExpiration=true");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();

    }
}
