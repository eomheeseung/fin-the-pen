package project.fin_the_pen.finClient.api.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.model.user.dto.SignInRequest;
import project.fin_the_pen.model.user.dto.SignInResponse;
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
@Tag(name = "API 테스트 / 로그인")
public class LoginController {
    private final LoginService loginService;
    private UserResponseDTO currentUser;

    //    @PostMapping("/join")
    public UserRequestDTO join(@RequestBody UserRequestDTO userRequestDTO) {
        userRequestDTO.setRegisterDate(LocalDate.now());
        loginService.signUp(userRequestDTO);
        log.info("user: " + userRequestDTO.getName() + " 등록");
        return userRequestDTO;
    }

    /**
     * 회원 가입
     *
     * @param userRequestDTO
     * @return
     */
    @PostMapping(value = "/fin-the-pen-web/sign-up", produces = "application/json")
    @Operation(summary = "회원 가입")
    public ResponseEntity<Object> signUp(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = loginService.signUp(userRequestDTO);
        return ResponseEntity.ok().body(userResponseDTO);
//        return ApiResponse.success(loginService.signUp(userRequestDTO));
    }

    /**
     * 로그인
     *
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/fin-the-pen-web/sign-in", produces = "application/json")
    @Operation(summary = "로그인")
    public ResponseEntity<Object> signIn(@RequestBody SignInRequest signInRequest, HttpServletRequest request) {
        try {
            SignInResponse signInResponse = loginService.signIn(signInRequest, request);
            return ResponseEntity.ok().body(signInResponse);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
//        return ApiResponse.success(loginService.signIn(request));
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


    @DeleteMapping(value = "/fin-the-pen-web/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        if (loginService.logout(request)) {
            return ResponseEntity.ok().body("로그아웃 되었습니다.");
        }
        return ResponseEntity.badRequest().body("로그아웃 오류");
    }

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
