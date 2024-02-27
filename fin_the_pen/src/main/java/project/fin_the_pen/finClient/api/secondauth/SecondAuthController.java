package project.fin_the_pen.finClient.api.secondauth;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import project.fin_the_pen.model.user.service.LoginService;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "API 테스트 / 2차 인증 로그인")
@RequestMapping("/auth")
public class SecondAuthController {
    private final LoginService loginService;

    @PostMapping("/sign-up")
    public boolean appPasswordSignUp(@RequestBody ConcurrentHashMap<String, String> map) {
        boolean isPassword = loginService.saveAppPassword(map.get("password"));

        if (isPassword) {
            return true;
        } else {
            return false;
        }
    }

    @PostMapping
    public boolean appPasswordLogin(@RequestBody ConcurrentHashMap<String, String> map) {
        boolean isPassword = loginService.appPasswordLogin(map.get("password"));

        if (isPassword) {
            return true;
        } else {
            return false;
        }
    }
}
