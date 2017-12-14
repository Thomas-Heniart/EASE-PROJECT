package com.Ease.Utils.Servlets;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.websocketV1.WebSocketManager;
import com.Ease.websocketV1.WebSocketMessage;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PostServletManager extends ServletManager {

    protected JSONObject params;
    protected List<WebSocketMessage> webSocketMessages = new LinkedList<>();

    public PostServletManager(String servletName, HttpServletRequest request, HttpServletResponse response, boolean saveLogs) throws IOException {
        super(servletName, request, response, saveLogs);
        if (ServletFileUpload.isMultipartContent(request))
            return;
        String contentType = request.getHeader("Content-Type");
        if (contentType == null || (!contentType.contains("application/json") && !contentType.contains("application/JSON")))
            return;
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder json = new StringBuilder();
        String buffer;
        while ((buffer = br.readLine()) != null)
            json.append(buffer);
        br.close();
        if (!json.toString().startsWith("{"))
            return;
        params = new JSONObject(json.toString());
    }

    public WebSocketManager getSessionWebSocketManager() {
        WebSocketManager webSocketManager = (WebSocketManager) this.getSession().getAttribute("webSocketManager");
        if (webSocketManager == null) {
            webSocketManager = new WebSocketManager();
            this.getSession().setAttribute("webSocketManager", webSocketManager);
        }
        return webSocketManager;
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

    @Override
    protected Date getCurrentTime() throws HttpServletException {
        /* Long timestamp = this.getLongParam("timestamp", false, false);
        return new Date(timestamp); */
        return new Date();
    }

    public Object getParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        if (params == null)
            return null;
        Object param = params.get(paramName);
        if (param == null && !canBeNull)
            throw new HttpServletException(HttpStatus.BadRequest, "Missing parameter: " + paramName);
        if (param != null && saveInLogs)
            args.put(paramName, param.toString());
        return param;
    }

    public JSONObject getJsonParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            return params.getJSONObject(paramName);
        } catch (Exception e) {
            if (!canBeNull)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + paramName + " type (Expected JSON).");
            return null;
        }
    }

    public JSONArray getArrayParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            return params.getJSONArray(paramName);
        } catch (Exception e) {
            if (canBeNull)
                return null;
            throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + paramName + " type (Expected Array).");
        }
    }

    public String getStringParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            if (params == null)
                return request.getParameter(paramName);
            else
                return params.getString(paramName);
        } catch (Exception e) {
            if (canBeNull)
                return null;
            throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + paramName + " type (Expected String).");
        }

    }

    public Long getLongParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            return params.getLong(paramName);
        } catch (Exception e) {
            if (canBeNull)
                return null;
            throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + paramName + " type (Expected Long).");
        }
    }

    public Integer getIntParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            return params.getInt(paramName);
        } catch (Exception e) {
            if (canBeNull)
                return null;
            throw new HttpServletException(HttpStatus.BadRequest, "Expected parameter: " + paramName + " type Integer");
        }
    }

    public Boolean getBooleanParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            return params.getBoolean(paramName);
        } catch (Exception e) {
            if (canBeNull)
                return null;
            throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + paramName + " type (Expected Boolean).");
        }
    }

    public void addWebSocketMessage(WebSocketMessage webSocketMessage) {
        this.webSocketMessages.add(webSocketMessage);
    }

    @Override
    public void sendResponse() {
        super.sendResponse();
        if (this.response.getStatus() != HttpStatus.Success.getValue())
            return;
        if (this.webSocketMessages.isEmpty())
            return;
        try {
            String ws_id = this.getStringParam("ws_id", false, true);
            System.out.println("User WSM size: " + this.getUserWebSocketManager(this.getUser().getDb_id()).getWebSocketSessions().size());
            if (this.team != null) {
                WebSocketManager webSocketManager = this.getTeamWebSocketManager(team.getDb_id());
                System.out.println("Team id: " + team.getDb_id() + " Team WSM size: " + webSocketManager.getWebSocketSessions().size());
                webSocketManager.sendObjects(this.webSocketMessages, ws_id);
            } else {
                if (this.getUser() != null)
                    this.getUserWebSocketManager(this.getUser().getDb_id()).sendObjects(this.webSocketMessages, ws_id);
            }
        } catch (HttpServletException e) {
            e.printStackTrace();
        }
    }

    public String getBody() {
        return params.toString();
    }
}
