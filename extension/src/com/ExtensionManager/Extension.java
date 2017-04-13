package com.ExtensionManager;
/*
*   Class-name: [The class name here]
*   Author: debruy_p
*   Description: [The description here]
*   
*   Documentation-link: [The link here]
*/

import com.ExtensionManager.Exception.ExtensionClosedSocketException;
import com.ExtensionManager.Exception.ExtensionMessageNotSendedException;
import com.User.User;
import com.WebsiteBuilder.WebsiteBuilderManager;
import com.WebsiteBuilder.WebsiteManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletContext;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Extension {
    protected Session socketSession;
    protected String last_singleId;
    protected User user;
    protected Map<String, String[]> msgCommings;
    protected ServletContext context;

    public Extension(Session socketSession, ServletContext context) {
        this.socketSession = socketSession;
        user = null;
        this.context = context;
        msgCommings = new HashMap<String, String[]>();
    }

    public String getSingleId() {
        return (String) socketSession.getUserProperties().get("singleId");
    }

    public void setSession(Session session) {
        this.socketSession = session;
    }

    public Session getSession() {
        return socketSession;
    }

    public void sendMessage(String context, String order, JSONObject data) throws ExtensionClosedSocketException, ExtensionMessageNotSendedException {
        try {
            if (socketSession != null) {
                JSONObject obj = new JSONObject();
                obj.put("context", context);
                obj.put("order", order);
                obj.put("data", data);

                if (socketSession.isOpen()) {
                    socketSession.getBasicRemote().sendText(obj.toString());
                    System.out.println("ICIICICI!!!");
                } else {
                    throw new ExtensionClosedSocketException("Extension socket is closed.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExtensionMessageNotSendedException("Extension socket is closed.", e);
        }
    }

    public void close() throws ExtensionClosedSocketException {
        try {
            if (socketSession != null) {
                socketSession.close();
                socketSession = null;
            } else {
                throw new ExtensionClosedSocketException("Extension socket is already closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExtensionClosedSocketException("Extension socket is already closed.", e);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void actionHandler(JSONObject obj) {
        String[] messages;
        int size = Integer.parseInt((String) obj.get("size"));
        String msgId = (String) obj.get("msgId");
        int index = Integer.parseInt((String) obj.get("index"));

        if ((messages = msgCommings.get(msgId)) == null) {
            System.out.println("First recu: " + index + "/" + size);
            if (size == 1) {
                messages = new String[1];
                messages[0] = (String) obj.get("data");
                System.out.println("fini");
                onMessage(messages);
            } else {
                messages = new String[size];
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = null;
                }
                messages[index] = (String) obj.get("data");
                msgCommings.put(msgId, messages);
            }
        } else {
            System.out.println("recu: " + index + "/" + size);
            messages[index] = (String) obj.get("data");
            boolean isFull = true;
            for (String message : messages) {
                if (message == null) {
                    isFull = false;
                }
            }
            if (isFull) {
                System.out.println("fini");
                onMessage(messages);
                msgCommings.remove(msgId);
            }
        }
    }

    private void onMessage(String[] messages) {

        JSONParser parser = new JSONParser();
        String msg = "";
        for (String message : messages) {
            msg += message;
        }
        System.out.println(msg);
        try {
            JSONObject msgJson = (JSONObject) parser.parse(msg);
            switch ((String)((JSONObject)msgJson.get("action")).get("actionName")) {
                case "addWebsite" : {
                    ((WebsiteBuilderManager)(context.getAttribute("websiteBuilderManager"))).msgHandler(this, (JSONObject)msgJson.get("action"), (String)msgJson.get("order"), (JSONObject)msgJson.get("data"));
                }
                break;
                case "background" : {
                    System.out.println(msg);
                    if (msgJson.get("order").equals("getConnectJSON")) {
                        sendResponse(((WebsiteManager)(context.getAttribute("websiteManager"))).msgHandler(this, (JSONObject)msgJson.get("action"), (String)msgJson.get("order"), (JSONObject)msgJson.get("data")));
                    }
                }
                break;
                default: {

                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(JSONObject resp) {
        try {
            if (socketSession != null) {

                if (socketSession.isOpen()) {
                    System.out.println(resp.toString());
                    socketSession.getBasicRemote().sendText(resp.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}