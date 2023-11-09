package project.fin_the_pen.model.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.dto.UserResponseDTO;
import project.fin_the_pen.model.user.entity.User;
import project.fin_the_pen.model.user.entity.UserAppPassword;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class LoginRepository {
    @PersistenceContext
    EntityManager entityManager;

    private final CRUDLoginRepository crudLoginRepository;

    public void init() {
        User user = new User();
        user.setUserId("admin@naver.com");
        user.setName("관리자");
        user.setPassword("1234");
        user.setPhoneNumber("010-1111-1111");
        user.setRegisterDate(java.util.Calendar.getInstance().getTime());

        crudLoginRepository.save(user);
    }

    /**
     * 회원가입 (중복 검사까지)
     *
     * @param userRequestDTO
     * @return
     */
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

        crudLoginRepository.save(user);
        return true;
    }

    public List<User> findAll() {
        return crudLoginRepository.findAll();
    }

    public UserResponseDTO findByUser(String id, String password) {
        try {
            return entityManager.createQuery("select new project.fin_the_pen.finClient.login.dto.UserResponseDTO(u.id,u.userId,u.name,u.baby, u.registerDate,u.userRole, u.phoneNumber) " +
                            "from User u where u.userId =: findId and u.password =: findPw", UserResponseDTO.class)
                    .setParameter("findId", id)
                    .setParameter("findPw", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean saveAppPassword(String password) {
        UserAppPassword userAppEntity = UserAppPassword.builder().password(password).build();

        try {
            entityManager.persist(userAppEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("save error");
        }
    }

    public boolean appPasswordLogin(String password) {

        Object findPwd = entityManager.createQuery("select up from UserAppPassword up where up.password =:password")
                .setParameter("password", password)
                .getSingleResult();

        try {
            if (findPwd == null) {
                throw new NullPointerException();
            } else return true;
        } catch (NullPointerException e) {
            throw new RuntimeException("save error");
        }
    }

    private boolean duplicateLogin(UserRequestDTO userRequestDTO) {
        Optional<User> duplicatedUser = findAll().stream()
                .filter(user -> user.getUserId().equals(userRequestDTO.getUser_id()) &&
                        user.getPassword().equals(userRequestDTO.getPassword())).findAny();

        // db에 동일한 id, pw가 중복이 안되면
        if (duplicatedUser.isEmpty()) {
            return true;
        } else return false;
    }
}
