package project.fin_the_pen.finClient.api.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.finClient.core.util.ApiResponse;
import project.fin_the_pen.model.user.dto.SignInRequest;
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.dto.UserResponseDTO;
import project.fin_the_pen.model.user.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "API 테스트")
public class LoginController {
    private final LoginService loginService;
    private UserResponseDTO currentUser;

    @PostMapping("/join")
    public UserRequestDTO join(@RequestBody UserRequestDTO userRequestDTO) {
        userRequestDTO.setRegisterDate(LocalDate.now());
        loginService.signUp(userRequestDTO);
        log.info("user: " + userRequestDTO.getName() + " 등록");
        return userRequestDTO;
    }

    /**
     * 회원 가입
     * @param userRequestDTO
     * @return
     */
    @PostMapping("/fin-the-pen-web/sign-up")
    @Operation(summary = "회원 가입")
    public ApiResponse signUp(@RequestBody UserRequestDTO userRequestDTO) {
        return ApiResponse.success(loginService.signUp(userRequestDTO));
    }

    /**
     * 로그인
     * @return
     * @throws IOException
     */
    @PostMapping("/fin-the-pen-web/sign-in")
    @Operation(summary = "로그인")
    public ApiResponse signIn(@RequestBody SignInRequest request) {
        return ApiResponse.success(loginService.signIn(request));
    }


    /*@GetMapping("/findUser")
    public Users findUser() {
        Optional<Users> optionalUser = loginService.TempFindUser();
        return optionalUser.get();
    }*/

    /*@GetMapping("login")
    @ResponseBody
    public UserResponseDTO login(@RequestParam String userId, @RequestParam String password, HttpServletRequest request) {
        currentUser = loginService.findByUser(userId, password);
        grantSession(request);
        return currentUser;
    }*/


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



    /*@PostMapping("/fin-the-pen-web/modify")
    public boolean modify(@RequestBody UserRequestDTO userRequestDTO) throws IOException {
        return loginService.update(userRequestDTO);
    }*/


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


    // TODO 1
    @PostMapping("/app/password")
    public boolean appPasswordSignUp(@RequestBody ConcurrentHashMap<String, String> map) {
        boolean isPassword = loginService.saveAppPassword(map.get("password"));

        if (isPassword) {
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("/app/password/login")
    public boolean appPasswordLogin(@RequestBody ConcurrentHashMap<String, String> map) {
        boolean isPassword = loginService.appPasswordLogin(map.get("password"));

        if (isPassword) {
            return true;
        } else {
            return false;
        }
    }


    private void grantSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("session", currentUser.getUserId());
        log.info((String) session.getAttribute("session"));
    }
}
