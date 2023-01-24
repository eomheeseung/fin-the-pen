package project.fin_the_pen.repository;

import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.member.User;
import project.fin_the_pen.data.member.UserRequestDTO;
import project.fin_the_pen.data.member.UserResponseDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class LoginRepository {
    @PersistenceContext
    EntityManager entityManager;

    // TODO 사용자가 회원가입할 때 id,pw를 중복검사
    public void joinRegister(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUserId(userRequestDTO.getUser_id());
        user.setName(userRequestDTO.getName());
        user.setPassword(userRequestDTO.getPassword());
        user.setPhoneNumber(userRequestDTO.getPhone_number());
        user.setRegisterDate(userRequestDTO.getRegisterDate());
        entityManager.persist(user);
    }

    public List<User> findAll() {
        List<User> findUserAll = entityManager.createQuery("select u from User u", User.class).getResultList();
        return findUserAll;
    }

    public UserResponseDTO findByUser(String id, String password) {
        User user = entityManager
                .createQuery("select u from User u where u.userId = :findId and u.password =: findPw", User.class)
                .setParameter("findId", id)
                .setParameter("findPw", password)
                .getSingleResult();

        UserResponseDTO currentUser = new UserResponseDTO();
        currentUser.setId(user.getId());
        currentUser.setUser_id(user.getUserId());
        currentUser.setName(user.getName());
        currentUser.setBaby(user.getBaby());
        currentUser.setRegisterDate(user.getRegisterDate());
        currentUser.setUserRole(user.getUserRole());
        currentUser.setPhone_number(user.getPhoneNumber());

        return currentUser;
    }
}
