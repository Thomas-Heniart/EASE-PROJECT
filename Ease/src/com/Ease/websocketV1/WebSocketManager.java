package com.Ease.websocketV1;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WebSocketManager {
    private List<WebSocketSession> webSocketSessions = new LinkedList<>();
    private List<WebSocketMessage> webSocketMessageList = new LinkedList<>();

    public WebSocketManager() {
    }

    public void sendMessage(String message) throws IOException {
        removeClosedSesions();
        for (WebSocketSession ws : webSocketSessions) {
            ws.sendMessage(message);
        }
    }

    public void sendObject(Object o) {
        removeClosedSesions();
        for (WebSocketSession ws : webSocketSessions) {
            ws.sendObject(o);
        }
    }

    public void sendObjects(List<WebSocketMessage> objects) {
        removeClosedSesions();
        for (WebSocketSession ws : webSocketSessions) {
            for (Object object : objects)
                ws.sendObject(object);
        }
    }

    public void sendObjects(List<WebSocketMessage> objects, String ws_id) {
        removeClosedSesions();
        for (WebSocketSession ws : webSocketSessions) {
            if (ws.getSession().getId().equals(ws_id))
                continue;
            for (Object object : objects)
                ws.sendObject(object);
        }
    }

    public void sendObjects(String ws_id) {
        removeClosedSesions();
        this.webSocketSessions.stream().filter(webSocketSession -> webSocketSession.getSession().getId().equals(ws_id)).forEach(webSocketSession -> this.webSocketMessageList.forEach(webSocketSession::sendObject));
        this.webSocketMessageList.clear();
    }

    public List<WebSocketSession> getWebSocketSessions() {
        return this.webSocketSessions;
    }

    public void addWebSocketSession(WebSocketSession wss) {
        removeClosedSesions();//to delete after start using sendMessage()
        this.webSocketSessions.add(wss);
    }

    public void addWebSocketMessage(WebSocketMessage webSocketMessage) {
        this.webSocketMessageList.add(webSocketMessage);
    }

    public void invalidateAllSessions() {
        for (WebSocketSession ws : this.webSocketSessions) {
            try {
                ws.getSession().close();
            } catch (IOException e) {
                System.out.println("Error during webSocket closing.");
            }
        }
    }

    public void removeWebSocketSession(WebSocketSession wss) {
        this.webSocketSessions.remove(wss);
    }

    private void removeClosedSesions() {
        List<WebSocketSession> webSocketSessions_to_remove = new LinkedList<>();
        for (WebSocketSession ws : this.webSocketSessions) {
            if (!ws.getSession().isOpen())
                webSocketSessions_to_remove.add(ws);
        }
        for (WebSocketSession ws : webSocketSessions_to_remove)
            this.removeWebSocketSession(ws);
    }
}
