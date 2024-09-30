package project.fin_the_pen.model.assets.saving.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.assets.saving.domain.entity.PersonalGoal;

import java.util.Optional;

@Repository
public interface PersonalGoalRepository extends JpaRepository<PersonalGoal, Long> {
    @Query("select p from PersonalGoal p where p.userId = :userId")
    Optional<PersonalGoal> findByUserId(@Param("userId") String userId);
}
