package com.Ease.websocketV1;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class WebSocketMessageDecoder implements Decoder.Text<WebSocketMessage> {
    @Override
    public WebSocketMessage decode(String s) throws DecodeException {
        JSONObject res = new JSONObject(StringEscapeUtils.unescapeHtml4(s));
        return new WebSocketMessage((String) res.get("type"), (JSONObject) res.get("data"), (String) res.get("action"));
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
