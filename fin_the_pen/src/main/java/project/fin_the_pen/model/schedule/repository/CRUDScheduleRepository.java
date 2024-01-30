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
public interface CRUDScheduleRepository extends JpaRepository<Schedule, Long> {
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


    @Query("select s from Schedule s where s.userId = :userId and s.category LIKE CONCAT('%', :categoryName, '%')")
    List<Schedule> findScheduleByCategory(@Param("userId") String userSessionId, @Param("categoryName") String categoryName);

    @Query("select s from Schedule s where s.userId = :userId and s.id = :scheduleId")
    Optional<Schedule> findByIdAndUserId(@Param("userId") String userId, @Param("scheduleId") Long scheduleId);

    /*@Query("SELECT s FROM Schedule s WHERE TO_DATE(s.startDate, 'yyyy-MM-dd') >= TO_DATE(:targetDate, 'yyyy-MM-dd') and s.eventName = :eventName")
    List<Schedule> findByAllDayNowAfter(@Param("targetDate") String targetDate, @Param("eventName") String eventName);*/

    @Query("SELECT s FROM Schedule s WHERE parsedatetime(s.startDate,'yyyy-MM-dd') >= parsedatetime(:targetDate,'yyyy-MM-dd') and s.eventName = :eventName")
    List<Schedule> findByAllDayNowAfter(@Param("targetDate") String targetDate, @Param("eventName") String eventName);

    @Query("SELECT s FROM Schedule s WHERE TO_DATE(s.startDate, 'yyyy-MM-dd') > TO_DATE(:targetDate, 'yyyy-MM-dd') and s.eventName = :eventName")
    List<Schedule> findByAllExceptNotAfter(@Param("targetDate") String targetDate, @Param("eventName") String eventName);

    @Query("select s from Schedule s where s.eventName = :eventName and s.userId = :userId")
    List<Schedule> findByEventName(@Param("eventName") String eventName, @Param("eventName") String userId);

    /**
     * TODO
     *   priceType이 entity에서 어떤 타입인지
     *   1. priceType의 sort number(long)인지, 단순 enum type인지
     *   2. stiring
     *
     * @param date
     * @param userId
     * @param priceType
     * @return
     */
    @Query("select s.amount from Schedule s where TO_DATE(s.startDate, 'yyyy-MM-dd') <= TO_DATE(:date) and s.userId = :userId and s.priceType = :priceType")
    List<String> findByAmount(@Param("date") String date, @Param("userId") String userId, @Param("priceType") PriceType priceType);


    @Query("select s.amount from Schedule s where s.userId = :userId and s.priceType = :priceType and s.startDate between :startDate and :endDate")
    List<String> findByAmountMonth(@Param("userId") String userId, @Param("priceType") PriceType priceType, @Param("startDate") String startDate,@Param("endDate") String endDate);


//    @Query("select s.amount from Schedule s where s.userId = :userId and s.priceType = :PriceType and s.startDate between :stDay and :date")
//    List<String> findByFixedAmountMonth(@Param("userId") String userId, @Param("priceType") PriceType priceType, @Param("type") TypeManage typeManage, @Param("1stDay") String startDate, @Param("date") String endDate);
}
