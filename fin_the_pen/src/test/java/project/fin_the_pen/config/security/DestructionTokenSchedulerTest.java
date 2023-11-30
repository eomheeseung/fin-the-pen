package project.fin_the_pen.config.security;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import project.fin_the_pen.model.user.dto.SignInRequest;
import project.fin_the_pen.model.user.dto.SignInResponse;
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.service.LoginService;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DestructionTokenSchedulerTest {
    private final Logger log = org.slf4j.LoggerFactory.getLogger(DestructionTokenSchedulerTest.class);

    @MockBean
    private UsersTokenRepository tokenRepository;

    @Autowired
    private LoginService loginService;

    @Test
    @Transactional(rollbackFor = Exception.class, noRollbackFor = RuntimeException.class)
    @Order(1)
    public void init() {
        registerUser();
        login();
    }

    private void registerUser() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUserId("test1234");
        dto.setPassword("1111");
        dto.setPhoneNumber("010-1111-1111");
        dto.setName("테스터");
        dto.setRegisterDate(LocalDate.now());
        dto.setBaby(LocalDate.now());

        loginService.signUp(dto);
    }

    private void login() {
        SignInRequest request = new SignInRequest();
        request.setLoginId("test1234");
        request.setPassword("1111");
        SignInResponse signInResponse = loginService.signIn(request);
        log.info("get token:{}", signInResponse.getToken());
    }



}