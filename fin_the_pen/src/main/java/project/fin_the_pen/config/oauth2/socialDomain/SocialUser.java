package project.fin_the_pen.config.oauth2.socialDomain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "social_users")
@Entity
public class SocialUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;
    private String nickName;
    private int age;
    private String city;

    @Enumerated(EnumType.STRING)
    private SocialRole role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String refreshToken;


    public void authorizeUser() {
        this.role = SocialRole.USER;
    }


    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
}
