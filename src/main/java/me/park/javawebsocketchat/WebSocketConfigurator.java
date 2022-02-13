package me.park.javawebsocketchat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

@Slf4j
@Configuration
public class WebSocketConfigurator extends Configurator implements ApplicationContextAware {
    public static final String Session = "Session";
    public static final String Context = "Context";
    private static volatile BeanFactory applicationContext;

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        log.info("Run modifyHandshake");

        HttpSession session = (HttpSession) request.getHttpSession();
        ServletContext context = session.getServletContext();
        config.getUserProperties().put(WebSocketConfigurator.Session, session);
        config.getUserProperties().put(WebSocketConfigurator.Context, context);
    }

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebSocketConfigurator.applicationContext = applicationContext;
    }
}