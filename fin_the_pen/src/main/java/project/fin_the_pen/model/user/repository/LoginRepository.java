package project.fin_the_pen.model.user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.config.security.TokenProvider;
import project.fin_the_pen.model.user.dto.UserRequestDTO;
import project.fin_the_pen.model.user.entity.UserAppPassword;
import project.fin_the_pen.model.user.entity.Users;

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
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    /*public void init() {
        Users users = new Users();
        users.setUserId("admin@naver.com");
        users.setName("관리자");
        users.setPassword("1234");
        users.setPhoneNumber("010-1111-1111");
        users.setRegisterDate(java.util.Calendar.getInstance().getTime());

        crudLoginRepository.save(users);
    }*/


    /*public boolean joinRegister(UserRequestDTO userRequestDTO) {
        if (!duplicateLogin(userRequestDTO)) {
            return false;
        }
        Optional<Users> result = crudLoginRepository
                .findByUserIdAndPassword(userRequestDTO.getUserId(), userRequestDTO.getPassword());

        Users users = result.get();
        result.filter(it -> passwordEncoder.matches(userRequestDTO.getPassword(), it.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));
        String token = tokenProvider.createToken(String.format("%s%s", users.getUserId(), users.getUserRole()));

        *//*Users users = new Users();
        users.setUserId(userRequestDTO.getUserId());
        users.setName(userRequestDTO.getName());
        users.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        users.setPhoneNumber(userRequestDTO.getPhoneNumber());
        users.setRegisterDate(userRequestDTO.getRegisterDate());*//*

        crudLoginRepository.save(users);
        return true;
    }*/

    public boolean modifyUser(Users modifyUser) {
        try {
            crudLoginRepository.save(modifyUser);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<Users> findAll() {
        return crudLoginRepository.findAll();
    }

    public Users findByUser(String id, String password) {
        try {
            Optional<Users> result = Optional.of(entityManager.createQuery("select Users from Users u where u.userId =: findId and u.password =: findPw", Users.class)
                    .setParameter("findId", id)
                    .setParameter("findPw", password)
                    .getSingleResult());
            return result.get();
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
        Optional<Users> duplicatedUser = findAll().stream()
                .filter(users -> users.getUserId().equals(userRequestDTO.getUserId()) &&
                        users.getPassword().equals(userRequestDTO.getPassword())).findAny();

        // db에 동일한 id, pw가 중복이 안되면
        if (duplicatedUser.isEmpty()) {
            return true;
        } else return false;
    }
}
