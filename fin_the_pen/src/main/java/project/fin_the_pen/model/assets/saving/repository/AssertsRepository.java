package project.fin_the_pen.model.assets.saving.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.assets.saving.domain.entity.SavingGoal;

import java.util.Optional;

@Repository
public interface AssertsRepository extends JpaRepository<SavingGoal, Long> {

    @Query("select a from SavingGoal a where a.userId = :userId")
    Optional<SavingGoal> findByUserId(@Param("userId") String userId);
}
