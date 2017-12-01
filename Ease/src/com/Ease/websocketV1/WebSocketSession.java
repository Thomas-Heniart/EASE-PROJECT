package com.Ease.websocketV1;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

public class WebSocketSession {

    protected Session session;

    public WebSocketSession(Session session) {
        this.session = session;
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    public void sendObject(Object o) {
        try {
            synchronized (session) {
                this.session.getBasicRemote().sendObject(o);
            }
        } catch (IOException | EncodeException e) {
            e.printStackTrace();
        }
        //this.session.getAsyncRemote().sendObject(o);
    }

    public Session getSession() {
        return this.session;
    }
}