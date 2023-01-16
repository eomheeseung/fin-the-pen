package project.fin_the_pen.data.member;

import lombok.Data;

import java.util.Date;

@Data
public class UserDAO {
    private String userId;
    private String password;
    private String userName;
    private Date date;
    private Date registerDate;
    private Date baby;
    private String userRole;
    private String phoneNumber;

}
