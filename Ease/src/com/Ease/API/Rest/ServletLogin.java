package com.Ease.API.Rest;

import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Map;

@WebServlet("/api/rest/Connection")
public class ServletLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getStringParam("email", true, false).toLowerCase();
            String password = sm.getStringParam("password", true, false);
            DataBaseConnection db = sm.getDB();
            if (!Regex.isEmail(email) || password.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong email or password.");
            /* String key = (String) sm.getContextAttr("privateKey");
            password = RSA.Decrypt(password, key); */
            DatabaseRequest databaseRequest = db.prepareRequest("SELECT * FROM users WHERE email = ?");
            databaseRequest.setString(email);
            if (!databaseRequest.get().next())
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong email or password.");
            User user = User.loadUser(email, password, sm.getServletContext(), db);
            user.renewJwt((Key) sm.getContextAttr("secret"), db);
            HibernateQuery hibernateQuery = new HibernateQuery();
            for (TeamUser teamUser : user.getTeamUsers()) {
                if (!teamUser.isVerified() && teamUser.getTeamKey() != null) {
                    teamUser.finalizeRegistration();
                    hibernateQuery.saveOrUpdateObject(teamUser);
                }
                if (teamUser.isVerified() && teamUser.getTeamKey() != null && teamUser.isDisabled()) {
                    String deciphered_teamKey = RSA.Decrypt(teamUser.getTeamKey(), user.getKeys().getPrivateKey());
                    teamUser.setTeamKey(user.encrypt(deciphered_teamKey));
                    teamUser.setDeciphered_teamKey(deciphered_teamKey);
                    teamUser.setDisabled(false);
                    hibernateQuery.saveOrUpdateObject(teamUser);
                    for (SharedApp sharedApp : teamUser.getSharedApps()) {
                        sharedApp.setDisableShared(false, sm.getDB());
                    }
                }
            }
            hibernateQuery.commit();
            ((Map<String, User>) sm.getContextAttr("users")).put(email, user);
            ((Map<String, User>) sm.getContextAttr("sessionIdUserMap")).put(sm.getSession().getId(), user);
            ((Map<String, User>) sm.getContextAttr("sIdUserMap")).put(user.getSessionSave().getSessionId(), user);
            user.getDashboardManager().decipherApps(sm);
            JSONObject res = new JSONObject();

            res.put("JWT", user.getJwt().getJwt((Key) sm.getContextAttr("secret")));
            ((Map<String, User>) sm.getContextAttr("tokenUserMap")).put(user.getJwt().getConnection_token(), user);
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
