package me.park.javawebsocketchat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class ChatController {

    @RequestMapping(value = "/chat.do", method = RequestMethod.GET)
    public String chat(HttpServletRequest request, HttpServletResponse response, Model model) {

        HttpSession session = request.getSession();
        if(session.getAttribute("nickname") == null || session.getAttribute("nickname").equals("")) {
            model.addAttribute("message", new Message("정상적인 경로로 접근해주세요.", "/logout.do"));
            return "message";
        }

        return "chat";
    }

}
