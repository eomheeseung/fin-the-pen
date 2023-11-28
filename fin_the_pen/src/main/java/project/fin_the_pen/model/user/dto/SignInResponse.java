package project.fin_the_pen.model.user.dto;

import lombok.AllArgsConstructor;
import project.fin_the_pen.model.user.type.UsersType;

@AllArgsConstructor
public class SignInResponse {
    private String name;
    private UsersType usersType;
    private String token;
}
