package com.Ease.websocketV1;

import org.json.simple.JSONObject;

public class TeamWebSocketMessage extends WebSocketMessage {

    public TeamWebSocketMessage(String data_type, String action, JSONObject data, JSONObject target) {
        super("UPDATE_CLIENT");
        this.getData().put("data", data);
        this.getData().put("type", data_type);
        this.getData().put("action", action);
        this.getData().put("target", target);
    }

    public TeamWebSocketMessage(String data_type, String action, Integer id, JSONObject target) {
        super("UPDATE_CLIENT");
        JSONObject data = new JSONObject();
        data.put("id", id);
        this.getData().put("data", data);
        this.getData().put("type", data_type);
        this.getData().put("action", action);
        this.getData().put("target", target);
    }
}
