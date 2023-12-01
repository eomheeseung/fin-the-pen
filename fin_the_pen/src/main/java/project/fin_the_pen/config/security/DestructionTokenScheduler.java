package project.fin_the_pen.config.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

import java.time.Instant;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Slf4j
public class DestructionTokenScheduler {
    private final UsersTokenRepository tokenRepository;


    // 주기적으로 실행되어 만료된 토큰을 파기하는 메서드
//    @Scheduled(fixedRate = 30 * 60 * 1000) // 30분마다 실행
    @Scheduled(fixedRate = 5 * 60 * 1000) // 2분마다 실행
    @Transactional
    public void invalidateExpiredTokens() {
        Date deleteTime = Date.from(Instant.now());


        // logging 추가
        log.info("Scheduled method executed at: {}", deleteTime);
        tokenRepository.deleteByAccessTokenIsAfter(deleteTime);
    }
}
