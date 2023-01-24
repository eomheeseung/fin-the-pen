package project.fin_the_pen.data.member;

import lombok.Data;

import java.util.Date;

@Data
public class UserResponseDTO {
    private int id;
    private String user_id;
    private String name;
    private Date baby;
    private Date registerDate;
    private String userRole;
    private String phone_number;

}
