package com.Ease.websocket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Utils.ServletManager;

@SuppressWarnings("unchecked")
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
	
	public static WebsocketMessage addProfileMessage (Profile profile) {
		JSONObject data = new JSONObject();
		data.put("name", profile.getName());
		data.put("color", profile.getColor());
		data.put("profile_id", profile.getSingleId());
		return new WebsocketMessage ("addProfile", data, Who.OTHERTABS);
	}
	
	public static WebsocketMessage removeProfileMessage(String profile_id) {
		JSONObject data = new JSONObject();
		data.put("profile_id", profile_id);
		return new WebsocketMessage("removeProfile", data, Who.OTHERTABS);
	}
	
	public static WebsocketMessage removeAppMessage(String app_id) {
		JSONObject data = new JSONObject();
		data.put("app_id", app_id);
		return new WebsocketMessage("removeApp", data, Who.OTHERTABS);
	}
	
	public static WebsocketMessage logoutMessage() {
		return new WebsocketMessage("logout", Who.OTHERTABS);
	}
	
	public static WebsocketMessage pingMessage() {
		return new WebsocketMessage("ping", Who.THISTAB);
	}
	
	protected String action;
	protected JSONObject data;
	protected Who 	who;
	
	public WebsocketMessage(String action, JSONObject data, Who who) {
		this.action = action;
		this.data = data;
		this.who = who;
	}
	
	public WebsocketMessage(String action, Who who) {
		this.action = action;
		this.data = new JSONObject();
		this.who = who;
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

	public static WebsocketMessage moveProfileMessage(String profile_id, String profileIdx, String columnIdx) {
		JSONObject data = new JSONObject();
		data.put("profileIdx", profileIdx);
		data.put("columnIdx", columnIdx);
		data.put("profile_id", profile_id);
		return new WebsocketMessage("moveProfile", data, Who.OTHERTABS);
	}
}
