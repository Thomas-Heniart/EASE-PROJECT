package com.Ease.websocketV1;

import org.json.JSONObject;

public class UserWebSocketMessage extends WebSocketMessage {
    public UserWebSocketMessage(String data_type, String action, JSONObject data) {
        super("UPDATE_CLIENT", data, data_type + "_" + action);
    }

    public UserWebSocketMessage(String data_type, String action, Integer id) {
        super("UPDATE_CLIENT", new JSONObject(), data_type + "_" + action);
        this.getData().put("id", id);
    }

    public UserWebSocketMessage(WebSocketMessageType type, JSONObject data) {
        super("UPDATE_CLIENT", data, type.name());
    }
}
