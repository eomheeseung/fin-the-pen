package project.fin_the_pen.config.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.config.oauth2.custom.CustomOAuth2KakaoUser;
import project.fin_the_pen.config.oauth2.custom.CustomOAuth2NaverUser;
import project.fin_the_pen.config.oauth2.socialDomain.SocialType;
import project.fin_the_pen.config.oauth2.socialDomain.SocialUserResponseDto;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Oauth2SuccessController {

    /**
     * 10-18
     * redirect하고,
     * front에서 fetch요청으로 /oauth2/success/info로 요청을 해야지 사용자의 정보와 자체 토큰을 발행해서 넣어줄 것임
     * @param authentication
     * @return
     */
    @GetMapping("/oauth2/success/info")
    public ResponseEntity<SocialUserResponseDto> successTransferUserInfo(Authentication authentication) {
        // 사용자 정보 가져오기
        Optional<String> name = Optional.empty();
        Optional<String> email = Optional.empty();
        SocialUserResponseDto socialUserResponseDto = new SocialUserResponseDto();
        socialUserResponseDto.setSocialType(SocialType.NONE);

        if (authentication.getPrincipal() instanceof CustomOAuth2NaverUser) {
            CustomOAuth2NaverUser oAuth2User = (CustomOAuth2NaverUser) authentication.getPrincipal();
            name = Optional.ofNullable(oAuth2User.getFullName());
            email = Optional.ofNullable(oAuth2User.getEmail());
            socialUserResponseDto.setSocialType(SocialType.NAVER);
        } else if (authentication.getPrincipal() instanceof CustomOAuth2KakaoUser) {
            CustomOAuth2KakaoUser oAuth2User = (CustomOAuth2KakaoUser) authentication.getPrincipal();
            name = Optional.ofNullable(oAuth2User.getFullName());
            email = Optional.ofNullable(oAuth2User.getEmail());
            socialUserResponseDto.setSocialType(SocialType.KAKAO);
        }

        // SocialUserResponseDto는 사용자의 정보를 담은 DTO 클래스
        if (name.isPresent()) {
            socialUserResponseDto.setName(name.get());
        } else {
            throw new NotFoundDataException("사용자의 이름이 없습니다.");
        }
        if (email.isPresent()) {
            socialUserResponseDto.setName(email.get());
        } else {
            throw new NotFoundDataException("사용자의 이메일이 없습니다.");
        }

        return ResponseEntity.ok(socialUserResponseDto);
    }
}
