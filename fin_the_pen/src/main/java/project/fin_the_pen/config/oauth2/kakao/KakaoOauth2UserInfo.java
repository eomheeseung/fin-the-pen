package project.fin_the_pen.config.oauth2.kakao;

import project.fin_the_pen.config.oauth2.social.Oauth2UserInfo;

import java.util.Map;

public class KakaoOauth2UserInfo extends Oauth2UserInfo {
    public KakaoOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getNickName() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");

        if (account == null || profile == null) {
            return null;
        }
        return (String) profile.get("nickname");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) attributes.get("profile");

        if (account == null || profile == null) {
            return null;
        }
        return (String) profile.get("thumbnail_image_url");
    }
}
