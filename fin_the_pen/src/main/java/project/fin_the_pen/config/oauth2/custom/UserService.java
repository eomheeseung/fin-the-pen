package project.fin_the_pen.config.oauth2.custom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.config.oauth2.socialDomain.SocialUser;
import project.fin_the_pen.config.oauth2.socialDomain.SocialUserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final SocialUserRepository socialUserRepository;

    public void saveUser(String name, String email) {
        log.info("Saving user with email: {} and name: {}", email, name);
        SocialUser socialUser = SocialUser
                .builder()
                .nickName(name)
                .build();

        socialUser.authorizeUser();

        socialUserRepository.save(socialUser);
    }
}
