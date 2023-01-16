package project.fin_the_pen.repository;

import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.member.User;
import project.fin_the_pen.data.member.UserDAO;
import project.fin_the_pen.data.member.UserDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class LoginRepository {
    @PersistenceContext
    EntityManager entityManager;

    public void joinRegister(UserDAO userDAO) {
        User user = new User();
        user.setUserId(userDAO.getUserId());
        user.setUserName(userDAO.getUserName());
        user.setPassword(userDAO.getPassword());
        user.setPhoneNumber(userDAO.getPhoneNumber());
        user.setRegisterDate(userDAO.getRegisterDate());
        entityManager.persist(user);
    }

    public List<User> findAll() {
        List<User> findUserAll = entityManager.createQuery("select u from User u", User.class).getResultList();
        return findUserAll;
    }

    public UserDTO findByUser(String id, String password) {
        User user = entityManager
                .createQuery("select u from User u where u.userId = :findId and u.password =: findPw",User.class)
                .setParameter("findId", id)
                .setParameter("findPw", password)
                .getSingleResult();

        UserDTO currentUser = new UserDTO();
        currentUser.setId(user.getId());
        currentUser.setUserId(user.getUserId());
        currentUser.setUserName(user.getUserName());
        currentUser.setBaby(user.getBaby());
        currentUser.setRegisterDate(user.getRegisterDate());
        currentUser.setUserRole(user.getUserRole());
        currentUser.setPhoneNumber(user.getPhoneNumber());

        return currentUser;
    }
}
