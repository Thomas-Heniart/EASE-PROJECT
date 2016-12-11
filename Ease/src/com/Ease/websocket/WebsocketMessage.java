package com.Ease.websocket;

import org.json.simple.JSONObject;

import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.Profile.Profile;

public class WebsocketMessage {
	public enum Who {
		ALLTABS,
		OTHERTABS,
		THISTAB
	}
	
	public static WebsocketMessage connectionMessage() {
		return new WebsocketMessage("connect", new JSONObject(), Who.OTHERTABS);
	}
	
	public static WebsocketMessage assignIdMessage(String wSessionId) {
		JSONObject data = new JSONObject();
		data.put("socketId", wSessionId);
		return new WebsocketMessage("setSocketId", data, Who.OTHERTABS);
	}
	
	public static WebsocketMessage addClassicAppMessage(ClassicApp app) {
		JSONObject data = new JSONObject();
		// TODO set data
		return new WebsocketMessage ("addClassicApp", data, Who.OTHERTABS);
	}
	
	public static WebsocketMessage addLogWithAppMessage(ClassicApp app) {
		JSONObject data = new JSONObject();
		// TODO set data
		return new WebsocketMessage ("addLogWithApp", data, Who.OTHERTABS);
	}
	
	public static WebsocketMessage addEmptyAppMessage (WebsiteApp app) {
		JSONObject data = new JSONObject();
		// TODO set data
		return new WebsocketMessage ("addEmptyApp", data, Who.OTHERTABS);
	}
	
	public static WebsocketMessage addProfile (Profile profile) {
		JSONObject data = new JSONObject();
		// TODO set data
		return new WebsocketMessage ("addProfile", data, Who.OTHERTABS);
	}
	
	protected String action;
	protected JSONObject data;
	protected Who 	who;
	//protected Map<String, JSONObject> mapStringJson;
	//protected Map<String, String> mapStringString;
	//protected Map<String, JSONArray> mapStringArray;
	
	public WebsocketMessage(String action, JSONObject data, Who who) {
		this.action = action;
		this.data = data;
		this.who = who;
		//this.mapStringJson = new HashMap<String, JSONObject>();
		//this.mapStringArray = new HashMap<String, JSONArray>();
		//this.mapStringString = new HashMap<String, String>();
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
	
	public Who getWho() {
		return who;
	}
	
	/*public void add(String argName, JSONObject obj) {
		this.mapStringJson.put(argName, obj);
	}
	
	public void add(String argName, JSONArray obj) {
		this.mapStringArray.put(argName, obj);
	}
	
	public void add(String argName, String obj) {
		this.mapStringString.put(argName, obj);
	}*/
	
	@SuppressWarnings("unchecked")
	public JSONObject getJSON() {
		JSONObject res = new JSONObject();
		res.put("action", this.action);
		res.put("data", this.data);
		/*for (Map.Entry<String, JSONObject> entry : this.mapStringJson.entrySet())
			res.put(entry.getKey(), entry.getValue());
		for (Map.Entry<String, JSONArray> entry : this.mapStringArray.entrySet())
			res.put(entry.getKey(), entry.getValue());
		for (Map.Entry<String, String> entry : this.mapStringString.entrySet())
			res.put(entry.getKey(), entry.getValue());*/
		return res;
	}
	
	public String getJSONString() {
		return this.getJSON().toString();
	}
}
