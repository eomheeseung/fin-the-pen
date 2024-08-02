package project.fin_the_pen.config.oauth2.social;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {

    Optional<SocialUser> findByEmail(String email);

    Optional<SocialUser> findByNickName(String nickName);

    Optional<SocialUser> findByRefreshToken(String refreshToken);

    Optional<SocialUser> findBySocialTypeAndSocialId(SocialType socialType,
                                                     String socialId);
}
