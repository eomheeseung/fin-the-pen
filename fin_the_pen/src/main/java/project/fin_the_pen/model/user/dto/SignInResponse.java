package project.fin_the_pen.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import project.fin_the_pen.model.user.type.UsersType;

@Data
@Builder
@AllArgsConstructor
public class SignInResponse {
    private String name;
    private UsersType usersType;
    private String token;
}
