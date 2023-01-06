package project.fin_the_pen.login;

import org.springframework.stereotype.Repository;
import project.fin_the_pen.member.User;
import project.fin_the_pen.member.UserDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContexts;
import java.text.Format;
import java.util.Date;

@Repository
public class LoginRepository {
    @PersistenceContext
    EntityManager entityManager;

    private void init() {
        UserDTO adminDTO = new UserDTO();
        adminDTO.setUserId("admin");
        adminDTO.setPassword("admin1234");
        adminDTO.setUserName("관리자");
        adminDTO.setDate(new Date());
        adminDTO.setUserName("관리자");
        adminDTO.setRegisterDate(new Date());
    }

}
