package project.fin_the_pen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.fin_the_pen.data.member.User;
import project.fin_the_pen.data.member.UserRequestDTO;
import project.fin_the_pen.data.member.UserResponseDTO;
import project.fin_the_pen.repository.LoginRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {
    private final LoginRepository loginRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public void joinUser(UserRequestDTO userRequestDTO) {
        loginRepository.joinRegister(userRequestDTO);
    }

    public Optional<User> TempFindUser() {
        List<User> all = loginRepository.findAll();
        return all.stream().filter(user -> user.getName().equals("테스터")).findFirst();
    }

    public UserResponseDTO findByUser(String id, String password) {
        UserResponseDTO currentUser = loginRepository.findByUser(id, password);
        return currentUser;
    }
}