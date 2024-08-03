package project.fin_the_pen.config.oauth2.social;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class OauthAttributes {
    private String nameAttributeKey;
    private Oauth2UserInfo oauth2UserInfo;

    @Builder
    public OauthAttributes(String nameAttributeKey,
                           Oauth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

//    public static OauthAttributes of(SocialType socialType,
//                                     String userNameAttributeName,
//                                     Map<String, Object> attributes) {
//        /*if (socialType == SocialType.NAVER) {
//            return ofNaver(userNameAttributeName, attributes);
//        }
//        if (socialType == SocialType.KAKAO) {
//            return ofKakao(userNameAttributeName, attributes);
//        }
//        return ofGoogle(userNameAttributeName, attributes);*/
//
//        return ofKakao(userNameAttributeName, attributes);
//    }

//    private static OauthAttributes ofKakao(String userNameAttributeName,
//                                           Map<String, Object> attributes) {
//        return OauthAttributes.builder()
//                .nameAttributeKey(userNameAttributeName)
//                .oauth2UserInfo(new KakaoOauth2UserInfo(attributes))
//                .build();
//    }

//    private static OauthAttributes ofNaver(String userNameAttributeName,
//                                           Map<String, Object> attributes) {
//        return OauthAttributes.builder()
//                .nameAttributeKey(userNameAttributeName)
//                .oauth2UserInfo(new NaverOauth2UserInfo(attributes))
//                .build();
//    }

//    private static OauthAttributes ofGoogle(String userNameAttributeName,
//                                            Map<String, Object> attributes) {
//        return OauthAttributes.builder()
//                .nameAttributeKey(userNameAttributeName)
//                .oauth2UserInfo(new GoogleOauth2UserInfo(attributes))
//                .build();
//    }

    public SocialUser toEntity(SocialType socialType,
                               Oauth2UserInfo oAuth2UserInfo) {
        return SocialUser.builder()
                .socialType(socialType)
                .socialId(oAuth2UserInfo.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .nickName(oAuth2UserInfo.getNickName())
                .imageUrl(oAuth2UserInfo.getImageUrl())
                .role(SocialRole.GUEST)
                .build();
    }
}
