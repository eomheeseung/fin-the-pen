package project.fin_the_pen.config.oauth2.naver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverUserInfoService {
    public Map<String, Object> getUserInfo(OAuth2UserRequest request) {
        return new HashMap<>();
    }
}
