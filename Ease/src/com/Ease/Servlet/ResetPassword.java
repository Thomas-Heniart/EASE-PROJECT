package com.Ease.Servlet;

import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.User.Keys;
import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet implementation class ResetPassword
 */
@WebServlet("/resetPassword")
public class ResetPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) (session.getAttribute("user"));
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);

        String email = sm.getServletParam("email", true);
        String code = sm.getServletParam("code", true);
        try {
            if (user != null) {
                Logout.logoutUser(user, sm);
            }
            if (email == null || email.equals("")) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong email or password.");
            } else if (code == null || code.equals("")) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong informations.");
            }
            String userId = User.findDBid(email, sm);
            if (Keys.checkCodeValidity(userId, code, sm))
                sm.setRedirectUrl("newPassword.jsp?email=" + email + "&linkCode=" + code + "");
            else
                sm.setRedirectUrl("passwordLost?codeExpiration=true");
        } catch (GeneralException e) {
            sm.setResponse(e);
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) (session.getAttribute("user"));
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);

        String email = sm.getServletParam("email", true);
        String code = sm.getServletParam("linkCode", true);
        String password = sm.getServletParam("password", false);
        String confirmPassword = sm.getServletParam("confirmPassword", false);
        HibernateQuery hibernateQuery = new HibernateQuery();
        try {
            if (user != null) {
                Logout.logoutUser(user, sm); //throw new GeneralException(ServletManager.Code.ClientWarning, "You are logged on Ease.");
            }
            if (email == null || email.equals("")) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong email or password.");
            } else if (code == null || code.equals("")) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong informations.");
            } else if (password == null || !Regex.isPassword(password)) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong email or password.");
            } else if (confirmPassword == null || !confirmPassword.equals(password)) {
                throw new GeneralException(ServletManager.Code.ClientWarning, "Passwords doesn't match.");
            }
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            String userId = User.findDBid(email, sm);
            Keys.resetPassword(userId, password, sm);
            hibernateQuery.querySQLString("SELECT id, team_id FROM teamUsers WHERE user_id = ?");
            hibernateQuery.setParameter(1, userId);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            for (Object teamUserIdAndTeamIdObj : hibernateQuery.list()) {
                Object[] teamUserIdAndTeamId = (Object[]) teamUserIdAndTeamIdObj;
                Team team = teamManager.getTeamWithId((Integer) teamUserIdAndTeamId[1]);
                TeamUser teamUser = team.getTeamUserWithId((Integer) teamUserIdAndTeamId[0]);
                teamUser.setDisabled(true);
                teamUser.setDisabledDate(new Date());
                teamUser.setTeamKey(null);
                hibernateQuery.saveOrUpdateObject(teamUser);
                for (SharedApp sharedApp : teamUser.getSharedApps())
                    sharedApp.setDisableShared(true, sm.getDB());
                if (teamUser.getAdmin_id() != null)
                    team.getTeamUserWithId(teamUser.getAdmin_id()).addNotification(teamUser.getUsername() + " lost the password to access your team " + team.getName() + " on Ease.space. Please give again the access to this person.", "@" + teamUser.getDb_id().toString(), "/resources/notifications/flag.png", new Date(), sm.getDB());
                else {
                    /* @TODO */
                }
            }
            hibernateQuery.commit();
            DatabaseRequest databaseRequest = db.prepareRequest("DELETE FROM passwordLost WHERE user_id = ?;");
            databaseRequest.setInt(userId);
            databaseRequest.set();
            db.commitTransaction(transaction);
            sm.setResponse(ServletManager.Code.Success, "Account trunced and password set.");
        } catch (GeneralException e) {
            hibernateQuery.rollback();
            sm.setResponse(e);
        } catch (Exception e) {
            hibernateQuery.rollback();
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

}
