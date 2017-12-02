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
        this.session.getAsyncRemote().sendObject(o);
    }

    public Session getSession() {
        return this.session;
    }
}