package com.Ease.websocketV1;

import org.json.simple.JSONObject;

public class WebSocketMessage {
    private String type;
    private Object data = new JSONObject();

    public WebSocketMessage(String type){
        this.type = type;
    }

    public WebSocketMessage(String type, JSONObject data) {
        this.type = type;
        this.data = data;
    }

    public WebSocketMessage(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType(){
        return this.type;
    }

    public Object getData() {
        return this.data;
    }

    public JSONObject toJSONObject(){
        JSONObject res = new JSONObject();
        res.put("type", this.type);
        res.put("data", this.data);
        return res;
    }
}
