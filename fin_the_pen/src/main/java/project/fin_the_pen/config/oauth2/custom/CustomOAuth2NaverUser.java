package project.fin_the_pen.config.oauth2.custom;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;


@Slf4j
@Getter
public class CustomOAuth2NaverUser extends CustomOAuth2BaseUser {
    private final String social = "naver";

    public CustomOAuth2NaverUser(OAuth2User oAuth2User) {
        super(oAuth2User);
    }

    @Override
    public String getEmail() {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        if (attributes.containsKey("response")) {  // Naver
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            log.info(response.toString());
            return (String) response.get("email");
        }
        return null;
    }

    @Override
    public String getFullName() {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        if (attributes.containsKey("response")) {  // Naver
            Map<String, Object> response = (Map<String, Object>) attributes.get("response");
            log.info(response.toString());
            return (String) response.get("name");
        }
        return null;
    }

    @Override
    public String getSocial() {
        return social;
    }
}
