package project.fin_the_pen.config.oauth2.social;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "socail_users")
@Entity
public class SocialUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private int age;
    private String city;
    private String imageUrl;
    private String nickName;

    @Enumerated(EnumType.STRING)
    private SocialRole role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;


    private String refreshToken;

    private String socialId;


    public void authorizeUser() {
        this.role = SocialRole.USER;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
