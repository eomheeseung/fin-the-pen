package project.fin_the_pen.finClient.api.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.model.user.dto.SignInRequest;
import project.fin_the_pen.model.user.dto.SignInResponse;
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.dto.UserResponseDTO;
import project.fin_the_pen.model.user.service.LoginService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "API 테스트 / 로그인")
public class LoginController {
    private final LoginService loginService;
    
    @PostMapping(value = "/sign-up", produces = "application/json")
    @Operation(summary = "회원 가입 (O)")
    public ResponseEntity<Object> signUp(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = loginService.signUp(userRequestDTO);
        return ResponseEntity.ok().body(userResponseDTO);
//        return ApiResponse.success(loginService.signUp(userRequestDTO));
    }


    @PostMapping(value = "/sign-in", produces = "application/json")
    @Operation(summary = "로그인 (O)")
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

    @DeleteMapping(value = "/logout")
    @Operation(summary = "로그아웃 (O)")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        if (loginService.logout(request)) {
            return ResponseEntity.ok().body("로그아웃 되었습니다.");
        }
        return ResponseEntity.badRequest().body("로그아웃 오류");
    }
}
