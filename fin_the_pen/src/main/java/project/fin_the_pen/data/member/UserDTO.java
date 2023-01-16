package project.fin_the_pen.data.member;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private int id;
    private String userId;
    private String userName;
    private Date baby;
    private Date registerDate;
    private String userRole;
    private String phoneNumber;

}
