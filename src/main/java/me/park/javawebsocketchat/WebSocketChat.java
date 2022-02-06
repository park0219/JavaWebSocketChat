package me.park.javawebsocketchat;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@ServerEndpoint(value = "/echo.do")
public class WebSocketChat {

    private static final List<Session> sessionList = new ArrayList<>();
    private final Gson gson = new Gson();
    private final Map<String, Object> returnMap = new HashMap<>();

    public WebSocketChat() {
        log.info("웹소켓 객체 생성");
    }

    @OnOpen
    public void onOpen(Session session) {

        log.info("WebSocketChat.onOpen: user enter => " + session.getId());
        try {
            RemoteEndpoint.Basic basic = session.getBasicRemote();
            returnMap.clear();
            returnMap.put("message", "채팅방에 연결되었습니다.");
            returnMap.put("returnCode", "3");
            basic.sendText(gson.toJson(returnMap));
        }
        catch(Exception e) {
            log.info("WebSocketChat.onOpen ERROR: " + e.getMessage());
        }
        sessionList.add(session);
    }

    @OnMessage
    @SuppressWarnings("unchecked")
    public void onMessage(String message, Session session) {

        Map<String, String> jsonMap = gson.fromJson(message, Map.class);

        log.info("Message From \"" + jsonMap.get("nickname") + "\": " + jsonMap.get("message"));

        returnMap.clear();
        LocalDateTime now = LocalDateTime.now();
        //1은 본인이 보낸 메시지
        returnMap.put("returnCode", "1");
        returnMap.put("sender", jsonMap.get("nickname"));
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
        sendAllSessionMessage(session, returnMap);
    }

    @OnError
    public void onError(Throwable e, Session session) {
        log.info("WebSocketChat.onError ERROR: " + session.getId() + " " + e.getMessage());
    }

    @OnClose
    public void onClose(Session session) {
        log.info("Session " + session.getId() + " has closed");
        sessionList.remove(session);
    }

    private void sendAllSessionMessage(Session self, Map<String, Object> returnMap) {

        //2는 다른사람이 보낸 메시지
        returnMap.put("returnCode", "2");

        try {
            for(Session session : WebSocketChat.sessionList) {
                if(!self.getId().equals(session.getId())) {
                    session.getBasicRemote().sendText(gson.toJson(returnMap));
                }
            }
        }
        catch(Exception e) {
            log.info("WebSocketChat.sendAllSessionMessage ERROR: " + e.getMessage());
        }
    }
}
