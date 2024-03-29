package project.fin_the_pen.thirdparty.codefAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.fin_the_pen.model.thirdparty.codef.repository.TokenRepository;

import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class CodefPublishTokenService {
    private final TokenRepository tokenRepository;

    /**
     * 토근 발급받음
     * @param clientId
     * @param clientSecret
     */
    public void init(String clientId, String clientSecret) {
        HashMap<String, Object> stringObjectHashMap = TokenCall.publishToken(clientId, clientSecret);

        stringObjectHashMap.keySet().forEach(k -> {
            try {
                log.info(k);
                if (k.equals("access_token")) {
                    String accessToken = stringObjectHashMap.get(k).toString();
                    tokenRepository.init(accessToken);
                } else {
                    log.info(stringObjectHashMap.get(k).toString());
                }
            } catch (NullPointerException e) {
                log.info(e.toString());
            }
        });
    }


}
