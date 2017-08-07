package com.Ease.Context;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServletRequestListenerImpl implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {
        ServletRequest request = servletRequestEvent.getServletRequest();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String json = "";
            String buffer;
            while ((buffer = br.readLine()) != null)
                json += buffer;
            br.close();
            if (!json.startsWith("{"))
                return;
            JSONParser jsonParser = new JSONParser();
            JSONObject params = (JSONObject) jsonParser.parse(json);
            request.setAttribute("params", params);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
