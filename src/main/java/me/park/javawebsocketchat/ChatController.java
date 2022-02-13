package me.park.javawebsocketchat;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ChatController {

    private final ChatRepository chatRepository;
    private final Gson gson = new Gson();

    public ChatController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @RequestMapping(value = "/chat.do", method = RequestMethod.GET)
    public String chat(HttpServletRequest request, HttpServletResponse response, Model model) {

        HttpSession session = request.getSession();
        if(session.getAttribute("nickname") == null || session.getAttribute("nickname").equals("")) {
            model.addAttribute("message", new Message("정상적인 경로로 접근해주세요.", "/logout.do"));
            return "message";
        }

        //5분 전 대화까지 검색
        LocalDateTime startDate = LocalDateTime.now().minusMinutes(5);
        LocalDateTime endDate = LocalDateTime.now();
        List<ChatEntity> chatList = chatRepository.findByChatRegdateBetweenOrderByChatRegdateAsc(startDate, endDate);

        model.addAttribute("chatList", gson.toJson(chatList));

        return "chat";
    }

}
