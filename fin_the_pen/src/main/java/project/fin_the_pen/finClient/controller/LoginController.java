package project.fin_the_pen.finClient.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.finClient.data.member.User;
import project.fin_the_pen.finClient.data.member.UserRequestDTO;
import project.fin_the_pen.finClient.data.member.UserResponseDTO;
import project.fin_the_pen.finClient.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;
@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private UserResponseDTO currentUser;

    /*@GetMapping("init")
    public boolean init() {
        loginService.init();
        return true;
    }*/

    @PostMapping("join")
    public UserRequestDTO join(@RequestBody UserRequestDTO userRequestDTO) {
        userRequestDTO.setRegisterDate(Calendar.getInstance().getTime());
        loginService.joinUser(userRequestDTO);
        log.info("user: " + userRequestDTO.getName() + " 등록");
        return userRequestDTO;
    }

    /**
     * 회원 가입
     * @param userRequestDTO
     * @return
     */
    @PostMapping("/fin-the-pen-web/sign-up")
    public boolean signIn(@RequestBody UserRequestDTO userRequestDTO) {
        userRequestDTO.setRegisterDate(Calendar.getInstance().getTime());
        // 이 경우 아이디 비밀번호가 중복되므로 회원가입창으로 리다이렉트
        if (!loginService.joinUser(userRequestDTO)) {
            log.info("회원가입 - 중복된 아이디, 패드워드 존재");
            return false;
        }
        log.info("회원가입 - user: " + userRequestDTO.getName() + " 등록, 로그인 성공");
        return true;
    }


    @GetMapping("findUser")
    @ResponseBody
    public User findUser() {
        Optional<User> optionalUser = loginService.TempFindUser();
        return optionalUser.get();
    }

    /*@GetMapping("login")
    @ResponseBody
    public UserResponseDTO login(@RequestParam String userId, @RequestParam String password, HttpServletRequest request) {
        currentUser = loginService.findByUser(userId, password);
        grantSession(request);
        return currentUser;
    }*/

    /**
     * 로그인
     * @param userRequestDTO
     * @param request
     * @return
     * @throws IOException
     */
//    @PostMapping("/fin-the-pen-web/sign-in")
    /*@ResponseBody
    public UserResponseDTO apiLogin(@RequestBody UserRequestDTO userRequestDTO, HttpServletRequest request) throws IOException {
        try {
            currentUser = loginService.findByUser(userRequestDTO.getUser_id(), userRequestDTO.getPassword());
            grantSession(request);
        } catch (NullPointerException e) {
            log.info("로그인 - 존재하지 않는 사용자 입니다.");
            return null;
        }
        return currentUser;
    }*/

    @PostMapping("/fin-the-pen-web/sign-in")
    @ResponseBody
    public UserResponseDTO apiLogin(@RequestBody UserRequestDTO userRequestDTO, HttpServletRequest request) throws IOException {
        try {
            currentUser = loginService.findByUser(userRequestDTO.getUser_id(), userRequestDTO.getPassword());
            grantSession(request);
        } catch (NullPointerException e) {
            log.info("로그인 - 존재하지 않는 사용자 입니다.");
            return null;
        }
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
        log.info((String) session.getAttribute("session"));
    }
}
