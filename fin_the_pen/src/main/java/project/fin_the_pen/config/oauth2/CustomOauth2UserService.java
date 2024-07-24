package project.fin_the_pen.config.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 사용자 정보를 처리하는 로직
        // 예: 사용자 정보를 데이터베이스에 저장하거나 업데이트
        Map<String, Object> attributes = new HashMap<>();
        // 사용자 정보 가져오기 및 처리
        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority("ROLE_USER", attributes)),
                attributes,
                "id");
    }
}
