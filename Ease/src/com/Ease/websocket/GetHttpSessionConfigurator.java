package com.Ease.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {

	@Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
		super.modifyHandshake(config, request, response);
		HttpSession httpSession =(HttpSession) request.getHttpSession();
        if (httpSession != null)
        	config.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
	
}
