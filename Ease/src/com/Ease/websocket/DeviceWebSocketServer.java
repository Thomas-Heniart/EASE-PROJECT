package com.Ease.websocket;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.model.Device;

@ApplicationScoped
@ServerEndpoint("/actions")
public class DeviceWebSocketServer {

	@Inject
	private final DeviceSessionHandler sessionHandler = DeviceSessionHandler.getInstance();
	
	@OnOpen
	public void open(Session session) {
		sessionHandler.addSession(session);
	}

	@OnClose
	public void close(Session session) {
		sessionHandler.removeSession(session);
	}

	@OnError
	public void onError(Throwable error) {
		Logger.getLogger(DeviceWebSocketServer.class.getName()).log(Level.SEVERE, null, error);
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject jsonMessage;

			jsonMessage = (JSONObject) parser.parse(message);

			if ("add".equals(jsonMessage.get("action"))) {
				Device device = new Device();
				device.setName(jsonMessage.get("name").toString());
				device.setDescription(jsonMessage.get("description").toString());
				device.setType(jsonMessage.get("type").toString());
				device.setStatus("Off");
				sessionHandler.addDevice(device);
			}

			if ("remove".equals(jsonMessage.get("action").toString())) {
				int id = (int) jsonMessage.get("id");
				sessionHandler.removeDevice(id);
			}

			if ("toggle".equals(jsonMessage.get("action"))) {
				int id = new Integer(jsonMessage.get("id").toString());
				sessionHandler.toggleDevice(id);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
