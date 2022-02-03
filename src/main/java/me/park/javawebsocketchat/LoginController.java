package me.park.javawebsocketchat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class LoginController {

    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "login";
    }

    @RequestMapping(value = "/loginProcess.do")
    public String loginProcess(@RequestParam String id, HttpServletRequest request, HttpServletResponse response) {

        log.info("Welcome " + id);

        HttpSession session = request.getSession();
        session.setAttribute("id", id);
        return "chat";
    }


}
