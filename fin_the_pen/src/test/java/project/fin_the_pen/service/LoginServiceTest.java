package project.fin_the_pen.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoginServiceTest {
    @Autowired
    LoginService loginService;

    @Test
    @DisplayName("데이터베이스 등록")
    void registerDB() {
        // given
        loginService.start();
        // when

        // then
    }
}