package project.fin_the_pen.model.user.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
