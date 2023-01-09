package project.fin_the_pen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.fin_the_pen.member.User;
import project.fin_the_pen.repository.LoginRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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
}
