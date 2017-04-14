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
import com.Servlet.Exception.ServletErrorException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint(value="/extensionEndpoint", configurator = ExtensionSocketConfigurator.class)
public class ExtensionManager {

    @Resource

    private HttpSession httpSession;

    static Map<String, Extension> extensions = Collections.synchronizedMap(new HashMap<String, Extension>());
    static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        httpSession = (HttpSession) config.getUserProperties().get("httpSession");
        System.out.println("open");
        sessions.add(session);
        session.getUserProperties().put("singleId", UUID.randomUUID().toString());
        JSONObject data = new JSONObject();
        data.put("singleId", session.getUserProperties().get("singleId"));
        //data.put("timeout", session.getMaxIdleTimeout());
        try {
            if (session.isOpen()) {
                session.getBasicRemote().sendText(this.createMessage("identity", "welcome", data));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("closeEvent");
        close(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("msg");
        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(message);
            String context = (String) obj.get("context");
            System.out.println(context);
            switch (context) {
                case "identity": {
                    this.msgHandler((String) obj.get("order"), obj.get("data"), session);
                }
                break;
                case "updater": {
                    Extension extension = extensions.get(session.getUserProperties().get("singleId"));
                    if (extension != null) {
                        this.updateHandler((String) obj.get("order"), obj.get("data"), extension);
                    } else {
                        session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Client not identified."));
                    }
                }
                break;
                case "action" : {
                    Extension extension = extensions.get(session.getUserProperties().get("singleId"));
                    extension.actionHandler(obj);
                }
                break;
                default: {
                    session.close(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, "Wrong context."));
                }
            }
        } catch (ParseException | ExtensionMessageNotSendedException | ExtensionClosedSocketException | IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.out.println("error");
        t.printStackTrace();
    }

    private String createMessage(String context, String order, JSONObject data) {
        JSONObject obj = new JSONObject();
        obj.put("context", context);
        obj.put("order", order);
        obj.put("data", data);
        return obj.toString();
    }

    private static void close(Session session) {
        sessions.remove(session);
        Extension extension = extensions.get(session.getUserProperties().get("singleId"));
        if (extension != null) {
            if (extension.getUser() == null) {
                System.out.println("Remove Extension: " + session.getUserProperties().get("singleId"));
                extensions.remove(session.getUserProperties().get("singleId"));
            }
        }
    }

    private void msgHandler(String order, Object data, Session session) throws ExtensionClosedSocketException, ExtensionMessageNotSendedException{

        ExtensionFolderManager folders = (ExtensionFolderManager) httpSession.getServletContext().getAttribute("extensionFolderManager");
        switch (order) {
            case "lastSingleId" :{
                String lastSingleId = (String) ((JSONObject)data).get("lastSingleId");
                Extension extension = extensions.get(lastSingleId);
                if (extension != null) {
                    extensions.remove(extension.getSingleId());
                    extension.setSession(session);
                    extensions.put((String) session.getUserProperties().get("singleId"), extension);
                } else {
                    extension = new Extension(session, httpSession.getServletContext());
                    System.out.println("Add extension: " + session.getUserProperties().get("singleId"));
                    extensions.put((String) session.getUserProperties().get("singleId"), extension);
                }
                folders.sendUpdateVersion(extension);
            }
            break;
        }
    }

    private void updateHandler(String order, Object data, Extension extension) throws ExtensionClosedSocketException, ExtensionMessageNotSendedException {
        ExtensionFolderManager folders = (ExtensionFolderManager) httpSession.getServletContext().getAttribute("extensionFolderManager");
        switch (order) {
            case "getSources" :{
                JSONObject sources;
                if (extension.getUser() != null) {
                    ExtensionFolder folder = folders.getFolder(extension.getUser().getExtensionVersion());
                    if (folder != null) {
                        sources = folder.getJson();
                    } else {
                        sources = folders.getDefaultFolder().getJson();
                    }
                } else {
                    sources = folders.getDefaultFolder().getJson();
                }
                extension.sendMessage("updater", "sources", sources);
            }
            break;
        }
    }

    public static void refresh(ExtensionFolderManager folders) throws ServletErrorException {
        folders.refresh();
        for (Extension extension : extensions.values()) {
            try {
                System.out.println(extension.getSingleId());
                folders.sendUpdateVersion(extension);
            } catch (ExtensionClosedSocketException e) {
                e.printStackTrace();
                close(extension.getSession());
            } catch (ExtensionMessageNotSendedException e) {
                e.printStackTrace();
                try {
                    extension.close();
                } catch (ExtensionClosedSocketException e1) {
                    e1.printStackTrace();
                    close(extension.getSession());
                }
            }
        }
    }
}
