package project.fin_the_pen.model.usersToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.usersToken.entity.UsersToken;

import java.util.Optional;

@Repository
public interface UsersTokenRepository extends JpaRepository<UsersToken, Long> {
    @Query("select ut from UsersToken ut where ut.usersToken = :accessToken")
    public Optional<UsersToken> findUsersToken(@Param("accessToken") String accessToken);
}
