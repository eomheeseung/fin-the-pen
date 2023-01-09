package project.fin_the_pen.member;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private String userId;
    private String password;
    private String userName;
    private Date date;
    private Date registerDate;
}
