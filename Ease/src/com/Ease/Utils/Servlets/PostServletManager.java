package com.Ease.Utils.Servlets;

import com.Ease.Team.Team;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.websocketV1.WebSocketMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PostServletManager extends ServletManager {

    protected JSONObject params;
    protected List<WebSocketMessage> webSocketMessages = new LinkedList<>();

    public PostServletManager(String servletName, HttpServletRequest request, HttpServletResponse response, boolean saveLogs) throws IOException {
        super(servletName, request, response, saveLogs);
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        String buffer;
        while ((buffer = br.readLine()) != null)
            json += buffer;
        br.close();
        if (!json.startsWith("{"))
            return;
        JSONParser jsonParser = new JSONParser();
        try {
            params = (JSONObject) jsonParser.parse(json);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IOException();
        }
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
        Long timestamp = this.getLongParam("timestamp", false, false);
        return new Date(timestamp);
    }

    public Object getParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        Object param = params.get(paramName);
        if (param == null && !canBeNull)
            throw new HttpServletException(HttpStatus.BadRequest, "Missing parameter: " + paramName);
        if (param != null && saveInLogs)
            args.put(paramName, param.toString());
        return param;
    }

    public JSONObject getJsonParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            return (JSONObject) getParam(paramName, saveInLogs, canBeNull);
        } catch (ClassCastException e) {
            throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + paramName + " type (Expected JSON).");
        }
    }

    public JSONArray getArrayParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            return (JSONArray) getParam(paramName, saveInLogs, canBeNull);
        } catch (ClassCastException e) {
            throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + paramName + " type (Expected Array).");
        }
    }

    public String getStringParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            String param = (String) this.getParam(paramName, saveInLogs, canBeNull);
            if (!canBeNull && param.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, paramName + " cannot be empty");
            return param;
        } catch (ClassCastException e) {
            throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + paramName + " type (Expected String).");
        }

    }

    public Long getLongParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            return (Long) getParam(paramName, saveInLogs, canBeNull);
        } catch (ClassCastException e) {
            throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter " + paramName + " type (Expected Long).");
        }
    }

    public Integer getIntParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        Long param = getLongParam(paramName, saveInLogs, canBeNull);
        if (param == null)
            return null;
        else
            return Math.toIntExact(param);
    }

    public Boolean getBooleanParam(String paramName, boolean saveInLogs, boolean canBeNull) throws HttpServletException {
        try {
            return (Boolean) this.getParam(paramName, saveInLogs, canBeNull);
        } catch (ClassCastException e) {
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
            if (ws_id == null || ws_id.equals("-1"))
                return;
            Integer team_id = this.getIntParam("team_id", false, true);
            if (team_id != null) {
                Team team = this.getTeamUserForTeamId(team_id).getTeam();
                System.out.println("WebSocketMessage to team " + team_id);
                team.getWebSocketManager().sendObjects(this.webSocketMessages, ws_id);
            } else {
                if (this.user != null)
                    this.user.getWebSocketManager().sendObjects(this.webSocketMessages, ws_id);
            }
        } catch (HttpServletException e) {
            e.printStackTrace();
        }
    }

    /* Horrible glitch */
    public void setParam(String key, Object value) {
        this.params.put(key, value);
    }

    public String getBody() {
        return params.toString();
    }
}
