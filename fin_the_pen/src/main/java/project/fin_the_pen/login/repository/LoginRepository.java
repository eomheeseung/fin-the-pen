package project.fin_the_pen.login.repository;

import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.member.User;
import project.fin_the_pen.data.member.UserDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public class LoginRepository {
    @PersistenceContext
    EntityManager entityManager;
    UserDTO adminDTO = new UserDTO();

    private void init() {
        adminDTO.setUserId("admin");
        adminDTO.setPassword("admin1234");
        adminDTO.setUserName("관리자");
        adminDTO.setDate(new Date());
        adminDTO.setUserName("관리자");
        adminDTO.setRegisterDate(new Date());
    }

    public void save() {
        init();
        User user = new User();
        user.setUserId(adminDTO.getUserId());
        user.setPassword(adminDTO.getPassword());
        user.setUserName(adminDTO.getUserName());
        user.setDate(adminDTO.getDate());
        user.setRegisterDate(adminDTO.getRegisterDate());
        entityManager.persist(user);
    }

    public void joinRegister(User user) {
        entityManager.persist(user);
    }

    public List<User> findAll() {
        List<User> findUserAll = entityManager.createQuery("select u from User u", User.class).getResultList();
        return findUserAll;
    }
}
