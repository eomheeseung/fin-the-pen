package project.fin_the_pen.model.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    /*private final LoginRepository loginRepository;
    private final PasswordEncoder encoder;
    private final ObjectMapper objectMapper;
    private final CRUDLoginRepository crudLoginRepository;
    private final TokenProvider tokenProvider;
    private final UsersTokenRepository tokenRepository;
    private final TokenManager tokenManager;

    *//*@Transactional
    @PostConstruct
    public void init() {
        LocalDate currentDate = LocalDate.now();
        Date convertDate = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        tokenRepository.deleteByAccessTokenIsAfter(convertDate);
    }*//*

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
                .userId(dto.getUserId())
                .build());

        return new SignInResponse(users.getName(), users.getUserRole(), token);
    }

    *//**
     * 회원가입
     * @param dto
     * @param request
     * @return
     *//*
    @Transactional
    public SignInResponse signIn(SignInRequest dto, HttpServletRequest request) {
        Users users = crudLoginRepository.findByUserId(dto.getUserId())
                .filter(find -> encoder.matches(dto.getPassword(), find.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        String find = tokenManager.parseBearerToken(request);

        if (find == null) {
            return firstLogin(users, dto);
        } else {
            // expire time 전에 재 로그인
            Optional<UsersToken> findToken = tokenRepository.findUsersToken(find);

            // 토큰이 있고, 토큰 테이블에 저장된 id와 현재 id가 같으면...
            if (findToken.get().getUserId().equals(dto.getUserId())) {
                UsersToken usersToken = findToken.get();

                // 가지고 잇는 토큰을 삭제하고, 새로운 토큰발급
                tokenRepository.deleteByAccessToken(usersToken.getAccessToken());
            }
            return firstLogin(users, dto);
        }


    }

    @Transactional
    public boolean logout(HttpServletRequest request) {
        String findToken = tokenManager.parseBearerToken(request);
        tokenRepository.deleteByAccessToken(findToken);
        return true;
    }



    *//*public Optional<Users> TempFindUser() {
        List<Users> all = loginRepository.findAll();
        return all.stream().filter(users -> users.getName().equals("테스터")).findFirst();
    }*//*

    *//*public UserResponseDTO findByUser(String id, String password) {
        UserResponseDTO currentUser = loginRepository.findByUser(id, password);
        return currentUser;
    }*//*

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
    }*/
}