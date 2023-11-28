package project.fin_the_pen.model.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.user.entity.Users;

import java.util.Optional;

@Repository
public interface CRUDLoginRepository extends JpaRepository<Users, Long> {
    public Optional<Users> findByUserId(String id);
}
