package com.Ease.websocketV1;

import org.json.simple.JSONObject;

public class TeamWebSocketMessage extends WebSocketMessage {
    protected String action;
    protected JSONObject target;

    public TeamWebSocketMessage(String type, String action, JSONObject data, JSONObject target) {
        super(type, data);
        this.action = action;
        this.target = target;
    }

    public String getAction() {
        return action;
    }

    public JSONObject getTarget() {
        return target;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject res = super.toJSONObject();
        res.put("action", this.action);
        res.put("target", this.target);
        return res;
    }
}
