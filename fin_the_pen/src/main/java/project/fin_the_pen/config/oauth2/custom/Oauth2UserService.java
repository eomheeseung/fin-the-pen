package project.fin_the_pen.config.oauth2.custom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.config.oauth2.socialDomain.SocialType;
import project.fin_the_pen.config.oauth2.socialDomain.SocialUser;
import project.fin_the_pen.config.oauth2.socialDomain.SocialUserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2UserService {
    private final SocialUserRepository socialUserRepository;

    public void saveUser(String email, String name, SocialType socialType) {
        log.info("Saving user with email: {} and name: {}", email, name);

        Optional<SocialUser> duplicatedUser =
                socialUserRepository.findByNickNameAndEmailAndSocialType(name, email, socialType);

        if (duplicatedUser.isEmpty()) {
            SocialUser socialUser = SocialUser
                    .builder()
                    .email(email)
                    .socialType(socialType)
                    .nickName(name)
                    .build();

            socialUser.authorizeUser();

            socialUserRepository.save(socialUser);
        }
    }
}
