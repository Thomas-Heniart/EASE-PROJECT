package com.Ease.Utils.Servlets;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class PostServletManager extends ServletManager {

    protected JSONObject params;

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
}
