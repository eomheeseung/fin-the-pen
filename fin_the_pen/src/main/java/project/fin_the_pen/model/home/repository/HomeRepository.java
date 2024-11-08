package project.fin_the_pen.model.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.util.List;

@Repository
public interface HomeRepository extends JpaRepository<Schedule, Long> {
    @Query("select s.amount from Schedule s where s.userId = :userId and s.priceType = :priceType and s.startDate between :startDate and :endDate")
    List<String> findAmountByUserIdAndPriceType(@Param("userId") String userId, @Param("priceType") PriceType priceType,
                                                @Param("startDate") String date, @Param("endDate") String endDate);


    @Query("select s from Schedule s where s.userId = :userId and s.startDate = :targetDate")
    List<Schedule> findByUserIdAndStartDate(@Param("userId") String userId, @Param("targetDate") String targetDate);

}
