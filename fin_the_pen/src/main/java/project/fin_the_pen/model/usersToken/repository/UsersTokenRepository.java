package project.fin_the_pen.model.usersToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.usersToken.entity.UsersToken;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UsersTokenRepository extends JpaRepository<UsersToken, Long> {
    @Query("select ut from UsersToken ut where ut.accessToken = :accessToken")
    public Optional<UsersToken> findUsersToken(@Param("accessToken") String accessToken);

    @Modifying
    @Query("DELETE from UsersToken ut WHERE ut.expireTime < :deleteTime")
    public void deleteByAccessTokenIsAfter(@Param("deleteTime") Date deleteTime);

    @Modifying
    @Query("delete from UsersToken ut where ut.accessToken = :accessToken")
    public void deleteByAccessToken(@Param("accessToken") String accessToken);

    @Modifying
    @Query("update UsersToken ut set ut.expireTime = :newExpireTime where ut.accessToken = :accessToken")
    void updateExpireTimeByAccessToken(@Param("accessToken") String accessToken, @Param("newExpireTime") Date newExpireTim);


}
