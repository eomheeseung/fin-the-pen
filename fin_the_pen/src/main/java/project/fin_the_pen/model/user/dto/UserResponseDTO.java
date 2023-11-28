package project.fin_the_pen.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import project.fin_the_pen.model.user.type.UsersType;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class UserResponseDTO {
    private String userId;
    private String name;
    private LocalDate baby;
    private LocalDate registerDate;
    private UsersType userRole;
    private String phoneNumber;
}
