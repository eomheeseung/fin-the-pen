package project.fin_the_pen.config.swagger;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("fin-the-pen API DOCS")
                .version("v1")
                .description("fin-the-pen API 명세서 입니다.");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
