package project.fin_the_pen.model.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.schedule.entity.Schedule;

import java.util.List;

/*
<T Entity, ID>
JpaRepository 내부에 @Transactional 존재
 */
@Repository
public interface CRUDScheduleRepository extends JpaRepository<Schedule, String> {
    @Query("SELECT s FROM Schedule s WHERE s.startDate LIKE CONCAT('%',:date,'%') and s.userId = :userId")
    List<Schedule> findByMonthSchedule(@Param("date") String date, @Param("userId") String userId);

    @Query("SELECT s FROM Schedule s WHERE s.eventName LIKE CONCAT('%',:name,'%') ")
    List<Schedule> findByContainsName(@Param("name") String name);

    List<Schedule> findScheduleByUserId(String id);

    /*@Query("select s from Schedule s where s.userId = :userId and s.startDate >= :startDate and s.endDate <= :endDate")
    List<Schedule> findScheduleByDateContains(@Param("startDate") String startDate, @Param("endDate") String endDate,
                                              @Param("userId") String userId);*/

    @Query("SELECT s FROM Schedule s WHERE s.userId=:userId and s.startDate BETWEEN :startDate AND :endDate")
    List<Schedule> findScheduleByDateContains(@Param("startDate") String startDate, @Param("endDate") String endDate,
                                              @Param("userId") String userId);


    @Query("select s from Schedule s where s.userId = :userId and s.category LIKE CONCAT('%', :categoryName, '%')")
    List<Schedule> findScheduleByCategory(@Param("userId") String userSessionId, @Param("categoryName") String categoryName);
}
