package project.fin_the_pen.api;

import lombok.Builder;
import lombok.Getter;


@Getter
public class TokenIssuance {

    @Builder
    public TokenIssuance(String accessToken, String tokenType, String refreshToken,
                         String expiresIn, String scope, String userSeqNo) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.userSeqNo = userSeqNo;
    }

    private String accessToken;

    private String tokenType;

    private String refreshToken;

    private String expiresIn;

    private String scope;

    private String userSeqNo;
}
