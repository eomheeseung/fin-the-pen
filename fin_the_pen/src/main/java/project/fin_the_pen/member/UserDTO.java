package project.fin_the_pen.member;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
public class UserDTO {
    private String userId;
    private String password;
    private String userName;
    private Date date;
    private Date registerDate;
}
