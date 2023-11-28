package project.fin_the_pen.model.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.dto.UserResponseDTO;
import project.fin_the_pen.model.user.entity.Users;
import project.fin_the_pen.model.user.repository.LoginRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void convertStrategy() {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public void init() {
        loginRepository.init();
    }

    public boolean joinUser(UserRequestDTO userRequestDTO) {
        if (loginRepository.joinRegister(userRequestDTO)) {
            return true;
        } else return false;
    }


    public Optional<Users> TempFindUser() {
        List<Users> all = loginRepository.findAll();
        return all.stream().filter(users -> users.getName().equals("테스터")).findFirst();
    }

    /*public UserResponseDTO findByUser(String id, String password) {
        UserResponseDTO currentUser = loginRepository.findByUser(id, password);
        return currentUser;
    }*/

    public UserResponseDTO convertUserFunc(String id, String password) {
        Users currentUser = findByUser(id, password);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(currentUser.getUserId());
        userResponseDTO.setUserRole(currentUser.getUserRole());
        userResponseDTO.setBaby(currentUser.getBaby());
        userResponseDTO.setName(currentUser.getName());
        userResponseDTO.setRegisterDate(currentUser.getRegisterDate());

        UserResponseDTO convertUser = objectMapper.convertValue(userResponseDTO, UserResponseDTO.class);
        return convertUser;
    }

    public Users findByUser(String id, String password) {
        return loginRepository.findByUser(id, password);
    }

    public boolean update(UserRequestDTO dto) {
        Users modifyUser = findByUser(dto.getUserId(), dto.getPassword());

        modifyUser.setPassword(dto.getPassword() == null || dto.getPassword().isBlank()
                ? modifyUser.getPassword() : passwordEncoder.encode(dto.getPassword()));
        modifyUser.setName(dto.getName());
        modifyUser.setPhoneNumber(dto.getPhoneNumber());

        try {
            return loginRepository.modifyUser(modifyUser);
        } catch (Exception e) {
            return false;
        }
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