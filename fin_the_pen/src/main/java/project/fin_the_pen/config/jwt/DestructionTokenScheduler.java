package project.fin_the_pen.config.jwt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

@RequiredArgsConstructor
@Component
@Slf4j
public class DestructionTokenScheduler {
    private final UsersTokenRepository tokenRepository;
}
