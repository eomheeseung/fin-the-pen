package project.fin_the_pen.config.oauth2.custom;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;


@Slf4j
@Getter
public class CustomOAuth2KakaoUser extends CustomOAuth2BaseUser {
    private final String social = "kakao";

    public CustomOAuth2KakaoUser(OAuth2User oAuth2User) {
        super(oAuth2User);
    }

    @Override
    public String getEmail() {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        ObjectMapper objectMapper = new ObjectMapper();
        log.info("get Email call");
        TypeReference<Map<String, Object>> typeReferencer = new TypeReference<Map<String, Object>>() {
        };

        Object kakaoAccount = attributes.get("kakao_account");
        Map<String, Object> account = objectMapper.convertValue(kakaoAccount, typeReferencer);

        return (String) account.get("email");
    }

    public String getName() {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Log for debugging
        log.info("getName call");

        // Extracting the profile nickname from the kakao_account attribute
        if (attributes.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount.containsKey("profile")) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                return (String) profile.get("nickname");
            }
        }
        return null;
    }

    @Override
    public String getFullName() {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        if (attributes.containsKey("kakao_account")) {  // Kakao
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount.containsKey("profile")) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                log.info(profile.toString());
                return (String) profile.get("nickname");
            }
        }
        return null;
    }

    @Override
    public String getSocial() {
        return social;
    }
}
