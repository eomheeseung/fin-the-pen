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
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.dto.UserResponseDTO;
import project.fin_the_pen.model.user.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "API 테스트 / 로그인")
public class LoginController {
    private final LoginService loginService;

    @PostMapping(value = "/sign-up", produces = "application/json")
    @Operation(summary = "회원 가입 (O)")
    public ResponseEntity<Object> signUp(@RequestBody UserRequestDTO userRequestDTO) {
        log.info("회원 가입 method call");
        UserResponseDTO userResponseDTO = loginService.signUp(userRequestDTO);
        return ResponseEntity.ok().body(userResponseDTO);
//        return ApiResponse.success(loginService.signUp(userRequestDTO));
    }


    @PostMapping(value = "/sign-in", produces = "application/json")
    @Operation(summary = "로그인 (O)")
    public ResponseEntity<Object> signIn(@RequestBody SignInRequest signInRequest,
                                         HttpServletResponse response) {
        Map<String, Object> responseMap = loginService.signIn(signInRequest, response);

        try {

            Optional<String> status =
                    Optional.ofNullable(responseMap.get("refreshToken").toString());

            if (status.isEmpty()) {
                return ResponseEntity.badRequest().body(responseMap);
            } else {
                return ResponseEntity.ok(responseMap);
            }

        } catch (IllegalArgumentException | NullPointerException e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(responseMap);
        }
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
