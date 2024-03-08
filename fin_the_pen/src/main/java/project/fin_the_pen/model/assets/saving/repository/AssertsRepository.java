package project.fin_the_pen.model.assets.saving.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.assets.saving.domain.entity.Asserts;

import java.util.Optional;

@Repository
public interface AssertsRepository extends JpaRepository<Asserts, Long> {

    @Query("select a from Asserts a where a.userId = :userId")
    Optional<Asserts> findByUserId(@Param("userId") String userId);
}
