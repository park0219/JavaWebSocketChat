package me.park.javawebsocketchat;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
@ServerEndpoint(value = "/echo.do", configurator = HttpSessionConfigurator.class)
public class WebSocketChat {

    private static final Map<Session, EndpointConfig> sessionConfigMap = Collections.synchronizedMap(new HashMap<>());
    private final Gson gson = new Gson();
    private final Map<String, Object> returnMap = new HashMap<>();

    public WebSocketChat() {
        log.info("웹소켓 객체 생성");
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {

        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSessionConfigurator.Session);

        log.info("WebSocketChat.onOpen: user enter => " + session.getId() + "(" + httpSession.getAttribute("nickname") + ")");
        try {
            RemoteEndpoint.Basic basic = session.getBasicRemote();
            returnMap.clear();
            returnMap.put("message", "채팅방에 연결되었습니다.");
            returnMap.put("returnCode", "3");
            basic.sendText(gson.toJson(returnMap));

            returnMap.put("message", httpSession.getAttribute("nickname") + "님이 들어왔습니다");
            sendAllSessionMessage(session, returnMap);
        }
        catch(Exception e) {
            log.info("WebSocketChat.onOpen ERROR: " + e.getMessage());
        }

        if(!sessionConfigMap.containsKey(session)) {
            sessionConfigMap.put(session, config);
        }
    }

    @OnMessage
    @SuppressWarnings("unchecked")
    public void onMessage(String message, Session session) {

        EndpointConfig config = sessionConfigMap.get(session);
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSessionConfigurator.Session);

        Map<String, String> jsonMap = gson.fromJson(message, Map.class);

        log.info("Message From \"" + httpSession.getAttribute("nickname") + "\": " + jsonMap.get("message"));

        returnMap.clear();
        LocalDateTime now = LocalDateTime.now();
        //1은 본인이 보낸 메시지
        returnMap.put("returnCode", "1");
        returnMap.put("sender", httpSession.getAttribute("nickname"));
        returnMap.put("message", jsonMap.get("message"));
        returnMap.put("time", now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")));
        returnMap.put("timeFormatted", now.format(DateTimeFormatter.ofPattern("HH:mm")));

        try {
            RemoteEndpoint.Basic basic = session.getBasicRemote();
            basic.sendText(gson.toJson(returnMap));
        }
        catch(Exception e) {
            log.info("WebSocketChat.onMessage ERROR: " + e.getMessage());
        }
        returnMap.put("returnCode", "2");
        sendAllSessionMessage(session, returnMap);
    }

    @OnError
    public void onError(Throwable e, Session session) {
        log.info("WebSocketChat.onError ERROR: " + session.getId() + " " + e.getMessage());
    }

    @OnClose
    public void onClose(Session session) {

        EndpointConfig config = sessionConfigMap.get(session);
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSessionConfigurator.Session);

        if(httpSession != null) {
            log.info("Session " + session.getId() + "(" + httpSession.getAttribute("nickname") + ")" + " has closed");
            returnMap.put("message", httpSession.getAttribute("nickname") + "님이 나갔습니다");
            returnMap.put("returnCode", "3");
            sendAllSessionMessage(session, returnMap);
            httpSession.removeAttribute("nickname");
        }
        else {
            log.info("Session " + session.getId() + " has closed");
        }
        sessionConfigMap.remove(session);

    }

    private void sendAllSessionMessage(Session self, Map<String, Object> returnMap) {

        WebSocketChat.sessionConfigMap.forEach((key, value) -> {
            if(!self.getId().equals(key.getId())) {
                try {
                    key.getBasicRemote().sendText(gson.toJson(returnMap));
                }
                catch(IOException e) {
                    log.info("WebSocketChat.sendAllSessionMessage ERROR: " + e.getMessage());
                }
            }
        });

    }
}
