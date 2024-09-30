package project.fin_the_pen.config.oauth2.custom;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2Service extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String clientName = userRequest.getClientRegistration().getClientName();

        if (clientName.equals("Naver")) {
            log.info("accessToken:{}", userRequest.getAccessToken().toString());
            return new CustomOAuth2NaverUser(oAuth2User);
        } else if (clientName.equals("Kakao")) {
            log.info("accessToken:{}", userRequest.getAccessToken().getTokenValue().toString());
            return new CustomOAuth2KakaoUser(oAuth2User);
        }

        return new CustomOAuth2NaverUser(oAuth2User);


//        return new CustomOAuth2User(oAuth2User);
    }
}
