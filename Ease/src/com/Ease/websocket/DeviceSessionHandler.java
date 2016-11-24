package com.Ease.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import org.json.simple.JSONObject;

import com.Ease.model.Device;

@ApplicationScoped
public class DeviceSessionHandler {
	public static DeviceSessionHandler uniqueInstance = new DeviceSessionHandler();
	private int deviceId = 0;
	private final Set<Session> sessions = new HashSet<>();
    private final Set<Device> devices = new HashSet<>();
    
    public static DeviceSessionHandler getInstance() {
    	if (uniqueInstance == null)
    		uniqueInstance = new DeviceSessionHandler();
    	return uniqueInstance;
    }
    
    public void addSession(Session session) {
    	sessions.add(session);
    	for(Session sessionTest : sessions)
    		System.out.println("fuck you");
        for (Device device : devices) {
            JSONObject addMessage = createAddMessage(device);
            sendToSession(session, addMessage);
        }
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    public List<Device> getDevices() {
        return new ArrayList<>(devices);
    }

    public void addDevice(Device device) {
    	device.setId(deviceId);
        devices.add(device);
        deviceId++;
        JSONObject addMessage = createAddMessage(device);
        sendToAllConnectedSessions(addMessage);
    }

    public void removeDevice(int id) {
    	Device device = getDeviceById(id);
        if (device != null) {
            devices.remove(device);
            JSONObject removeMessage = new JSONObject();
            removeMessage.put("action", "remove");
            removeMessage.put("id", id);
            sendToAllConnectedSessions(removeMessage);
        }
    }

    public void toggleDevice(int id) {
    	JSONObject updateDevMessage = new JSONObject();
        Device device = getDeviceById(id);
        if (device != null) {
            if ("On".equals(device.getStatus())) {
                device.setStatus("Off");
            } else {
                device.setStatus("On");
            }
            updateDevMessage.put("action", "toggle");
            updateDevMessage.put("id", device.getId());
            updateDevMessage.put("status", device.getStatus());
            sendToAllConnectedSessions(updateDevMessage);
        }
    }

    private Device getDeviceById(int id) {
    	for (Device device : devices) {
            if (device.getId() == id) {
                return device;
            }
        }
        return null;
    }

    public JSONObject createAddMessage(Device device) {
        JSONObject addMessage = new JSONObject();
        addMessage.put("action", "add");
        addMessage.put("id", device.getId());
        addMessage.put("name", device.getName());
        addMessage.put("type", device.getType());
        addMessage.put("status", device.getStatus());
        addMessage.put("description", device.getDescription());
        return addMessage;
    }

    private void sendToAllConnectedSessions(JSONObject message) {
    	for (Session session : sessions) {
            sendToSession(session, message);
        }
    }

    private void sendToSession(Session session, JSONObject message) {
    	try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
        	ex.printStackTrace();
            sessions.remove(session);
            Logger.getLogger(DeviceSessionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
