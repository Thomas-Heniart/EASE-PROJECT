package com.Ease.Utils.Servlets;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationManager;
import com.Ease.User.User;
import com.Ease.User.UserFactory;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.*;
import com.Ease.websocketV1.WebSocketManager;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ServletManager {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String redirectUrl;
    protected String servletName;
    protected Map<String, String> args = new HashMap<>();
    protected DataBaseConnection db;
    private DataBaseConnection logDb;
    private User user;
    protected boolean saveLogs;
    protected String logResponse;
    protected String socketId;
    protected JSONObject jsonObjectResponse;
    protected JSONArray jsonArrayResponse;
    protected String errorMessage;
    protected Date timestamp;

    protected HibernateQuery hibernateQuery;

    public static boolean debug = true;


    public ServletManager(String servletName, HttpServletRequest request, HttpServletResponse response, boolean saveLogs) throws IOException {
        this.servletName = servletName;
        this.request = request;
        this.response = response;
        this.saveLogs = saveLogs;
    }

    protected void setSuccess() {
        response.setStatus(HttpStatus.Success.getValue());
        response.setContentType("application/json");
    }

    protected void setInternError() {
        response.setStatus(HttpStatus.InternError.getValue());
        this.errorMessage = "Darn - that didn’t work. Feel free to contact Thomas at thomas@ease.space";
    }

    protected void setForbidden() {
        response.setStatus(HttpStatus.Forbidden.getValue());
    }

    protected void setAccessDenied() {
        response.setStatus(HttpStatus.AccessDenied.getValue());
    }

    protected void setBadRequest() {
        response.setStatus(HttpStatus.BadRequest.getValue());
    }

    public void setSuccess(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", msg);
        this.setSuccess(jsonObject);
    }

    public void setSuccess(JSONObject res) {
        this.setSuccess();
        this.jsonObjectResponse = res;
    }

    public void setSuccess(JSONArray res) {
        this.setSuccess();
        this.jsonArrayResponse = res;
    }

    public void setError(Exception e) {
        try {
            HttpServletException httpServletException = (HttpServletException) e;
            System.out.println("Error code: " + httpServletException.getHttpStatus().getValue());
            if (httpServletException.getMsg() == null && httpServletException.getJsonObject() != null) {
                response.setContentType("application/json");
                this.errorMessage = httpServletException.getJsonObject().toString();
            } else
                this.errorMessage = httpServletException.getMsg();
            System.out.println(this.errorMessage);
            this.logResponse = this.errorMessage;
            if (httpServletException.getHttpStatus().getValue() == HttpStatus.InternError.getValue()) {
                e.printStackTrace();
                this.setInternError();
            }
            response.setStatus(httpServletException.getHttpStatus().getValue());
        } catch (ClassCastException e1) {
            e.printStackTrace();
            this.logResponse = e.toString() + ".\nStackTrace:";
            for (int i = 0; i < e.getStackTrace().length; i++)
                this.logResponse += ("\n" + e.getStackTrace()[i]);
            this.setInternError();
        }
    }

    public void setError(StripeException e) {
        System.out.println("Error code: " + e.getStatusCode() + " and msg: " + e.getMessage());
        response.setStatus(e.getStatusCode());
        this.errorMessage = e.getMessage();
    }

    public void needToBeConnected() throws HttpServletException {
        Integer user_id = (Integer) this.getSession().getAttribute("user_id");
        String jwt = this.request.getHeader("Authorization");
        Cookie cookies[] = this.request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ((cookie.getName()).compareTo("sId") == 0) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    this.response.addCookie(cookie);
                } else if ((cookie.getName()).compareTo("sTk") == 0) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    this.response.addCookie(cookie);
                }
            }
        }
        if (jwt == null) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("JWT")) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (jwt == null && user_id == null) {
            throw new HttpServletException(HttpStatus.AccessDenied, "You must be logged in");
        } else if (jwt != null) {
            Key secret = (Key) this.getContextAttr("secret");
            this.user = UserFactory.getInstance().loadUserFromJwt(jwt, secret, this.getHibernateQuery());
            if (this.user == null)
                throw new HttpServletException(HttpStatus.AccessDenied, "You must be logged in");
            String keyUser = this.user.getJsonWebToken().getKeyUser(jwt, secret);
            Map<String, Object> userProperties = this.getUserProperties(this.user.getDb_id());
            userProperties.put("keyUser", keyUser);
            userProperties.put("privateKey", user.getUserKeys().getDecipheredPrivateKey(keyUser));
        } else
            this.user = UserFactory.getInstance().loadUser(user_id, this.getHibernateQuery());
        String keyUser = (String) this.getUserProperties(this.user.getDb_id()).get("keyUser");
        for (TeamUser teamUser : user.getTeamUsers()) {
            Map<String, Object> teamProperties = this.getTeamProperties(teamUser.getTeam().getDb_id());
            String teamKey = (String) teamProperties.get("teamKey");
            if (teamUser.getTeamKey() == null && !teamUser.isDisabled() && teamKey != null)
                teamUser.lastRegistrationStep(keyUser, teamKey, this.getUserWebSocketManager(this.user.getDb_id()), this.getHibernateQuery());
            else if (teamUser.getTeamKey() != null)
                teamProperties.put("teamKey", AES.decrypt(teamUser.getTeamKey(), keyUser));
        }
    }

    protected abstract Date getCurrentTime() throws HttpServletException;

    public void needToBeTeamUser() throws HttpServletException {
        this.needToBeConnected();
        this.timestamp = this.getCurrentTime();
        if (this.getUser().getTeamUsers().isEmpty())
            throw new HttpServletException(HttpStatus.Forbidden);
    }

    public void needToBeTeamUserOfTeam(Team team) throws HttpServletException {
        this.needToBeTeamUser();
        this.timestamp = this.getCurrentTime();
        for (TeamUser teamUser : this.getUser().getTeamUsers()) {
            if (teamUser.getTeam().equals(team) && !team.isBlocked() && !teamUser.isDisabled() && teamUser.isVerified() && (teamUser.getDepartureDate() == null || this.timestamp.getTime() < teamUser.getDepartureDate().getTime()))
                return;
        }
        throw new HttpServletException(HttpStatus.Forbidden);
    }

    public void needToBeAdminOfTeam(Team team) throws HttpServletException {
        this.needToBeTeamUser();
        this.timestamp = this.getCurrentTime();
        for (TeamUser teamUser : this.getUser().getTeamUsers()) {
            if (teamUser.getTeam().equals(team) && !team.isBlocked() && teamUser.isTeamAdmin() && !teamUser.isDisabled() && teamUser.isVerified() && (teamUser.getDepartureDate() == null || this.timestamp.getTime() < teamUser.getDepartureDate().getTime()))
                return;
        }
        throw new HttpServletException(HttpStatus.Forbidden);
    }

    public void needToBeOwnerOfTeam(Team team) throws HttpServletException {
        this.needToBeTeamUser();
        for (TeamUser teamUser : this.getUser().getTeamUsers()) {
            this.timestamp = this.getCurrentTime();
            if (teamUser.getTeam().equals(team) && !team.isBlocked() && teamUser.isTeamOwner() && !teamUser.isDisabled() && teamUser.isVerified() && (teamUser.getDepartureDate() == null || this.timestamp.getTime() < teamUser.getDepartureDate().getTime()))
                return;
        }
        throw new HttpServletException(HttpStatus.Forbidden);
    }

    public void needToBeEaseAdmin() throws HttpServletException {
        this.needToBeConnected();
        if (!this.getUser().isAdmin())
            throw new HttpServletException(HttpStatus.Forbidden);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public DataBaseConnection getDB() throws HttpServletException {
        if (this.db == null) {
            try {
                this.db = new DataBaseConnection(DataBase.getConnection());
            } catch (SQLException e) {
                throw new HttpServletException(HttpStatus.InternError);
            }
        }

        return this.db;
    }

    private DataBaseConnection getLogsDb() throws HttpServletException {
        if (this.logDb == null) {
            try {
                this.logDb = new DataBaseConnection(LogsDatabase.getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new HttpServletException(HttpStatus.InternError);
            }
        }
        return this.logDb;
    }

    public HibernateQuery getHibernateQuery() {
        if (this.hibernateQuery == null)
            this.hibernateQuery = new HibernateQuery();
        if (!this.hibernateQuery.isOpen())
            this.hibernateQuery = new HibernateQuery();
        return this.hibernateQuery;
    }

    public void saveOrUpdate(Object hibernateObject) {
        this.getHibernateQuery().saveOrUpdateObject(hibernateObject);
    }

    public void deleteObject(Object hibernateObject) {
        this.getHibernateQuery().deleteObject(hibernateObject);
    }

    public void setRedirectUrl(String url) {
        this.redirectUrl = url;
    }

    public void setLogResponse(String msg) {
        this.logResponse = msg;
    }

    private void saveLogs() throws HttpServletException {
        StringBuilder argsString = new StringBuilder();
        Set<Entry<String, String>> setHm = args.entrySet();
        for (Entry<String, String> e : setHm) {
            argsString.append("<").append(e.getKey()).append(":").append(e.getValue()).append(">");
        }
        if (this.logResponse == null)
            this.logResponse = "Success";
        try {
            this.logResponse = URLEncoder.encode(this.logResponse, "UTF-8");
            argsString = new StringBuilder(URLEncoder.encode(argsString.toString(), "UTF-8"));
            DatabaseRequest request = this.getLogsDb().prepareRequest("INSERT INTO logs values(NULL, ?, ?, ?, ?, ?, default);");
            request.setString(this.servletName);
            request.setInt(this.response.getStatus());
            if (this.user == null)
                request.setNull();
            else
                request.setInt(this.user.getDb_id());
            request.setString(argsString.toString());
            request.setString(this.logResponse);
            request.set();
            System.out.println(request.toString());
            this.getLogsDb().close();
        } catch (UnsupportedEncodingException e) {
            this.setInternError();
        }
    }

    public void sendResponse() {
        user = (request.getSession().getAttribute("user") == null) ? user : (User) request.getSession().getAttribute("user");
        if (this.saveLogs) {
            try {
                saveLogs();
            } catch (HttpServletException e) {
                e.printStackTrace();
                System.err.println("Logs not sended to database.");
            }
        }
        if (this.response.getStatus() != HttpStatus.Success.getValue()) {
            try {
                if (this.db != null)
                    this.db.rollbackTransaction();
                if (this.hibernateQuery != null)
                    this.hibernateQuery.rollback();
            } catch (HttpServletException e) {
                System.err.println("Rollback transaction failed.");
            }
        }
        try {
            if (this.response.getStatus() != HttpStatus.Success.getValue()) {
                System.out.println("Error code: " + response.getStatus());
                if (this.errorMessage != null) {
                    this.response.setCharacterEncoding("UTF-8");
                    response.getWriter().print(this.errorMessage);
                } else
                    response.sendError(response.getStatus());
            } else {
                if (this.redirectUrl != null) {
                    System.out.println("redirect to " + this.redirectUrl);
                    response.sendRedirect(this.redirectUrl);
                } else {
                    this.response.setCharacterEncoding("UTF-8");
                    if (this.jsonArrayResponse == null && this.jsonObjectResponse == null) {
                        response.sendError(HttpStatus.InternError.getValue());
                        try {
                            if (this.db != null)
                                this.db.rollbackTransaction();
                            if (this.hibernateQuery != null)
                                this.hibernateQuery.rollback();
                        } catch (HttpServletException e) {
                            System.out.println("Rollback failed");
                        }
                    } else {
                        if (this.hibernateQuery != null)
                            try {
                                this.hibernateQuery.commit();
                            } catch (HttpServletException e) {
                                response.setStatus(e.getHttpStatus().getValue());
                                response.getWriter().print(e.getMsg());
                                if (this.db != null)
                                    this.db.close();
                                return;
                            }
                        PrintWriter out = response.getWriter();
                        if (this.jsonArrayResponse != null)
                            out.print(this.jsonArrayResponse.toString());
                        else
                            out.print(this.jsonObjectResponse.toString());
                        out.close();
                        out.flush();
                    }
                }

            }
        } catch (IOException e) {
            System.err.println("Send response failed.");
        }
        if (this.db != null)
            this.db.close();
    }

    public Object getContextAttr(String attr) {
        return request.getServletContext().getAttribute(attr);
    }

    public ServletContext getServletContext() {
        return request.getServletContext();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.getSession().setAttribute("is_admin", user.isAdmin());
        this.getSession().setAttribute("user_id", user.getDb_id());
    }

    public HttpSession getSession() {
        return request.getSession();
    }

    public Set<TeamUser> getTeamUsers() {
        return this.getUser().getTeamUsers();
    }

    public TeamUser getTeamUser(Team team) throws HttpServletException {
        return user.getTeamUser(team);
    }

    public TeamUser getTeamUser(Integer team_id) throws HttpServletException {
        return user.getTeamUser(team_id);
    }

    public Date getTimestamp() throws HttpServletException {
        if (this.timestamp == null)
            this.timestamp = this.getCurrentTime();
        return timestamp;
    }

    public User getUserWithToken() throws HttpServletException {
        this.needToBeConnected();
        return this.user;
    }

    public Map<String, Object> getUserProperties(Integer user_id) {
        Map<Integer, Map<String, Object>> userIdMap = (Map<Integer, Map<String, Object>>) this.getContextAttr("userIdMap");
        Map<String, Object> userProperties = userIdMap.get(user_id);
        if (userProperties == null) {
            userProperties = new ConcurrentHashMap<>();
            userIdMap.put(user_id, userProperties);
        }
        return userProperties;
    }

    public Map<String, Object> getTeamProperties(Integer team_id) {
        Map<Integer, Map<String, Object>> teamIdMap = (Map<Integer, Map<String, Object>>) this.getContextAttr("teamIdMap");
        Map<String, Object> teamProperties = teamIdMap.get(team_id);
        if (teamProperties == null) {
            teamProperties = new ConcurrentHashMap<>();
            teamIdMap.put(team_id, teamProperties);
        }
        return teamProperties;
    }

    public WebSocketManager getUserWebSocketManager(Integer user_id) {
        WebSocketManager webSocketManager = (WebSocketManager) this.getUserProperties(user_id).get("webSocketManager");
        if (webSocketManager == null) {
            webSocketManager = new WebSocketManager();
            this.getUserProperties(user_id).put("webSocketManager", webSocketManager);
        }
        return webSocketManager;
    }

    public WebSocketManager getTeamWebSocketManager(Integer team_id) {
        WebSocketManager webSocketManager = (WebSocketManager) this.getTeamProperties(team_id).get("webSocketManager");
        if (webSocketManager == null) {
            webSocketManager = new WebSocketManager();
            this.getTeamProperties(team_id).put("webSocketManager", webSocketManager);
        }
        return webSocketManager;
    }

    public NotificationManager getUserNotificationManager(Integer user_id) {
        NotificationManager notificationManager = (NotificationManager) this.getUserProperties(user_id).get("notificationManager");
        if (notificationManager == null)
            notificationManager = new NotificationManager();
        return notificationManager;
    }

    public Team getTeam(Integer id) throws HttpServletException {
        TeamManager teamManager = (TeamManager) this.getContextAttr("teamManager");
        Team team = teamManager.getTeam(id, this.getHibernateQuery());
        this.initializeTeamWithContext(team);
        return team;
    }

    public void initializeTeamWithContext(Team team) throws HttpServletException {
        try {
            if (team.getCustomer_id() != null) {
                Customer customer = (Customer) this.getTeamProperties(team.getDb_id()).get("customer");
                if (customer == null) {
                    customer = Customer.retrieve(team.getCustomer_id());
                    this.getTeamProperties(team.getDb_id()).put("customer", customer);
                }
                team.setCustomer(customer);
            }
            if (team.getSubscription_id() != null) {
                Subscription subscription = (Subscription) this.getTeamProperties(team.getDb_id()).get("subscription");
                if (subscription == null) {
                    subscription = Subscription.retrieve(team.getSubscription_id());
                    this.getTeamProperties(team.getDb_id()).put("subscription", subscription);
                }
                team.setSubscription(subscription);
            }
        } catch (StripeException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public String getKeyUser() {
        return (String) this.getUserProperties(this.getUser().getDb_id()).get("keyUser");
    }
}
