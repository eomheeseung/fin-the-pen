package project.fin_the_pen.model.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import project.fin_the_pen.model.user.type.UsersType;

import java.time.LocalDate;

@Data
public class UserRequestDTO {
    @JsonProperty("user_id")
    @Schema(description = "회원가입할 id",example = "test1234")
    private String userId;

    @Schema(description = "사용할 비밀번호",example = "1111")
    private String password;

    @Schema(description = "유저 이름", example = "테스터")
    private String name;

    @Hidden
    private LocalDate date;

    @Hidden
    @JsonProperty("register_date")
    private LocalDate registerDate;

    @Hidden
    private LocalDate baby;

    @Hidden
    private UsersType userRole;
    
    @JsonProperty("phone_number")
    @Schema(description = "핸드 번호",example = "010-1111-1111")
    private String phoneNumber;
}
