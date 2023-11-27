package project.fin_the_pen.model.user.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserResponseDTO {
    private Long id;
    private String userId;
    private String name;
    private Date baby;
    private Date registerDate;
    private String userRole;
    private String phoneNumber;
}
