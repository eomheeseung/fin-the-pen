package project.fin_the_pen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.data.schedule.Schedule;

import java.util.List;
import java.util.UUID;

// <>안에는 엔티티 클래스 이름과 ID 필드 타입이 지정 => 여기서는 Schedule, UUID
@Repository
public interface CRUDScheduleRepository extends JpaRepository<Schedule, UUID> {
    @Query("SELECT s FROM Schedule s WHERE s.date LIKE CONCAT('%',:date,'%') and s.userId = :userId")
    List<Schedule> findByMonthSchedule(@Param("date") String date, @Param("userId") String userId);
}
