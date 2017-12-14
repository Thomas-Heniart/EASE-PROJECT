package com.Ease.websocketV1;

import javax.websocket.Session;
import java.io.IOException;

public class WebSocketSession {

    protected Session session;

    public WebSocketSession(Session session) {
        this.session = session;
    }

    public void sendMessage(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message);
    }

    public void sendObject(Object o) {
        System.out.println("Send websocket message " + o.toString());
        try {
            this.session.getAsyncRemote().sendObject(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return this.session;
    }
}