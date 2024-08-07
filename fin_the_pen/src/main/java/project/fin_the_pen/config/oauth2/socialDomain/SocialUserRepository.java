package project.fin_the_pen.config.oauth2.socialDomain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {

    Optional<SocialUser> findByNickName(String nickName);
    Optional<SocialUser> findBySocialId(String socialId);

}
