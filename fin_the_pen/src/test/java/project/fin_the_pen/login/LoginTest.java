package project.fin_the_pen.login;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import project.fin_the_pen.finClient.api.login.controller.LoginController;
import project.fin_the_pen.model.user.dto.UserRequestDTO;

import javax.annotation.PostConstruct;
import java.io.IOException;

@SpringBootTest
@Rollback(value = false)
public class LoginTest {
    @Autowired
    LoginController loginController;

    @Test
    @DisplayName(value = "회원 저장")
    @PostConstruct
    public void register() throws IOException {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserId("test");
        userRequestDTO.setPassword("test1234");
        userRequestDTO.setName("테스터1");
        loginController.join(userRequestDTO);
    }

    /*@Test
    @DisplayName(value = "로그인")
     public void login() throws IOException {
        // given
        HttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUser_id("test");
        userRequestDTO.setPassword("test1234");
        UserResponseDTO findUser = loginController.apiLogin(userRequestDTO, mockHttpServletRequest);

        Assertions.assertThat(userRequestDTO.getUser_id()).isEqualTo(findUser.getUser_id());

    }*/
}
