package com.Ease.websocketV1;

import org.json.simple.JSONObject;

public class TeamWebSocketMessage extends WebSocketMessage {

    private JSONObject target;

    public JSONObject getTarget() {
        return target;
    }

    public TeamWebSocketMessage(String data_type, String action, JSONObject data, JSONObject target) {
        super("UPDATE_CLIENT", data, data_type + "_" + action);
        this.target = target;
    }

    public TeamWebSocketMessage(String data_type, String action, JSONObject data) {
        super("UPDATE_CLIENT", data, data_type + "_" + action);
    }

    public TeamWebSocketMessage(String data_type, String action, Integer id, JSONObject target) {
        super("UPDATE_CLIENT", new JSONObject(), data_type + "_" + action);
        this.getData().put("id", id);
        this.target = target;
    }

    public TeamWebSocketMessage(String data_type, String action, Integer id) {
        super("UPDATE_CLIENT", new JSONObject(), data_type + "_" + action);
        this.getData().put("id", id);
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject res = super.toJSONObject();
        if (this.getTarget() != null)
            res.put("target", this.getTarget());
        return res;
    }
}
