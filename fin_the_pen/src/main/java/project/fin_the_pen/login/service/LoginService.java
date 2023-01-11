package project.fin_the_pen.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.fin_the_pen.data.member.User;
import project.fin_the_pen.data.member.UserDTO;
import project.fin_the_pen.login.repository.LoginRepository;

import java.util.List;
import java.util.Optional;

@Service

public class LoginService {
    private final LoginRepository loginRepository;

    @Autowired
    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public void start() {
        loginRepository.save();
        List<User> list = loginRepository.findAll();

        Optional<User> admin = list.stream().filter(u -> u.getUserId().equals("admin")).findFirst();
        System.out.println(admin.get().getUserName());
    }

    public void joinUser(UserDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setUserName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setRegisterDate(userDTO.getRegisterDate());
        user.setDate(userDTO.getDate());
        loginRepository.joinRegister(user);
    }

    public Optional<User> findUser() {
        List<User> all = loginRepository.findAll();
        return all.stream().filter(user -> user.getUserName().equals("테스터")).findFirst();
    }
}
