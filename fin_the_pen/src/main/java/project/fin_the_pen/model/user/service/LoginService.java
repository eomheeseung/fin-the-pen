package project.fin_the_pen.model.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.fin_the_pen.config.security.TokenProvider;
import project.fin_the_pen.model.user.dto.SignInRequest;
import project.fin_the_pen.model.user.dto.SignInResponse;
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.dto.UserResponseDTO;
import project.fin_the_pen.model.user.entity.Users;
import project.fin_the_pen.model.user.repository.CRUDLoginRepository;
import project.fin_the_pen.model.user.repository.LoginRepository;
import project.fin_the_pen.model.usersToken.entity.UsersToken;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

import javax.annotation.PostConstruct;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final LoginRepository loginRepository;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    private final CRUDLoginRepository crudLoginRepository;
    private final TokenProvider tokenProvider;
    private final UsersTokenRepository tokenRepository;

    @PostConstruct
    public void convertStrategy() {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    // 등록: 그냥 여기서 처리하자.
    @Transactional
    public UserResponseDTO signUp(UserRequestDTO userRequestDTO) {
        Users users = crudLoginRepository.save(Users.from(userRequestDTO, encoder));

        try {
            crudLoginRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        return UserResponseDTO.builder().userId(users.getUserId())
                .baby(users.getBaby())
                .phoneNumber(users.getPhoneNumber())
                .registerDate(users.getRegisterDate())
                .name(users.getName())
                .build();
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest request) {
        Users users = crudLoginRepository.findByUserId(request.getLoginId())
                .filter(find -> encoder.matches(request.getPassword(), find.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        log.info("find users Id:{}", users.getUserId());

        String token = tokenProvider.createToken(String.format("%s:%s", users.getUserId(), users.getUserRole()));
        Optional<UsersToken> findToken = tokenRepository.findUsersToken(token);


        /*
        TODO
         1. 사용자가 expire time전에 로그인을 하면 기존 토큰은 그대로 사용하고,
         expire time 만 갱신
         2. 다시 로그인을 했다면 서비스 이용을 마치고 다시 들어온것이기 때문에
         토큰을 다시 발급 but 이 방식은 어차피 expire time만 갱신하면 될 것 같음.
         그리고 이 방식을 사용하면 expire time전에 로그인을 계속 시도할 때
         토큰이 계속 생성되어 DB과부하
         */
        if (findToken.isPresent()) {
            UsersToken existingToken = findToken.get();

            if (existingToken.getUserId().equals(request.getLoginId())) {

                // 사용자가 일치하고, 토큰의 expire time이 넘기지 않은 경우
                if (existingToken.getExpireTime().after(Date.from(existingToken.getExpireTime().toInstant().plus(30, ChronoUnit.MINUTES)))) {
                    // 토큰의 expire time만 갱신
                    tokenRepository.updateExpireTimeByAccessToken(token, tokenProvider.refreshToken(findToken).getExpireTime());
                    return new SignInResponse(users.getName(), users.getUserRole(), token);
                }
            }
        }

        // 토큰이 존재하지 않거나 사용자가 일치하지 않거나 토큰의 expire time이 넘은 경우
        // 새로운 토큰 생성 및 저장
        tokenRepository.save(UsersToken.builder()
                .accessToken(token)
                .expireTime(tokenProvider.getExpiredTime())
                .userId(request.getLoginId())
                .build());

        return new SignInResponse(users.getName(), users.getUserRole(), token);
    }



    /*public Optional<Users> TempFindUser() {
        List<Users> all = loginRepository.findAll();
        return all.stream().filter(users -> users.getName().equals("테스터")).findFirst();
    }*/

    /*public UserResponseDTO findByUser(String id, String password) {
        UserResponseDTO currentUser = loginRepository.findByUser(id, password);
        return currentUser;
    }*/

    public Users findByUser(String id, String password) {
        return loginRepository.findByUser(id, password);
    }


    public boolean saveAppPassword(String password) {
        try {
            loginRepository.saveAppPassword(password);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean appPasswordLogin(String password) {
        try {
            loginRepository.appPasswordLogin(password);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}