package project.fin_the_pen.model.schedule.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.finClient.core.util.ScheduleTypeFunc;
import project.fin_the_pen.model.schedule.dto.ScheduleDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.embedded.RepeatType;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScheduleRepository {
    private final CRUDScheduleRepository crudScheduleRepository;
    private final CRUDRegularScheduleRepository regularScheduleRepository;
//    private final ManageRepository manageRepository;

    // TODO 1
    public Boolean registerSchedule(ScheduleDTO dto, PriceType priceType, String token) {
        try {
            Certain result = getCertain(dto);

            List<Schedule> allSchedule = findAllSchedule(token);

            boolean isDifferent = allSchedule.stream().noneMatch(it ->
                    it.getUserId().equals(dto.getUserId()) &&
                            it.getEventName().equals(dto.getEventName()) &&
                            it.getCategory().equals(dto.getCategory()) &&
                            it.getStartDate().equals(dto.getStartDate()) &&
                            it.getEndDate().equals(dto.getEndDate()) &&
                            it.getStartTime().equals(dto.getStartTime()) &&
                            it.getEndTime().equals(dto.getEndTime()));

            if (isDifferent) {
                Schedule schedule = Schedule.builder()
                        .token(token)
                        .userId(dto.getUserId())
                        .eventName(dto.getEventName())
                        .category(dto.getCategory())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .startTime(dto.getStartTime())
                        .endTime(dto.getEndTime())
                        .allDay(dto.isAllDay())
                        .repeat(result.repeatType)
                        .period(result.periodType)
                        .priceType(priceType)
                        .isExclude(dto.isExclude())
                        .importance(dto.getImportance())
                        .amount(dto.getAmount())
                        .isFixAmount(dto.isFixAmount())
                        .build();


                log.info(dto.getUserId());
                log.info(dto.getAmount());
                crudScheduleRepository.save(schedule);
//            manageSave(schedule);
                log.info(schedule.getUserId());
            } else throw new DuplicatedScheduleException("중복된 일정 등록입니다.");
        } catch (RuntimeException e) {
            return null;
        }
        return true;
    }


    /**
     *
     */
    public List<Schedule> findByContainsName(String name) {
        return crudScheduleRepository.findByContainsName(name);
    }

    /**
     * 전체 일정 조회 userId에 따라서
     *
     * @return
     */
    public List<Schedule> findAllSchedule(String token) {
        return crudScheduleRepository.findByToken(token);
    }

    /**
     * 월별로 일정 조회
     * TODO !!!!!!!!!!!!!!!!!
     *
     * @param date
     * @return
     */
    public List<Schedule> findMonthSchedule(String date, String userId) {
        return crudScheduleRepository.findByMonthSchedule(date, userId);

    }

    public List<Schedule> findMonthSectionSchedule(String startDate, String endDate, String userId) {
        return crudScheduleRepository.findScheduleByDateContains(startDate, endDate, userId);
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
     * 일정 수정
     * TODO
     *  만약, "매주 반복 + 2024.01.01"로 생성했는데
     *  수정에서 "매월 반복 + 2024.02.01"로 수정되면, 매주 반복을 없애고 매월 반복으로 바꿔야 함...
     */
    /*public Boolean modifySchedule(ScheduleDTO dto, PriceType priceType) {

        try {
            Optional<Schedule> optionalSchedule = Optional.of(getSingleSchedule(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("error")));

            Certain result = getCertain(dto);

            Schedule findSchedule = optionalSchedule.get();
            findSchedule.setEventName(dto.getEventName());
            findSchedule.setCategory(dto.getCategory());
            findSchedule.setStartDate(dto.getStartDate());
            findSchedule.setEndDate(dto.getEndDate());
            findSchedule.setStartTime(dto.getStartTime());
            findSchedule.setEndTime(dto.getEndTime());
            findSchedule.setAllDay(dto.isAllDay());
            findSchedule.setRepeat(result.repeatType);
            findSchedule.setPeriod(result.periodType);
            findSchedule.setPriceType(priceType);
            findSchedule.setExclude(dto.isExclude());
            findSchedule.setImportance(dto.getImportance());
            findSchedule.setAmount(dto.getAmount());
            findSchedule.setFixAmount(dto.isFixAmount());

            crudScheduleRepository.save(findSchedule);
            return true;

        } catch (RuntimeException e) {
            return false;
        }
    }*/


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
        return crudScheduleRepository.findScheduleByCategory(currentSession, categoryRequestDTO.getCategoryName());
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

//    private Optional<Schedule> getSingleSchedule(String uuid) {
//        return crudScheduleRepository.findById(uuid);
//    }

    /*private static void manageSave(Schedule schedule) {
        ScheduleManage manage = new ScheduleManage();
        manage.setDeleteFlag(false);
        manage.setSchedule(schedule);
        schedule.setScheduleManage(manage);
    }*/

    @NotNull
    private static Certain getCertain(ScheduleDTO dto) {
        RepeatType repeatType = new RepeatType();
        String repeatTypeValue = dto.getRepeat();

        switch (repeatTypeValue) {
            case "day":
                repeatType.setDay("day");
                break;
            case "everyWeek":
                repeatType.setEveryYear("everyWeek");
                break;
            case "Monthly":
                repeatType.setMonthly("Monthly");
                break;
            case "everyYear":
                repeatType.setEveryYear("everyYear");
                break;
        }

        PeriodType periodType = new PeriodType();

        String periodTypeValue = dto.getPeriod();

        switch (periodTypeValue) {
            case "keepRepeat":
                periodType.setKeepRepeat("keepRepeat");
                break;
            case "certain":
                periodType.setCertain("certain");
                break;
            case "endDate":
                periodType.setRepeatEndDate("endDate");
                break;
        }
        return new Certain(repeatType, periodType);
    }

    private static class Certain {
        public final RepeatType repeatType;
        public final PeriodType periodType;

        public Certain(RepeatType repeatType, PeriodType periodType) {
            this.repeatType = repeatType;
            this.periodType = periodType;
        }
    }
}
