package com.Ease.websocketV1;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/webSocketServer",
        configurator = GetHttpSessionConfigurator.class,
        encoders = {WebSocketMessageEncoder.class},
        decoders = {WebSocketMessageDecoder.class})
public class WebSocketServer {

    @OnMessage
    public void onMessage(WebSocketMessage message, Session session) {
        System.out.println("new message");
        System.out.println(message.getType());
        System.out.println(message.getData());
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) throws IOException, EncodeException {
        HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
        Integer user_id = (Integer) httpSession.getAttribute("user_id");
        ServletContext servletContext = httpSession.getServletContext();
        System.out.println("connection opened by websocket. id = " + session.getId());
        System.out.println("is secure ? " + session.isSecure());
        if (!session.isSecure()) {
            session.close();
            System.out.println("webSocketSession closed");
            return;
        }
        session.getBasicRemote().sendText(new WebSocketMessage("CONNECTION_ID", session.getId()).toJSONObject().toString());
        if (user_id == null)
            return;
        HibernateQuery hibernateQuery = new HibernateQuery();
        hibernateQuery.get(User.class, user_id);
        User user = (User) hibernateQuery.getSingleResult();
        if (user == null)
            return;
        WebSocketSession webSocketSession = new WebSocketSession(session);
        Map<Integer, Map<String, Object>> userIdMap = (Map<Integer, Map<String, Object>>) servletContext.getAttribute("userIdMap");
        Map<String, Object> userProperties = userIdMap.get(user_id);
        if (userProperties == null) {
            userProperties = new ConcurrentHashMap<>();
            userIdMap.put(user_id, userProperties);
        }
        WebSocketManager webSocketManager = (WebSocketManager) userProperties.get("webSocketManager");
        if (webSocketManager == null) {
            webSocketManager = new WebSocketManager();
            userProperties.put("webSocketManager", webSocketManager);
        }
        webSocketManager.addWebSocketSession(webSocketSession);
        for (TeamUser teamUser : user.getTeamUsers()) {
            Team team = teamUser.getTeam();
            Map<Integer, Map<String, Object>> teamIdMap = (Map<Integer, Map<String, Object>>) servletContext.getAttribute("teamIdMap");
            Map<String, Object> teamProperties = teamIdMap.get(team.getDb_id());
            if (teamProperties == null) {
                teamProperties = new ConcurrentHashMap<>();
                teamIdMap.put(team.getDb_id(), teamProperties);
            }
            WebSocketManager teamWebSocketManager = (WebSocketManager) teamProperties.get("webSocketManager");
            if (teamWebSocketManager == null) {
                teamWebSocketManager = new WebSocketManager();
                teamProperties.put("webSocketManager", teamWebSocketManager);
            }
            teamWebSocketManager.addWebSocketSession(webSocketSession);
        }
        System.out.println("webSocketSession registered for user : " + user.getEmail());
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        System.out.println("connection closed by websocket. id = " + session.getId());
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("WebSocketError : " + error.getMessage());
    }
}
