package project.fin_the_pen.model.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.util.List;
import java.util.Optional;

/*
<T Entity, ID>
JpaRepository 내부에 @Transactional 존재
 */
@Repository
public interface CrudScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s WHERE s.startDate LIKE CONCAT('%',:date,'%') and s.userId = :userId")
    List<Schedule> findByMonthSchedule(@Param("date") String date, @Param("userId") String userId);

    @Query("SELECT s FROM Schedule s WHERE s.eventName LIKE CONCAT('%',:name,'%') ")
    List<Schedule> findByContainsName(@Param("name") String name);

    @Query("select s from Schedule s where s.userId = :userId")
    List<Schedule> findByUserId(@Param("userId") String userId);

    /*@Query("select s from Schedule s where s.userId = :userId and s.eventName = :eventName")
    Schedule findByUserIdAndeAndEventName(@Param("accessToken") String userId, @Param("eventName") String eventName);*/

    /*@Query("select s from Schedule s where s.userId = :userId and s.startDate >= :startDate and s.endDate <= :endDate")
    List<Schedule> findScheduleByDateContains(@Param("startDate") String startDate, @Param("endDate") String endDate,
                                              @Param("userId") String userId);*/

    @Query("SELECT s FROM Schedule s WHERE s.userId = :userId and s.startDate BETWEEN :startDate AND :endDate")
    List<Schedule> findScheduleByDateContains(@Param("startDate") String startDate, @Param("endDate") String endDate,
                                              @Param("userId") String userId);

    @Query("select s from Schedule  s where s.userId = :userId and :date between s.startDate and s.endDate")
    List<Schedule> findSchedulesByStartDateBetweenEndDate(@Param("userId") String userId, @Param("date") String date);

    @Query("select s from Schedule s where s.userId = :userId and s.startDate = :startDate")
    List<Schedule> findByStartDate(@Param("userId") String userId, @Param("startDate") String startDate);


    @Query("select s from Schedule s where s.userId = :userId and s.category LIKE CONCAT('%', :categoryName, '%')")
    List<Schedule> findScheduleByCategory(@Param("userId") String userId, @Param("categoryName") String categoryName);

    @Query("select s from Schedule s where s.userId = :userId and s.category LIKE CONCAT('%', :category, '%') and substring(s.startDate,1,7) = :date")
    List<Schedule> findByCategoryBetweenDate(@Param("userId") String userId, @Param("category") String category, @Param("date") String date);

    @Query("select s from Schedule s where s.userId = :userId and s.id = :scheduleId")
    Optional<Schedule> findByIdAndUserId(@Param("userId") String userId, @Param("scheduleId") Long scheduleId);

    /*@Query("SELECT s FROM Schedule s WHERE TO_DATE(s.startDate, 'yyyy-MM-dd') >= TO_DATE(:targetDate, 'yyyy-MM-dd') and s.eventName = :eventName")
    List<Schedule> findByAllDayNowAfter(@Param("targetDate") String targetDate, @Param("eventName") String eventName);*/

    @Query("SELECT s FROM Schedule s WHERE parsedatetime(s.startDate,'yyyy-MM-dd') >= parsedatetime(:targetDate,'yyyy-MM-dd') and s.eventName = :eventName")
    List<Schedule> findByAllDayNowAfter(@Param("targetDate") String targetDate, @Param("eventName") String eventName);

    @Query("SELECT s FROM Schedule s WHERE parsedatetime(s.startDate, 'yyyy-MM-dd') > parsedatetime(:targetDate, 'yyyy-MM-dd') and s.eventName = :eventName")
    List<Schedule> findByAllExceptNotAfter(@Param("targetDate") String targetDate, @Param("eventName") String eventName);

    @Query("select s from Schedule s where s.eventName = :eventName and s.userId = :userId")
    List<Schedule> findByEventName(@Param("eventName") String eventName, @Param("userId") String userId);

    /**
     * TODO
     *   h2 => parsedatetime() 사용
     */
    @Query("select s.amount from Schedule s where parsedatetime(s.startDate,'yyyy-MM-dd') >= parsedatetime(:targetDate,'yyyy-MM-dd')  and s.userId = :userId and s.priceType = :priceType")
    List<String> findByAmount(@Param("targetDate") String targetDate, @Param("userId") String userId, @Param("priceType") PriceType priceType);

    @Query("select s from Schedule s where PARSEDATETIME(s.startDate,'yyyy-MM-dd') >= PARSEDATETIME(:targetDate,'yyyy-MM-dd')  and s.userId = :userId and s.priceType = :priceType")
    List<Schedule> findByAmountDemo(@Param("targetDate") String targetDate, @Param("userId") String userId, @Param("priceType") PriceType priceType);

    @Query("select s.amount from Schedule s where s.userId = :userId and s.priceType = :priceType and s.startDate between :startDate and :endDate")
    List<String> findByAmountMonth(@Param("userId") String userId, @Param("priceType") PriceType priceType, @Param("startDate") String startDate, @Param("endDate") String endDate);


    @Query("select s from Schedule s where s.userId = :userId and s.priceType = :priceType and s.startDate between :startDate and :endDate")
    List<Schedule> findByStartDateAndeAndEndDatePriceType(@Param("userId") String userId, @Param("priceType") PriceType priceType, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("select s from Schedule s where s.userId = :userId and parsedatetime(s.startDate,'yyyy-MM-dd') >= parsedatetime(:targetDate,'yyyy-MM-dd')")
    List<Schedule> findByScheduleFromStartDate(@Param("userId") String userId, @Param("targetDate") String targetDate);

    @Query("select s.amount from Schedule s where s.userId = :userId and s.priceType = :priceType and s.isFixAmount = :fixed and s.startDate between :startDate and :endDate")
    List<String> findByFixedAmountMonth(@Param("userId") String userId, @Param("priceType") PriceType priceType, @Param("fixed") boolean fixed, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("select s from Schedule s where s.userId = :userId and s.startDate between :startDate and :endDate")
    List<Schedule> findByStartDateAndeEndDate(@Param("userId") String userId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}
