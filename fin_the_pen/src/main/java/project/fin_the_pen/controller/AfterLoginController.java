package project.fin_the_pen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AfterLoginController {
    private Object currentSession;
    @GetMapping("afterHome")
    public String loginHome(HttpServletRequest request) {
        HttpSession session = request.getSession();
        currentSession = session.getAttribute("session");
        return "redirect:/";
    }
}
