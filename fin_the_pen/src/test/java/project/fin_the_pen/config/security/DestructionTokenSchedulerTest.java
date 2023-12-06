package project.fin_the_pen.config.security;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import project.fin_the_pen.finClient.core.util.TokenManager;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.service.ScheduleService;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.user.dto.SignInRequest;
import project.fin_the_pen.model.user.dto.SignInResponse;
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.service.LoginService;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private TokenManager tokenManager;
    private String token;

    @Test
    @Transactional(rollbackFor = Exception.class, noRollbackFor = RuntimeException.class)
    @Order(1)
    public void init() {
        registerUser();
        token = login();
    }

    @Test
    @Order(2)
    public void registerSchedule() throws IOException, ParseException {
        ScheduleRequestDTO dto = new ScheduleRequestDTO();
        JSONParser parser = new JSONParser();

        // JSON 파일 읽기
        Reader reader = new FileReader("/Users/eomhuiseung/IdeaProjects/fin-the-pen/fin_the_pen/src/main/java/project/fin_the_pen/finClient/core/util/register.json");
        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        dto.setUserId((String) jsonObject.get("user_id"));
        dto.setEventName((String) jsonObject.get("event_name"));
        dto.setCategory((String) jsonObject.get("category"));
        dto.setStartDate((String) jsonObject.get("start_date"));
        dto.setEndDate((String) jsonObject.get("end_date"));
        dto.setStartTime((String) jsonObject.get("end_time"));
        dto.setAllDay((Boolean) jsonObject.get("is_all_day"));
//        dto.setRepeat((String) jsonObject.get("repeat"));
        dto.setPeriod((String) jsonObject.get("period"));
        dto.setPriceType(PriceType.Plus);
        dto.setExclude((Boolean) jsonObject.get("exclusion"));
        dto.setImportance((String) jsonObject.get("importance"));
        dto.setAmount((String) jsonObject.get("set_amount"));
        dto.setFixAmount((Boolean) jsonObject.get("fix_amount"));

        HttpServletRequest request = new MockHttpServletRequest();
        String authorization = request.getHeader("Authorization");
//        scheduleService.registerSchedule(dto, request);
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

    private String login() {
        MockHttpServletRequest mockHttpServletRequest = new MockMultipartHttpServletRequest();
        SignInRequest request = new SignInRequest();
        request.setLoginId("test1234");
        request.setPassword("1111");
        SignInResponse signInResponse = loginService.signIn(request, mockHttpServletRequest);
        log.info("get token:{}", signInResponse.getToken());

        return signInResponse.getToken();
    }


}