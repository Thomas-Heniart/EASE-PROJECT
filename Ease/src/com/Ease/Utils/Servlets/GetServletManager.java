package com.Ease.Utils.Servlets;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class GetServletManager extends ServletManager {

    public GetServletManager(String servletName, HttpServletRequest request, HttpServletResponse response, boolean saveLogs) throws IOException {
        super(servletName, request, response, saveLogs);
    }

    @Override
    protected Date getCurrentTime() throws HttpServletException {
        String timestamp = this.getParam("timestamp", false);
        if (timestamp == null || timestamp.equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "Missing current time.");
        return new Date(Long.valueOf(timestamp));
    }

    public String getParam(String paramName, boolean saveInLogs) {
        String param = request.getParameter(paramName);
        if (param != null) {
            param = StringEscapeUtils.escapeHtml4(request.getParameter(paramName));
            if (saveInLogs)
                args.put(paramName, param);
        }
        return param;
    }

    public Integer getIntParam(String paramName, boolean saveInLogs) {
        String param = this.getParam(paramName, saveInLogs);
        if (param == null)
            return null;
        return Integer.valueOf(param);
    }
}
