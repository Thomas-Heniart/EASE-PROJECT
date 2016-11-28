package com.Ease.websocket;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class WebsocketMessage {
	protected String action;
	protected JSONObject data;
	protected Map<String, JSONObject> mapStringJson;
	protected Map<String, String> mapStringString;
	protected Map<String, JSONArray> mapStringArray;
	
	public WebsocketMessage(String action, JSONObject data) {
		this.action = action;
		this.data = data;
		this.mapStringJson = new HashMap<String, JSONObject>();
		this.mapStringArray = new HashMap<String, JSONArray>();
		this.mapStringString = new HashMap<String, String>();
	}
	
	public String getAction() {
		return this.action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public JSONObject getData() {
		return this.data;
	}
	
	public void setData(JSONObject data) {
		this.data = data;
	}
	
	public void add(String argName, JSONObject obj) {
		this.mapStringJson.put(argName, obj);
	}
	
	public void add(String argName, JSONArray obj) {
		this.mapStringArray.put(argName, obj);
	}
	
	public void add(String argName, String obj) {
		this.mapStringString.put(argName, obj);
	}
	
	public JSONObject getJSON() {
		JSONObject res = new JSONObject();
		res.put("action", this.action);
		res.put("data", this.data);
		for (Map.Entry<String, JSONObject> entry : this.mapStringJson.entrySet())
			res.put(entry.getKey(), entry.getValue());
		for (Map.Entry<String, JSONArray> entry : this.mapStringArray.entrySet())
			res.put(entry.getKey(), entry.getValue());
		for (Map.Entry<String, String> entry : this.mapStringString.entrySet())
			res.put(entry.getKey(), entry.getValue());
		return res;
	}
	
	public String getJSONString() {
		return this.getJSON().toString();
	}
}
