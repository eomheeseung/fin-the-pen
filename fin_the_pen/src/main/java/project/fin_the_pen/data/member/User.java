package project.fin_the_pen.data.member;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Data
public class User {
    // TODO date 객체 사용
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotEmpty
    @Column(name = "user_id")
    private String userId;

    @NotEmpty
    @Column(name = "password")
    private String password;

//    @NotEmpty
    @Column(name = "name")
    private String name;

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
