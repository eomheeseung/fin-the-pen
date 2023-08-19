package project.fin_the_pen.schedule;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import project.fin_the_pen.finClient.controller.ScheduleController;
import project.fin_the_pen.finClient.data.member.UserResponseDTO;
import project.fin_the_pen.login.LoginTest;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ScheduleTest {
    @Autowired
    ScheduleController controller;
    LoginTest loginTest = new LoginTest();
    UserResponseDTO findUser;

    @Test
    @PostConstruct
    @DisplayName(value = "로그인 먼저 수행")
    public void init() throws IOException {
//        loginTest.register();
//        findUser = loginTest.login();
    }

    @Test
    @DisplayName(value = "일정 추가")
    public void test1() {
        HttpServletRequest request = new MockHttpServletRequest();
//        ScheduleRequestDTO scheduleRequestDTO = new ScheduleRequestDTO();
//        scheduleRequestDTO.setEventName("미용");

//        controller.registerSchedule(scheduleRequestDTO, request.getSession());
    }

   /* @Test
    @DisplayName(value = "일정 조회")
    public void test2() {
        String value = "미용";
        int number = 1;
        ScheduleResponseDTO findSchedule = controller.findSchedule(number);
        Assertions.assertThat(value).isEqualTo(findSchedule.getEventName());
    }*/

}