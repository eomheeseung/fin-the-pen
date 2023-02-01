package project.fin_the_pen.repository;

import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.member.User;
import project.fin_the_pen.data.member.UserRequestDTO;
import project.fin_the_pen.data.member.UserResponseDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class LoginRepository {
    @PersistenceContext
    EntityManager entityManager;

    public void init() {
        User user = new User();
        user.setUserId("admin@naver.com");
        user.setName("관리자");
        user.setPassword("1234");
        user.setPhoneNumber("010-1111-1111");
        user.setRegisterDate(java.util.Calendar.getInstance().getTime());

        entityManager.persist(user);
    }

    // TODO 사용자가 회원가입할 때 id,pw를 중복검사
    public boolean joinRegister(UserRequestDTO userRequestDTO) {
        if (!duplicateLogin(userRequestDTO)) {
            return false;
        }

        User user = new User();
        user.setUserId(userRequestDTO.getUser_id());
        user.setName(userRequestDTO.getName());
        user.setPassword(userRequestDTO.getPassword());
        user.setPhoneNumber(userRequestDTO.getPhone_number());
        user.setRegisterDate(userRequestDTO.getRegisterDate());

        entityManager.persist(user);
        return true;
    }

    public List<User> findAll() {
        List<User> findUserAll = entityManager.createQuery("select u from User u", User.class).getResultList();
        return findUserAll;
    }

    public UserResponseDTO findByUser(String id, String password) {
        try {
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
        } catch (Exception e) {
            return null;
        }
    }

    private boolean duplicateLogin(UserRequestDTO userRequestDTO) {
        List<User> all = findAll();
        Optional<User> duplicatedUser = all.stream()
                .filter(user -> user.getUserId().equals(userRequestDTO.getUser_id()) &&
                        user.getPassword().equals(userRequestDTO.getPassword())).findAny();

        // db에 동일한 id, pw가 중복이 안되면
        if (duplicatedUser.isEmpty()) {
            return true;
        }else return false;
    }
}
