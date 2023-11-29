package project.fin_the_pen.model.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.user.entity.Users;

import java.util.Optional;

@Repository
public interface CRUDLoginRepository extends JpaRepository<Users, Long> {
    @Query("select u from Users u where u.userId = :loginId")
    public Optional<Users> findByUserId(@Param("loginId") String loginId);
}
