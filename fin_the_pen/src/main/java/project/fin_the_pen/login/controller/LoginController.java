package project.fin_the_pen.login.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.data.member.User;
import project.fin_the_pen.data.member.UserDTO;
import project.fin_the_pen.login.service.LoginService;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("login")
    public UserDTO login(@RequestParam("name") String name) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(name);
        return userDTO;
    }

    @PostMapping("join")
    public UserDTO join(@RequestBody UserDTO userDTO) {
        loginService.joinUser(userDTO);
        log.info("user: " + userDTO.getUserName() + " 등록");
        return userDTO;
    }

    @GetMapping("findUser")
    @ResponseBody
    public User findUser() {
        Optional<User> optionalUser = loginService.findUser();
        User user = optionalUser.get();

        return user;
    }
}
