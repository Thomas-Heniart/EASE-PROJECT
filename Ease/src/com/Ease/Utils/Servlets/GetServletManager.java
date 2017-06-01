package com.Ease.Utils.Servlets;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetServletManager extends ServletManager {

    public GetServletManager(String servletName, HttpServletRequest request, HttpServletResponse response, boolean saveLogs) throws IOException {
        super(servletName, request, response, saveLogs);
    }

    public String getParam(String paramName, boolean saveInLogs) {
        String param = StringEscapeUtils.escapeHtml4(request.getParameter(paramName));
        if (param != null && saveInLogs)
            args.put(paramName, param);
        return param;
    }

    public Integer getIntParam(String paramName, boolean saveInLogs) {
        return Integer.valueOf(this.getParam(paramName, saveInLogs));
    }
}
