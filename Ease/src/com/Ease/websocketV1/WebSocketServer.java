package com.Ease.websocketV1;

import com.Ease.Dashboard.User.User;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

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
        User user = (User) httpSession.getAttribute("user");

        System.out.println("connection opened by websocket. id = " + session.getId());
        System.out.println("is secure ? " + session.isSecure());
        if (!session.isSecure()) {
            session.close();
            System.out.println("webSocketSession closed");
            return;
        }
        if (user != null) {
            WebSocketSession webSocketSession = new WebSocketSession(session);
            user.getWebSocketManager().addWebSocketSession(webSocketSession);
            for (TeamUser teamUser : user.getTeamUsers()) {
                Team team = teamUser.getTeam();
                team.getWebSocketManager().addWebSocketSession(webSocketSession);
                for (Channel channel : team.getChannelsForTeamUser(teamUser))
                    channel.getWebSocketManager().addWebSocketSession(webSocketSession);
            }
            System.out.println("webSocketSession registered for user : " + user.getEmail());
            webSocketSession.sendObject(new WebSocketMessage("CONNECTION_ID", session.getId()).toJSONObject());
        }
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
