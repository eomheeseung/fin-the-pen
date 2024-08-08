package project.fin_the_pen.config.oauth2.custom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.config.oauth2.socialDomain.SocialType;
import project.fin_the_pen.config.oauth2.socialDomain.SocialUser;
import project.fin_the_pen.config.oauth2.socialDomain.SocialUserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final SocialUserRepository socialUserRepository;

    public void saveUser(String email, String name, SocialType socialType) {
        log.info("Saving user with email: {} and name: {}", email, name);
        SocialUser socialUser = SocialUser
                .builder()
                .socialType(socialType)
                .nickName(name)
                .build();

        socialUser.authorizeUser();

        socialUserRepository.save(socialUser);
    }
}
