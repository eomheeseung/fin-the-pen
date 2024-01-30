package project.fin_the_pen.finClient.api.healthCheck;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import project.fin_the_pen.config.security.JwtAuthenticationFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@WebMvcTest(controllers = HealthController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {JwtAuthenticationFilter.class})
})
class HealthControllerTest {

    @Autowired
    MockMvc mockMvc;


    @Test
    @DisplayName(value = "HealthController 테스트 코드")
    @WithMockUser()
    void healthCheck() throws Exception {
        System.out.println("test 시작");
        mockMvc.perform(get("/alive")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}