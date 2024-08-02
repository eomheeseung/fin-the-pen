package project.fin_the_pen.config.oauth2.social;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialRole {
    GUEST("ROLE_GUEST"), USER("ROLE_USER");

    private final String key;
}
