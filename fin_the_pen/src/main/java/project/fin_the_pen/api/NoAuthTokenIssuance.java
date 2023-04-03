package project.fin_the_pen.api;

import lombok.Builder;
import lombok.Getter;

@Getter
public class NoAuthTokenIssuance {
    @Builder
    public NoAuthTokenIssuance(String accessToken, String tokenType, String expiresIn, String scope, String clientUseCode) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.clientUseCode = clientUseCode;
    }

    private String accessToken;

    private String tokenType;

    private String expiresIn;

    private String scope;

    private String clientUseCode;
}
