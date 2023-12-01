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
import project.fin_the_pen.finClient.core.util.TokenManager;
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
import javax.servlet.http.HttpServletRequest;
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
    private final TokenManager tokenManager;

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

    private SignInResponse firstLogin(Users users, SignInRequest dto) {
        log.info("find users Id:{}", users.getUserId());

        String token = tokenProvider.createToken(String.format("%s:%s", users.getUserId(), users.getUserRole()));

        tokenRepository.save(UsersToken.builder()
                .accessToken(token)
                .expireTime(tokenProvider.getExpiredTime())
                .userId(dto.getLoginId())
                .build());

        return new SignInResponse(users.getName(), users.getUserRole(), token);
    }

    @Transactional
    public SignInResponse signIn(SignInRequest dto, HttpServletRequest request) {
        Users users = crudLoginRepository.findByUserId(dto.getLoginId())
                .filter(find -> encoder.matches(dto.getPassword(), find.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        String find = tokenManager.parseBearerToken(request);

        if (find == null) {
            return firstLogin(users, dto);
        } else {
            // expire time 전에 재 로그인
            Optional<UsersToken> findToken = tokenRepository.findUsersToken(find);

            // 토큰이 있고, 토큰 테이블에 저장된 id와 현재 id가 같으면...
            if (findToken.get().getUserId().equals(dto.getLoginId())) {
                UsersToken usersToken = findToken.get();

                // 가지고 잇는 토큰을 삭제하고, 새로운 토큰발급

                tokenRepository.deleteByAccessToken(usersToken.getAccessToken());
            }
            return firstLogin(users, dto);
        }

            /*
            TODO
             - 다시 로그인을 했다면 서비스 이용을 마치고 다시 들어온 것이기 때문에
             토큰을 다시 발급 기존의 토큰 파기 (access, refresh)
             and 시간이 지나면 토큰 파기
             - logout하면 access, refresh 모두 파기
             */
    }

    @Transactional
    public boolean logout(HttpServletRequest request) {
        String findToken = tokenManager.parseBearerToken(request);

        tokenRepository.deleteByAccessToken(findToken);
        return true;
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