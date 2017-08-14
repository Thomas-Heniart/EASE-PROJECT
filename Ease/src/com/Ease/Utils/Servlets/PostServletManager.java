package com.Ease.Utils.Servlets;

import com.Ease.Team.Team;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.websocketV1.WebSocketMessage;
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
        Long now = this.getLongParam("now", false);
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
        Long timestamp = this.getLongParam("timestamp", false);
        if (timestamp == null)
            throw new HttpServletException(HttpStatus.BadRequest, "Missing current time.");
        return new Date(timestamp);
    }

    public Object getParam(String paramName, boolean saveInLogs) {
        Object param = params.get(paramName);
        if (param != null && saveInLogs)
            args.put(paramName, param.toString());
        return param;
    }

    public String getStringParam(String paramName, boolean saveInLogs) {
        return (String) getParam(paramName, saveInLogs);
    }

    public Long getLongParam(String paramName, boolean saveInLogs) {
        return (Long) getParam(paramName, saveInLogs);
    }

    public Integer getIntParam(String paramName, boolean saveInLogs) {
        Long param = getLongParam(paramName, saveInLogs);
        if (param == null)
            return null;
        else
            return Math.toIntExact(getLongParam(paramName, saveInLogs));
    }

    public Boolean getBooleanParam(String paramName, boolean saveInLogs) {
        return (Boolean) this.getParam(paramName, saveInLogs);
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
        String ws_id = this.getStringParam("ws_id", false);
        if (ws_id == null)
            return;
        Integer team_id = this.getIntParam("team_id", false);
        try {
            if (team_id != null) {
                Integer channel_id = this.getIntParam("channel_id", false);
                Team team = this.getTeamUserForTeamId(team_id).getTeam();
                if (channel_id == null)
                    team.getWebSocketManager().sendObjects(this.webSocketMessages, ws_id);
                else
                    team.getChannelWithId(channel_id).getWebSocketManager().sendObjects(this.webSocketMessages, ws_id);
            }
        } catch (HttpServletException e) {
            e.printStackTrace();
        }
    }
}
