package project.fin_the_pen.model.usersToken.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UsersToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token")
    private String accessToken;

    /*
    TODO
     expire time에 따라서 refresh도 해야 하고,
     expire time도 갱신
     사용자가 접속 30분이 지나면 DB에서 token 폐기
     */

    @Column(name = "user_id")
    private String userId;

    @Column(name = "expire_time")
    @Setter
    private Date expireTime;

}
