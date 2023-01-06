package project.fin_the_pen.member;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Data
public class User {
    @Id
    @Column(name = "id")
    private String id;

    @NotEmpty
    @Column(name = "user_id")
    private String userId;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Column(name = "name")
    private String userName;

    @NotEmpty
    @Column(name = "date")
    private Date date;

    @NotEmpty
    @Column(name = "register_date")
    private Date registerDate;
}
