package com.Ease.websocketV1;

import com.Ease.User.Notification;
import org.json.simple.JSONObject;

public class WebSocketMessageFactory {
    public static WebSocketMessage createWebSocketMessage(WebSocketMessageType dataType, WebSocketMessageAction actionType, JSONObject data, JSONObject target) {
        return new TeamWebSocketMessage(dataType.name(), actionType.name(), data, target);
    }

    public static WebSocketMessage createWebSocketMessage(WebSocketMessageType dataType, WebSocketMessageAction actionType, Integer id, JSONObject target) {
        return new TeamWebSocketMessage(dataType.name(), actionType.name(), id, target);
    }

    public static WebSocketMessage createNotificationMessage(Notification notification) {
        return new NotificationWebSocketMessage(notification);
    }
}
