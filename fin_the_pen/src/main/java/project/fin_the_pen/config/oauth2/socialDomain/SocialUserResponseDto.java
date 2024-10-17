package project.fin_the_pen.config.oauth2.socialDomain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SocialUserResponseDto {
    private String name;
    private String email;
    private SocialType socialType;
}
