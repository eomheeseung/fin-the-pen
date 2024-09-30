package project.fin_the_pen.model.assets.periodic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.assets.periodic.entity.PeriodicAmount;

import java.util.List;
import java.util.Optional;

/**
 * 정기 입/출금
 */
@Repository
public interface PeriodicRepository extends JpaRepository<PeriodicAmount, Long> {

    @Query("select p from PeriodicAmount p where p.userId = :userId")
    List<PeriodicAmount> findByUserId(@Param("userId") String userId);

    @Query("select p from PeriodicAmount p where p.userId = :userId and p.nickName = :nickName")
    Optional<PeriodicAmount> findByUserIdAndNickName(@Param("userId") String userId, @Param("nickName") String nickName);

    @Query("select p from PeriodicAmount p where p.id = :id and p.userId = :userId")
    Optional<PeriodicAmount> findByIdAndUserId(@Param("id") Long id, @Param("userId") String userId);
}
