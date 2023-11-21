package project.fin_the_pen.model.schedule.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.finClient.core.util.ScheduleTypeFunc;
import project.fin_the_pen.model.schedule.dto.ScheduleDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.ScheduleManage;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.type.RepeatType;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScheduleRepository {
    private final CRUDScheduleRepository scheduleRepository;
    private final CRUDRegularScheduleRepository regularScheduleRepository;
    private final ManageRepository manageRepository;

    // TODO 1
    public Boolean registerSchedule(ScheduleDTO dto, PriceType priceType, RepeatType repeatType) {
        try {

            Schedule schedule = Schedule.builder()
                    .id(dto.getId())
                    .userId(dto.getUserId())
                    .eventName(dto.getEventName())
                    .category(dto.getCategory())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .startTime(dto.getStartTime())
                    .endTime(dto.getEndTime())
                    .allDay(dto.isAllDay())
                    .repeat(repeatType)
                    .period(dto.getPeriod())
                    .priceType(priceType)
                    .isExclude(dto.isExclude())
                    .importance(dto.getImportance())
                    .amount(dto.getAmount())
                    .isFixAmount(dto.isFixAmount())
                    .build();


            log.info(dto.getStartTime());
            log.info(dto.getAmount());
            scheduleRepository.save(schedule);
            manageSave(schedule);
            log.info(schedule.getUserId());

        } catch (Exception e) {
            return null;
        }
        return true;
    }


    /**
     *
     */
    public List<Schedule> findByContainsName(String name) {
        return scheduleRepository.findByContainsName(name);
    }

    /**
     * 전체 일정 조회 userId에 따라서
     *
     * @param id
     * @return
     */
    public List<Schedule> findAllSchedule(String id) {
        return scheduleRepository.findScheduleByUserId(id);
    }

    /**
     * 월별로 일정 조회
     * TODO !!!!!!!!!!!!!!!!!
     *
     * @param date
     * @return
     */
    public List<Schedule> findMonthSchedule(String date, String userId) {
        return scheduleRepository.findByMonthSchedule(date, userId);

    }

    public List<Schedule> findMonthSectionSchedule(String startDate, String endDate, String userId) {
        return scheduleRepository.findScheduleByDateContains(startDate, endDate, userId);
    }

    /**
     * 일정 하나만 조회인데 필요할지 안 필요할지....
     *
     * @param uuid
     * @return
     */
    /*public ScheduleResponseDTO findOneSchedule(String uuid) {
        Schedule findSchedule = getSingleSchedule(uuid);

        ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.builder()
                .id(findSchedule.getId())
                .eventName(findSchedule.getEventName())
                .alarm(findSchedule.isAlarm())
                .date(findSchedule.getDate())
                .startTime(findSchedule.getStartTime())
                .endTime(findSchedule.getEndTime())
                .repeatingCycle(findSchedule.getRepeatingCycle())
                .repeatDeadline(findSchedule.getRepeatDeadline())
                .repeatEndDate(findSchedule.getRepeatEndDate())
                .category(findSchedule.getCategory())
                .type(findSchedule.getPriceType())
                .expectedSpending(findSchedule.getExpectedSpending())
                .importance(findSchedule.getImportance())
                .exclusion(findSchedule.isExclusion())
                .build();

        return scheduleResponseDTO;
    }*/

    /**
     * 일정 수정, 일단 간단한 일정만
     */
    public Boolean modifySchedule(ScheduleDTO dto, PriceType priceType, RepeatType repeatType) {

        try {
            Optional<Schedule> optionalSchedule = Optional.of(getSingleSchedule(dto.getId())
                    .orElseThrow(() -> new RuntimeException("error")));

            Schedule findSchedule = optionalSchedule.get();
            findSchedule.setEventName(dto.getEventName());
            findSchedule.setCategory(dto.getCategory());
            findSchedule.setStartDate(dto.getStartDate());
            findSchedule.setEndDate(dto.getEndDate());
            findSchedule.setStartTime(dto.getStartTime());
            findSchedule.setEndTime(dto.getEndTime());
            findSchedule.setAllDay(dto.isAllDay());
            findSchedule.setRepeat(repeatType);
            findSchedule.setPeriod(dto.getPeriod());
            findSchedule.setPriceType(priceType);
            findSchedule.setExclude(dto.isExclude());
            findSchedule.setImportance(dto.getImportance());
            findSchedule.setAmount(dto.getAmount());
            findSchedule.setFixAmount(dto.isFixAmount());

            scheduleRepository.save(findSchedule);
            return true;

        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * callback method
     * enum type에 따라서 다르게 overriding
     *
     * @param dto
     * @param callBack
     */
    private void isType(ScheduleDTO dto, ScheduleTypeFunc callBack) {
        callBack.callbackMethod(dto);
    }

    public List<Schedule> findScheduleByCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
        return scheduleRepository.findScheduleByCategory(currentSession, categoryRequestDTO.getCategoryName());
    }

    /*public boolean deleteSchedule(String uuid) {
        Schedule singleSchedule = getSingleSchedule(uuid);
        try {
            scheduleRepository.delete(singleSchedule);

        } catch (Exception e) {
            return false;
        }
        return true;
    }*/

    private Optional<Schedule> getSingleSchedule(String uuid) {
        return scheduleRepository.findById(uuid);
    }

    private static void manageSave(Schedule schedule) {
        ScheduleManage manage = new ScheduleManage();
        manage.setDeleteFlag(false);
        manage.setSchedule(schedule);
        schedule.setScheduleManage(manage);
    }
}
