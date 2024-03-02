package project.fin_the_pen.finClient.api.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import project.fin_the_pen.config.security.JwtAuthenticationFilter;
import project.fin_the_pen.finClient.core.util.ConvertResponse;
import project.fin_the_pen.model.home.dto.HomeRequestDto;
import project.fin_the_pen.model.home.service.HomeService;
import project.fin_the_pen.model.schedule.service.ScheduleService;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = HomeController.class,
excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {JwtAuthenticationFilter.class})
})
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    HomeService homeService;

    @MockBean
    ScheduleService scheduleService;

    @MockBean
    ConvertResponse convertResponse;

    @WithMockUser
    @Test
    @DisplayName(value = "홈 화면 controoler 테스트")
    void homeMonth() throws Exception {

        given(homeService.inquiryMonth(any(), any())).willReturn(new HashMap<Object, Object>(){{
            put("test", "test");
        }});

        HomeRequestDto dto = new HomeRequestDto();
        dto.setUserId("test");
        dto.setMainDate("2024-02");
        dto.setCalenderDate("2024-02-12");

        mockMvc.perform(post("/home/month")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.test").value("test"))
                .andDo(print());


    }

}