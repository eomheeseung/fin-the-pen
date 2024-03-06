package project.fin_the_pen.model.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.report.entity.Reports;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Reports, Long> {
    @Query("select r.amount from Reports r where r.userId = :userId and r.date = :date")
    Optional<String> findByAmountAndUserIdAndDate(@Param("date") String date, @Param("userId") String userId);

    /**
     * Ex)
     * request : 2024-02-01
     * DB : 2024-02
     * query 할 때
     * @param date
     * @param userId
     * @return
     */
    @Query("select r.amount from Reports r where r.userId = :userId and parsedatetime(r.date, 'yyyy-MM') = :date")
    Optional<String> findByContainAmountAndUserIdAndDate(@Param("date") String date, @Param("userId") String userId);

    @Query("select r from Reports r where r.userId = :userId and r.date = :date")
    Optional<Reports> findByUserIdAndDate(@Param("date") String date, @Param("userId") String userId);
}
