package project.fin_the_pen.model.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignInRequest {
    @Schema(description = "로그인 아이디", example = "test1234")
    @JsonProperty("user_id")
    private String userId;

    @Schema(description = "로그인 패스워드", example = "1111")
    private String password;

}
