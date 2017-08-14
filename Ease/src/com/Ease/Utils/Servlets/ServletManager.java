package com.Ease.Utils.Servlets;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.*;
import com.stripe.exception.StripeException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

public abstract class ServletManager {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String redirectUrl;
    protected String servletName;
    protected Map<String, String> args = new HashMap<>();
    protected User user;
    protected TeamUser teamUser;
    protected DataBaseConnection db;
    private DataBaseConnection logDb;
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
        this.user = (User) request.getSession().getAttribute("user");
        this.teamUser = (TeamUser) request.getSession().getAttribute("teamUser");
        this.saveLogs = saveLogs;
    }

    protected void setSuccess() {
        response.setStatus(HttpStatus.Success.getValue());
        response.setContentType("application/json");
    }

    protected void setInternError() {
        response.setStatus(HttpStatus.InternError.getValue());
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
            System.out.println("Error code: " + httpServletException.getHttpStatus());
            if (httpServletException.getMsg() == null && httpServletException.getJsonObject() != null) {
                response.setContentType("application/json");
                this.errorMessage = httpServletException.getJsonObject().toString();
            } else
                this.errorMessage = httpServletException.getMsg();
            System.out.println(this.errorMessage);
            response.setStatus(httpServletException.getHttpStatus().getValue());
        } catch (ClassCastException e1) {
            e.printStackTrace();
            this.setInternError();
        }
    }

    public void setError(StripeException e) {
        System.out.println("Error code: " + e.getStatusCode() + " and msg: " + e.getMessage());
        response.setStatus(e.getStatusCode());
        this.errorMessage = e.getMessage();
    }

    public void needToBeConnected() throws HttpServletException {
        if (user == null) {
            throw new HttpServletException(HttpStatus.AccessDenied);
        } else {
            /*socketId = request.getParameter("socketId");
            if (!debug && socketId == null) {
				throw new GeneralException(Code.ClientError, "No socketId.");
			} else if (user.getWebsockets().containsKey(socketId) == false) {
				System.out.println(user.getWebsockets().size());
				throw new GeneralException(Code.ClientError, "Wrong socketId.");
			}*/
        }
    }

    protected abstract Date getCurrentTime() throws HttpServletException;

    public void needToBeTeamUser() throws HttpServletException {
        this.needToBeConnected();
        this.timestamp = this.getCurrentTime();
        if (this.getUser().getTeamUsers().isEmpty())
            throw new HttpServletException(HttpStatus.Forbidden);
    }

    public void needToBeTeamUserOfTeam(String team_id) throws HttpServletException {
        this.needToBeAdminOfTeam(Integer.parseInt(team_id));
    }

    public void needToBeTeamUserOfTeam(Integer team_id) throws HttpServletException {
        if (team_id == null)
            throw new HttpServletException(HttpStatus.BadRequest, "Missing team id parameter");
        this.needToBeTeamUser();
        this.timestamp = this.getCurrentTime();
        for (TeamUser teamUser : this.getUser().getTeamUsers()) {
            if (teamUser.getTeam().getDb_id().equals(team_id) && !teamUser.isDisabled() && teamUser.isVerified() && (teamUser.getDepartureDate() == null || this.timestamp.getTime() < teamUser.getDepartureDate().getTime()))
                return;
        }
        throw new HttpServletException(HttpStatus.Forbidden);
    }

    public void needToBeAdminOfTeam(Team team) throws HttpServletException {
        this.needToBeTeamUser();
        this.timestamp = this.getCurrentTime();
        for (TeamUser teamUser : this.getUser().getTeamUsers()) {
            if (teamUser.getTeam() == team && teamUser.isTeamAdmin() && !teamUser.isDisabled() && teamUser.isVerified() && (teamUser.getDepartureDate() == null || this.timestamp.getTime() < teamUser.getDepartureDate().getTime()))
                return;
        }
        throw new HttpServletException(HttpStatus.Forbidden);
    }

    public void needToBeAdminOfTeam(Integer team_id) throws HttpServletException {
        this.needToBeTeamUser();
        if (team_id == null)
            throw new HttpServletException(HttpStatus.BadRequest, "Missing team id parameter");
        this.timestamp = this.getCurrentTime();
        for (TeamUser teamUser : this.getUser().getTeamUsers()) {
            if (teamUser.getTeam().getDb_id().equals(team_id) && teamUser.isTeamAdmin() && !teamUser.isDisabled() && teamUser.isVerified() && (teamUser.getDepartureDate() == null || this.timestamp.getTime() < teamUser.getDepartureDate().getTime()))
                return;
        }
        throw new HttpServletException(HttpStatus.Forbidden);
    }

    public void needToBeOwnerOfTeam(Team team) throws HttpServletException {
        this.needToBeTeamUser();
        for (TeamUser teamUser : this.getUser().getTeamUsers()) {
            this.timestamp = this.getCurrentTime();
            if (teamUser.getTeam() == team && teamUser.isTeamOwner() && !teamUser.isDisabled() && teamUser.isVerified() && (teamUser.getDepartureDate() == null || this.timestamp.getTime() < teamUser.getDepartureDate().getTime()))
                return;
        }
        throw new HttpServletException(HttpStatus.Forbidden);
    }

    public void needToBeOwnerOfTeam(Integer team_id) throws HttpServletException {
        this.needToBeTeamUser();
        if (team_id == null)
            throw new HttpServletException(HttpStatus.BadRequest, "Missing team id parameter");
        this.timestamp = this.getCurrentTime();
        for (TeamUser teamUser : this.getUser().getTeamUsers()) {
            if (teamUser.getTeam().getDb_id().equals(team_id) && teamUser.isTeamOwner() && !teamUser.isDisabled() && teamUser.isVerified() && (teamUser.getDepartureDate() == null || this.timestamp.getTime() < teamUser.getDepartureDate().getTime()))
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
                throw new HttpServletException(HttpStatus.InternError);
            }
        }
        return this.logDb;
    }

    public HibernateQuery getHibernateQuery() {
        if (this.hibernateQuery == null)
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

    private void saveLogs() throws GeneralException, HttpServletException {
        String argsString = "";
        Set<Entry<String, String>> setHm = args.entrySet();
        for (Entry<String, String> e : setHm) {
            argsString += "<" + e.getKey() + ":" + e.getValue() + ">";
        }
        if (this.logResponse == null)
            this.logResponse = "Success";
        try {
            this.logResponse = URLEncoder.encode(this.logResponse, "UTF-8");
            argsString = URLEncoder.encode(argsString, "UTF-8");
            DatabaseRequest request = this.getLogsDb().prepareRequest("INSERT INTO logs values(NULL, ?, ?, ?, ?, ?, default);");
            request.setString(this.servletName);
            request.setInt(this.response.getStatus());
            if (this.user == null)
                request.setNull();
            else
                request.setInt(this.user.getDBid());
            request.setString(argsString);
            request.setString(this.logResponse);
            request.set();
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
            } catch (GeneralException e) {
                System.out.println(e.getMsg());
                System.err.println("Logs not sended to database.");
            } catch (HttpServletException e) {
                e.printStackTrace();
            }
        }
        if (this.response.getStatus() != HttpStatus.Success.getValue()) {
            try {
                if (this.db != null)
                    this.db.rollbackTransaction();
                if (this.hibernateQuery != null)
                    this.hibernateQuery.rollback();
            } catch (GeneralException e) {
                System.err.println("Rollback transaction failed.");
            }
        }

        try {
            if (this.response.getStatus() != HttpStatus.Success.getValue()) {
                System.out.println("Error code: " + response.getStatus());
                if (this.errorMessage != null)
                    response.getWriter().print(this.errorMessage);
                else
                    response.sendError(response.getStatus());
            } else {
                if (this.redirectUrl != null) {
                    System.out.println("redirect to " + this.redirectUrl);
                    response.sendRedirect(this.redirectUrl);
                } else {
                    if (this.jsonArrayResponse == null && this.jsonObjectResponse == null) {
                        response.sendError(HttpStatus.InternError.getValue());
                        try {
                            if (this.db != null)
                                this.db.rollbackTransaction();
                            if (this.hibernateQuery != null)
                                this.hibernateQuery.rollback();
                        } catch (GeneralException e) {
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
                        if (this.jsonArrayResponse != null)
                            response.getWriter().print(this.jsonArrayResponse.toString());
                        else
                            response.getWriter().print(this.jsonObjectResponse.toString());
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
        this.user = user;
        this.getSession().setAttribute("user", user);
    }

    public TeamUser getTeamUser() {
        return teamUser;
    }

    public HttpSession getSession() {
        return request.getSession();
    }

    public List<TeamUser> getTeamUsers() {
        return this.getUser().getTeamUsers();
    }

    public TeamUser getTeamUserForTeam(Team team) throws HttpServletException {
        for (TeamUser teamUser : this.getTeamUsers()) {
            if (teamUser.getTeam() == team && !teamUser.isDisabled() && teamUser.isVerified())
                return teamUser;
        }
        throw new HttpServletException(HttpStatus.BadRequest);
    }

    public TeamUser getTeamUserForTeamId(Integer team_id) throws HttpServletException {
        if (team_id == null)
            throw new HttpServletException(HttpStatus.BadRequest, "Missing team id parameter");
        TeamManager teamManager = (TeamManager) this.getContextAttr("teamManager");
        Team team = teamManager.getTeamWithId(team_id);
        for (TeamUser teamUser : this.getTeamUsers()) {
            if (teamUser.getTeam() == team && !teamUser.isDisabled() && teamUser.isVerified())
                return teamUser;
        }
        return null;
    }

    public Date getTimestamp() throws HttpServletException {
        if (this.timestamp == null)
            this.timestamp = this.getCurrentTime();
        return timestamp;
    }
}
