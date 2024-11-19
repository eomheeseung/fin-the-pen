package project.fin_the_pen.config.oauth2.custom;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import project.fin_the_pen.config.oauth2.socialDomain.SocialType;

import java.util.Collection;
import java.util.Map;

@Slf4j
@Getter
public class CustomOAuth2KakaoUser extends CustomOAuth2BaseUser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomOAuth2KakaoUser(OAuth2User oAuth2User) {
        super(oAuth2User);
    }

    @Override
    public String getFullName() {
        Map<String, Object> kakaoAccount = getKakaoAccount();

        Object object = kakaoAccount.get("profile");

        Map<String, Object> profile = objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {
        });

        String nickName = profile.get("nickname").toString();
        return nickName;
    }

    private Map<String, Object> getKakaoAccount() {
        return objectMapper.convertValue(getAttributes().get("kakao_account"), new TypeReference<>() {
        });
    }

    public String getPhoneNumber() {
        Map<String, Object> kakaoAccount = getKakaoAccount();

        return kakaoAccount.get("phone_number").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = getKakaoAccount();

        return kakaoAccount.get("email").toString();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return super.getAttributes();
    }

    @Override
    public String getSocial() {
        return SocialType.KAKAO.toString();
    }
}
