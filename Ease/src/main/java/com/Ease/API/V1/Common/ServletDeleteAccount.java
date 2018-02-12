package com.Ease.API.V1.Common;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailjetContactWrapper;
import com.Ease.NewDashboard.*;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
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
            for (TeamUser teamUser : user.getTeamUsers()) {
                Team team = teamUser.getTeam();
                if (team.isActive())
                    throw new HttpServletException(HttpStatus.BadRequest, "It is not possible to delete your account as long as you are part of a team. Please delete your team (or ask your admin to be deleted of your team) before deleting your personal Ease.space account.");
            }
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("DELETE FROM passwordLost WHERE user_id = :id");
            hibernateQuery.setParameter("id", user.getDb_id());
            hibernateQuery.executeUpdate();
            hibernateQuery.queryString("DELETE FROM Update u WHERE u.user.db_id = :user_id");
            hibernateQuery.setParameter("user_id", user.getDb_id());
            hibernateQuery.executeUpdate();
            user.getApps().forEach(app -> {
                if (app.isLogWithApp()) {
                    ((LogWithApp) app).setLoginWith_app(null);
                    sm.saveOrUpdate(app);
                } else if (app.isSsoApp()) {
                    SsoApp ssoApp = (SsoApp) app;
                    SsoGroup ssoGroup = ssoApp.getSsoGroup();
                    ssoGroup.removeSsoApp(ssoApp);
                }
            });
            System.out.println("Apps size: " + user.getApps().size());
            user.getImportedAccountMap().forEach((aLong, importedAccount) -> sm.deleteObject(importedAccount));
            user.getImportedAccountMap().clear();
            user.getApps().forEach(sm::deleteObject);
            user.getApps().clear();
            user.getProfileSet().forEach(sm::deleteObject);
            user.getProfileSet().clear();
            user.getSsoGroupSet().clear();
            for (TeamUser teamUser : user.getTeamUsers()) {
                teamUser.setUser(null);
                teamUser.setProfile(null);
                teamUser.setTeamKey(null);
                teamUser.setState(0);
                sm.saveOrUpdate(teamUser);
            }
            user.getTeamUsers().clear();
            MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
            mailjetContactWrapper.deleteUserEmail(user.getEmail());
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
