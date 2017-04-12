package com.Ease.websocketV1;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WebSocketManager {
    private List<WebSocketSession> webSocketSessions;

    public WebSocketManager(){
        this.webSocketSessions = new LinkedList<>();
    }

    public void sendMessage(String message) {
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

    public List<WebSocketSession> getWebSocketSessions() {
        return this.webSocketSessions;
    }

    public void addWebSocketSession(WebSocketSession wss){
        removeClosedSesions();//to delete after start using sendMessage()
        this.webSocketSessions.add(wss);
    }

    public void invalidateAllSessions(){
        for (WebSocketSession ws : this.webSocketSessions){
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

    private void removeClosedSesions(){
        for (WebSocketSession ws : this.webSocketSessions){
            if (!ws.getSession().isOpen())
                this.webSocketSessions.remove(ws);
        }
    }
}
