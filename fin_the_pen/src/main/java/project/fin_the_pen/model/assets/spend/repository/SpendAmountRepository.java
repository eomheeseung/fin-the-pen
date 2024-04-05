package project.fin_the_pen.model.assets.spend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.assets.spend.entity.SpendAmount;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpendAmountRepository extends JpaRepository<SpendAmount, Long> {
    @Query("select s from SpendAmount s where s.userId = :userId and s.startDate = :startDate")
    Optional<SpendAmount> findByUserIdAndStartDate(@Param("userId") String userId, @Param("startDate") String startDate);

    @Query("select s from SpendAmount s where s.userId = :userId and s.startDate = :startDate")
    List<SpendAmount> findByUserIdAndStartDateToList(@Param("userId") String userId, @Param("startDate") String startDate);
}
