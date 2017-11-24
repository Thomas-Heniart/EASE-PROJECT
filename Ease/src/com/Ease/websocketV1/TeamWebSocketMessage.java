package com.Ease.websocketV1;

import org.json.simple.JSONObject;

public class TeamWebSocketMessage extends WebSocketMessage {

    public TeamWebSocketMessage(String data_type, String action, JSONObject data, JSONObject target) {
        super("UPDATE_CLIENT", new JSONObject());
        JSONObject thisData = (JSONObject) this.getData();
        thisData.put("data", data);
        thisData.put("action", data_type + "_" + action);
        thisData.put("target", target);
    }

    public TeamWebSocketMessage(String data_type, String action, JSONObject data) {
        super("UPDATE_CLIENT", new JSONObject());
        JSONObject thisData = (JSONObject) this.getData();
        thisData.put("data", data);
        thisData.put("action", data_type + "_" + action);
    }

    public TeamWebSocketMessage(String data_type, String action, Integer id, JSONObject target) {
        super("UPDATE_CLIENT", new JSONObject());
        JSONObject data = new JSONObject();
        data.put("id", id);
        JSONObject thisData = (JSONObject) this.getData();
        thisData.put("data", data);
        thisData.put("action", data_type + "_" + action);
        thisData.put("target", target);
    }
}
