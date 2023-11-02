package project.fin_the_pen.finClient.data.user;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
public class UserAppPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_id")
    private Long privateId;

    @Column(name = "apple_password")
    @Size(min = 6, max = 6)
    private String password;

    @Builder
    public UserAppPassword(String password) {
        this.password = password;
    }
}
