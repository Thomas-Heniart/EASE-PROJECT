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
        removeClosedSessions();
        for (WebSocketSession ws : webSocketSessions) {
            ws.sendMessage(message);
        }
    }

    public void sendObject(Object o) {
        removeClosedSessions();
        for (WebSocketSession ws : webSocketSessions) {
            System.out.println("Send object");
            ws.sendObject(o);
        }
    }

    public void sendObjects(List<WebSocketMessage> objects) {
        removeClosedSessions();
        for (WebSocketSession ws : webSocketSessions) {
            for (Object object : objects)
                ws.sendObject(object);
        }
    }

    public void sendObjects(List<WebSocketMessage> objects, String wsId) {
        try {
            System.out.println("WSM size before closing sessions: " + this.webSocketSessions.size());
            removeClosedSessions();
            System.out.println("WSM after closing sessions: " + this.webSocketSessions.size());
            System.out.println("Number of messages: " + objects.size());
            for (WebSocketSession ws : webSocketSessions) {
                if (wsId != null && ws.getSession().getId().equals(wsId))
                    continue;
                System.out.println("Start to send objects from: " + wsId + " to: " + ws.getSession().getId() + " nb of objects: " + objects.size());
                for (Object object : objects) {
                    ws.sendObject(object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendObjects(String wsId) {
        removeClosedSessions();
        this.webSocketSessions.stream().filter(webSocketSession -> webSocketSession.getSession().getId().equals(wsId)).forEach(webSocketSession -> this.webSocketMessageList.forEach(webSocketSession::sendObject));
        this.webSocketMessageList.clear();
    }

    public List<WebSocketSession> getWebSocketSessions() {
        return this.webSocketSessions;
    }

    public void addWebSocketSession(WebSocketSession wss) {
        removeClosedSessions();//to delete after start using sendMessage()
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

    private void removeWebSocketSession(WebSocketSession wss) {
        this.webSocketSessions.remove(wss);
    }

    private void removeClosedSessions() {
        List<WebSocketSession> webSocketSessionsToRemove = new LinkedList<>();
        for (WebSocketSession ws : this.webSocketSessions) {
            if (!ws.getSession().isOpen())
                webSocketSessionsToRemove.add(ws);
        }
        for (WebSocketSession ws : webSocketSessionsToRemove)
            this.removeWebSocketSession(ws);
    }
}
