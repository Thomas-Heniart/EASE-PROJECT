package com.Ease.websocketV1;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class WebSocketMessageEncoder implements Encoder.Text<WebSocketMessage>{
    @Override
    public String encode(WebSocketMessage webSocketMessage) throws EncodeException {
        return webSocketMessage.toJSONObject().toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {

    }
}
