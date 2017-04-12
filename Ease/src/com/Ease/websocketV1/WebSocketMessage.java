package com.Ease.websocketV1;

import org.json.simple.JSONObject;

public class WebSocketMessage {
    private String type;
    private String data;


    public WebSocketMessage(String type){
        this.type = type;
        this.data = null;
    }

    public WebSocketMessage(String type, String data){
        this.type = type;
        this.data = data;
    }

    public String getType(){
        return this.type;
    }
    public String getData(){
        return this.data;
    }

    public JSONObject toJSONObject(){
        JSONObject res = new JSONObject();
        res.put("type", this.type);
        res.put("data", this.data);
        return res;
    }
}
