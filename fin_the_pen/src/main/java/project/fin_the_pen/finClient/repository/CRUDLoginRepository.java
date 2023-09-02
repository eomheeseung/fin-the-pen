package project.fin_the_pen.finClient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.finClient.data.user.User;

@Repository
public interface CRUDLoginRepository extends JpaRepository<User, Long> {
}
