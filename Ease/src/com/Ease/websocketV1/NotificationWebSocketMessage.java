package com.Ease.websocketV1;

import com.Ease.Notification.Notification;

public class NotificationWebSocketMessage extends WebSocketMessage {
    public NotificationWebSocketMessage(Notification notification) {
        super("NEW_NOTIFICATION", notification.getJson());
    }
}