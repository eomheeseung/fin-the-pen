package project.fin_the_pen.data.member;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @NotEmpty
    @Column(name = "user_id")
    private String userId;

    @NotEmpty
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Column(name = "name")
    private String userName;

    @Column(name = "date")
    private Date date;

    @Column(name = "register_date")
    private Date registerDate;
}
