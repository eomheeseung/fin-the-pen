package project.fin_the_pen;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.fin_the_pen.model.schedule.service.AssetsService;
import project.fin_the_pen.model.user.service.LoginService;
import project.fin_the_pen.model.schedule.service.ScheduleService;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class FinThePenApplicationTests {
    LoginService loginService;

    @Test
    void contextLoads() {
    }

    @Autowired
    AssetsService assetsService;

    @Autowired
    ScheduleService scheduleService;

    /*@Test
    @DisplayName("일정에 따른 자산 출력")
    void print() {
        JSONObject jsonObject = assetsService
                .assetsPrintSchedule("2023-10", "a");
        Assertions.assertEquals("3", jsonObject.get("remainSchedule").toString());
    }*/
}
