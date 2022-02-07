package me.park.javawebsocketchat;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

@Slf4j
public class HttpSessionConfigurator extends Configurator {
    public static final String Session = "Session";
    public static final String Context = "Context";

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        log.info("Run modifyHandshake");

        HttpSession session = (HttpSession) request.getHttpSession();
        ServletContext context = session.getServletContext();
        config.getUserProperties().put(HttpSessionConfigurator.Session, session);
        config.getUserProperties().put(HttpSessionConfigurator.Context, context);
    }
}