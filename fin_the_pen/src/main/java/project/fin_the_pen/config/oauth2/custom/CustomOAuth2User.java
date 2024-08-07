package project.fin_the_pen.config.oauth2.custom;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oAuth2User;
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public String getEmail() {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (attributes.containsKey("response")) {  // Naver
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return (String) response.get("email");
        } else if (attributes.containsKey("kakao_account")) {  // Kakao
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("email");
            return (String) kakaoAccount.get("email");
        }
        return null;
    }

    public String getFullName() {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        if (attributes.containsKey("response")) {  // Naver
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            return (String) response.get("name");

        } else if (attributes.containsKey("kakao_account")) {  // Kakao
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            return (String) profile.get("nickname");
        }
        return null;
    }

    public boolean isNaver() {
        return oAuth2User.getAttributes().containsKey("response");
    }

    public boolean isKakao() {
        return oAuth2User.getAttributes().containsKey("kakao_account");
    }
}
