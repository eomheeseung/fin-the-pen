package project.fin_the_pen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.fin_the_pen.data.member.User;
import project.fin_the_pen.data.member.UserDAO;
import project.fin_the_pen.data.member.UserDTO;
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


    public void joinUser(UserDAO userDAO) {
        loginRepository.joinRegister(userDAO);
    }

    public Optional<User> TempFindUser() {
        List<User> all = loginRepository.findAll();
        return all.stream().filter(user -> user.getUserName().equals("테스터")).findFirst();
    }

    public UserDTO findByUser(String id, String password) {
        UserDTO currentUser = loginRepository.findByUser(id, password);
        return currentUser;
    }
}