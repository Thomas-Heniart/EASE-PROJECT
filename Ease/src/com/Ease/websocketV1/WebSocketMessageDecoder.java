package com.Ease.websocketV1;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class WebSocketMessageDecoder implements Decoder.Text<WebSocketMessage>{
    @Override
    public WebSocketMessage decode(String s) throws DecodeException {
        JSONParser parser = new JSONParser();
        JSONObject res;
        try {
            res = (JSONObject) parser.parse(StringEscapeUtils.unescapeHtml4(s));
            return new WebSocketMessage((String)res.get("type"), (String)res.get("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean willDecode(String s) {
        return s != null && s.length() > 0;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
