package project.fin_the_pen.finClient.data.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserRequestDTO {
    private String user_id;
    private String password;
    private String name;
    private Date date;
    private Date registerDate;
    private Date baby;
    private String userRole;
    private String phone_number;
}
