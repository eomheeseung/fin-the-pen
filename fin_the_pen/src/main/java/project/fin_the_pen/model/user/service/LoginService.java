package project.fin_the_pen.model.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.fin_the_pen.config.jwt.JwtService;
import project.fin_the_pen.config.oauth2.socialDomain.SocialType;
import project.fin_the_pen.finClient.core.util.TokenManager;
import project.fin_the_pen.model.user.dto.SignInRequest;
import project.fin_the_pen.model.user.dto.SignInResponse;
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.dto.UserResponseDTO;
import project.fin_the_pen.model.user.entity.Users;
import project.fin_the_pen.model.user.repository.CRUDLoginRepository;
import project.fin_the_pen.model.user.repository.LoginRepository;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final LoginRepository loginRepository;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    private final CRUDLoginRepository crudLoginRepository;
    private final JwtService jwtService;
    private final UsersTokenRepository tokenRepository;
    private final TokenManager tokenManager;

//    @Transactional
//    @PostConstruct
//    public void init() {
//        LocalDate currentDate = LocalDate.now();
//        Date convertDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//        tokenRepository.deleteByAccessTokenIsAfter(convertDate);
//    }

    @PostConstruct
    public void convertStrategy() {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    /**
     * 회원가입
     * 회원가입하고, 유저의 정보를 보낼 필요가 있나?
     *
     * @param userRequestDTO
     * @return
     */
    @Transactional
    public UserResponseDTO signUp(UserRequestDTO userRequestDTO) {
        Users users = crudLoginRepository.save(Users.from(userRequestDTO, encoder, SocialType.NONE));

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

    public Map<String, Object> signIn(SignInRequest dto,
                                      HttpServletResponse response) {
        String userId = dto.getUserId();
        String password = dto.getPassword();

        Optional<Users> optionalUsers = crudLoginRepository.findByUserId(userId)
                .filter(find -> encoder.matches(password,
                        find.getPassword()));

        if (optionalUsers.isEmpty()) {
            HashMap<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", HttpStatus.BAD_REQUEST);
            return responseMap;
        } else {
            HashMap<String, Object> responseMap = new HashMap<>();
            responseMap.put("status", HttpStatus.OK);

            String accessToken = jwtService.createAccessToken(userId, SocialType.NONE);
            log.info("new login:{}", accessToken);
            response.addHeader("Authorization", "Bearer " + accessToken);
            String refreshToken = jwtService.createRefreshToken();
            responseMap.put("refreshToken", refreshToken);
            return responseMap;
        }
    }

    @Transactional
    public boolean logout(HttpServletRequest request) {
        String findToken = tokenManager.parseBearerToken(request);
        tokenRepository.deleteByAccessToken(findToken);
        return true;
    }

    private SignInResponse firstLogin(Users users) {
        log.info("find users Id: {}", users.getUserId());

        SocialType socialType = SocialType.NONE;

        // JWT 생성
        String token = jwtService.createAccessToken(String.format("%s:%s",
                        users.getUserId(),
                        users.getUserRole()),
                socialType);

        // SignInResponse 객체 반환
        return new SignInResponse(users.getName(), users.getUserRole(), token);
    }


    /*public Optional<Users> TempFindUser() {
        List<Users> all = loginRepository.findAll();
        return all.stream().filter(users -> users.getName().equals("테스터")).findFirst();
    }

    public UserResponseDTO findByUser(String id, String password) {
        UserResponseDTO currentUser = loginRepository.findByUser(id, password);
        return currentUser;
    }

    public Users findByUser(String id, String password) {
        return loginRepository.findByUser(id, password);
    }*/


    /*public boolean saveAppPassword(String password) {
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
    }*/
}