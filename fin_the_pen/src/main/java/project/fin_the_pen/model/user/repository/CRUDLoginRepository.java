package project.fin_the_pen.model.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.user.entity.User;

@Repository
public interface CRUDLoginRepository extends JpaRepository<User, Long> {
}
