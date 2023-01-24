package project.fin_the_pen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.data.member.User;
import project.fin_the_pen.data.member.UserRequestDTO;
import project.fin_the_pen.data.member.UserResponseDTO;
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
    private UserResponseDTO currentUser;

    @PostMapping("join")
    public UserRequestDTO join(@RequestBody UserRequestDTO userRequestDTO) {
        userRequestDTO.setRegisterDate(Calendar.getInstance().getTime());
        loginService.joinUser(userRequestDTO);
        log.info("user: " + userRequestDTO.getName() + " 등록");
        return userRequestDTO;
    }

    //TODO API 로그인 연동
    @PostMapping("/fin-the-pen-web/sign-up")
    public boolean signIn(@RequestBody UserRequestDTO userRequestDTO) {
        userRequestDTO.setRegisterDate(Calendar.getInstance().getTime());
        loginService.joinUser(userRequestDTO);
        log.info("user: " + userRequestDTO.getName() + " 등록");
        return true;
    }


    @GetMapping("findUser")
    @ResponseBody
    public User findUser() {
        Optional<User> optionalUser = loginService.TempFindUser();
        return optionalUser.get();
    }

    @GetMapping("login")
    @ResponseBody
    public UserResponseDTO login(@RequestParam String userId, @RequestParam String password, HttpServletRequest request) {
        currentUser = loginService.findByUser(userId, password);
        grantSession(request);
        return currentUser;
    }

    //TODO API 로그인
    @PostMapping("/fin-the-pen-web/sign-in")
    @ResponseBody
    public UserResponseDTO apiLogin(@RequestBody UserRequestDTO userRequestDTO, HttpServletRequest request) {
        currentUser = loginService.findByUser(userRequestDTO.getUser_id(), userRequestDTO.getPassword());
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
        session.setAttribute("session", currentUser.getUser_id());
    }
}
