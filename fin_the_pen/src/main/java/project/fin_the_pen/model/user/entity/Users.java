package project.fin_the_pen.model.user.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.type.UsersType;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    private LocalDate baby;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "register_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate registerDate;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UsersType userRole;

    public static Users from(UserRequestDTO request, PasswordEncoder encoder) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return Users.builder().userId(request.getUserId())
                .password(encoder.encode(request.getPassword()))
                .name(request.getName())
                .baby(LocalDate.now())
                .date(LocalDate.now())
                .userRole(UsersType.USER)
                .phoneNumber(request.getPhoneNumber())
                .registerDate(LocalDate.now())
                .build();
    }

    public void update(UserRequestDTO newUser, PasswordEncoder encoder) {
        this.password = newUser.getPassword() == null || newUser.getPassword().isBlank()
                ? this.password : encoder.encode(newUser.getPassword());
        this.name = newUser.getName();
        this.phoneNumber = getPhoneNumber();
    }
}
