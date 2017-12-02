package com.Ease.websocketV1;

import org.json.simple.JSONObject;

public class WebSocketMessage {
    private String type;
    private Object data = new JSONObject();
    private String action;

    public WebSocketMessage(String type) {
        this.type = type;
    }

    public WebSocketMessage(String type, JSONObject data, String action) {
        this.type = type;
        this.data = data;
        this.action = action;
    }

    public WebSocketMessage(String type, String id) {
        this.type = type;
        this.data = id;
    }

    public String getType() {
        return this.type;
    }

    public JSONObject getData() {
        return (JSONObject) this.data;
    }

    public String getAction() {
        return action;
    }

    public JSONObject toJSONObject() {
        JSONObject res = new JSONObject();
        res.put("type", this.type);
        res.put("data", this.data);
        res.put("action", this.getAction());
        return res;
    }

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "type='" + type + '\'' +
                ", data=" + data +
                ", action='" + action + '\'' +
                '}';
    }
}
