package project.fin_the_pen.data.member;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Data
public class User {
    // TODO date 객체 사용
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @NotEmpty
    @Column(name = "user_id")
    private String userId;

    //TODO json 타입의 키와 엔티티 또는 DAO의 필드가 동일해야 하는거 같음.
    @NotEmpty
    @Column(name = "password")
    private String password;

//    @NotEmpty
    @Column(name = "name")
    private String userName;

    @Column(name = "baby")
    private Date baby;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date")
    private Date date;

    @Column(name = "register_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registerDate;

    @Column(name = "user_role")
    private String userRole;
}
