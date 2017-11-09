package com.Ease.API.V1.Common;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
            User user = sm.getUser();
            if (user != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You are already logged in");
            DataBaseConnection db = sm.getDB();
            addIpInDataBase(client_ip, db);
            if (canConnect(client_ip, db)) {
                if (email == null || !Regex.isEmail(email) || password == null || password.isEmpty())
                    throw new HttpServletException(HttpStatus.BadRequest, "Wrong email or password.");
                else {
                    String key = (String) sm.getContextAttr("privateKey");
                    password = RSA.Decrypt(password, key);
                    DatabaseRequest databaseRequest = db.prepareRequest("SELECT * FROM users WHERE email = ?");
                    databaseRequest.setString(email);
                    if (!databaseRequest.get().next())
                        throw new HttpServletException(HttpStatus.BadRequest, "Wrong email or password.");
                    user = User.loadUser(email, password, sm.getServletContext(), db);
                    sm.setUser(user);
                    HibernateQuery hibernateQuery = sm.getHibernateQuery();
                    for (TeamUser teamUser : user.getTeamUsers())
                        teamUser.cipheringStep(hibernateQuery, db);
                    ((Map<String, User>) sm.getContextAttr("users")).put(email, user);
                    ((Map<String, User>) sm.getContextAttr("sessionIdUserMap")).put(sm.getSession().getId(), user);
                    ((Map<String, User>) sm.getContextAttr("sIdUserMap")).put(user.getSessionSave().getSessionId(), user);
                    sm.setUser(user);
                    user.initializeDashboardManager(hibernateQuery);
                    hibernateQuery.commit();
                    user.decipherDashboard();
                    removeIpFromDataBase(client_ip, db);
                    JSONObject res = new JSONObject();
                    res.put("user", user.getJson());
                    sm.setSuccess(res);
                }
            } else {
                throw new HttpServletException(HttpStatus.Forbidden, "Too much attempts to connect. Please retry in 5 minutes.");
            }
        } catch (GeneralException e) {
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

    public void addIpInDataBase(String client_ip, DataBaseConnection db) throws GeneralException {
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

    public void removeIpFromDataBase(String client_ip, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("DELETE FROM askingIps WHERE ip = ?;");
        request.setString(client_ip);
        request.set();
    }

    public boolean canConnect(String client_ip, DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT attempts, expirationDate FROM askingIps WHERE ip= ?;");
        request.setString(client_ip);
        DatabaseResult rs = request.get();
        int attempts = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expirationDate = new Date();
        try {
            if (rs.next()) {
                attempts = Integer.parseInt(rs.getString(1));
                expirationDate = dateFormat.parse(rs.getString(2));
            }
        } catch (Exception e) {
            throw new GeneralException(ServletManager.Code.InternError, e);
        }
        return attempts < max_attempts || expirationDate.compareTo(new Date()) <= 0;
    }
}
