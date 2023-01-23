package project.fin_the_pen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.data.member.User;
import project.fin_the_pen.data.member.UserDAO;
import project.fin_the_pen.data.member.UserDTO;
import project.fin_the_pen.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private UserDTO currentUser;

    @PostMapping("join")
    public UserDAO join(@RequestBody UserDAO userDAO) {
        userDAO.setRegisterDate(Calendar.getInstance().getTime());
        loginService.joinUser(userDAO);
        log.info("user: " + userDAO.getUserName() + " 등록");
        return userDAO;
    }

    //TODO API 로그인 연동
    @PostMapping("/fin-the-pen-web/sign-up")
    public UserDAO signIn(@RequestBody UserDAO userDAO) {
        userDAO.setRegisterDate(Calendar.getInstance().getTime());
        loginService.joinUser(userDAO);
        log.info("user: " + userDAO.getUserName() + " 등록");
        return userDAO;
    }

    @GetMapping("findUser")
    @ResponseBody
    public User findUser() {
        Optional<User> optionalUser = loginService.TempFindUser();
        return optionalUser.get();
    }

    @GetMapping("login")
    @ResponseBody
    public UserDTO login(@RequestParam String userId, @RequestParam String password, HttpServletRequest request) {
        currentUser = loginService.findByUser(userId, password);
        grantSession(request);
        return currentUser;
    }

    //TODO API 로그인
    @PostMapping("/fin-the-pen-web/sign-in")
    @ResponseBody
    public UserDTO apiLogin(@RequestBody UserDAO userDAO,HttpServletRequest request) {
        currentUser = loginService.findByUser(userDAO.getUserId(), userDAO.getPassword());
        grantSession(request);
        return currentUser;
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    private void grantSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("session", currentUser.getUserId());
    }
}
