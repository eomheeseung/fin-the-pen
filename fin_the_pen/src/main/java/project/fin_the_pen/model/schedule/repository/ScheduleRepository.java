package project.fin_the_pen.model.schedule.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.TypeManage;
import project.fin_the_pen.model.schedule.entity.type.day.DayType;
import project.fin_the_pen.model.schedule.service.register.*;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScheduleRepository {
    private final CRUDScheduleRepository crudScheduleRepository;
    private final CRUDRegularScheduleRepository regularScheduleRepository;
    //    private final ManageRepository manageRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final RegisterNoneSchedule registerNoneSchedule;
    private final RegisterDaySchedule registerDaySchedule;
    private final RegisterWeekSchedule registerWeekSchedule;
    private final RegisterMonthSchedule registerMonthSchedule;
    private final RegisterYearSchedule registerYearSchedule;

    /**
     * 반복이 아닐 때 (단일일정)
     *
     * @param dto
     * @return
     */
    public Boolean registerNoneSchedule(ScheduleRequestDTO dto) {
        return registerNoneSchedule.registerSchedule(dto);
    }

    /**
     * "일" 단위 반복
     *
     * @return
     */
    public Boolean registerDaySchedule(ScheduleRequestDTO dto) {
        return registerDaySchedule.registerSchedule(dto);
    }

    /**
     * "주" 단위 반복
     *
     * @return
     */
    public boolean registerWeekSchedule(ScheduleRequestDTO dto) {
        return registerWeekSchedule.registerSchedule(dto);
    }

    /**
     * "월" 단위 반복
     */
    public Boolean registerMonthSchedule(ScheduleRequestDTO dto) {
        return registerMonthSchedule.registerSchedule(dto);
    }

    public Boolean registerYearSchedule(ScheduleRequestDTO dto) {
        return registerYearSchedule.registerSchedule(dto);
    }

    /**
     * 현재부터 이 이후의 일정들
     * <p>
     *  TODO 수정
     *   1. 현재부터 이 이후의 모든 일정들
     *   type1의 조건에 맞게 또 반복일정이면, registerXXX의 logic을 가져와서 사용해야 하는데..
     *
     * @param dto
     * @return
     */
    public Boolean modifyNowFromAfter(ModifyScheduleDTO dto, String repeatType) {
        Optional<Schedule> findModifySchedule = crudScheduleRepository.findByIdAndUserId(dto.getUserId(), Long.parseLong(dto.getScheduleId()));

        if (findModifySchedule.isPresent()) {
            String targetDate = findModifySchedule.get().getStartDate();
            List<Schedule> entities = crudScheduleRepository.findByAllDayNowAfter(targetDate);

            log.info("수정할 list 사이즈:{}", entities.size());

            int size = entities.size() - 1;

            if (repeatType.equals("day")) {
                DayType bindingDayType = new DayType();
                bindingDayType.setValue(dto.getRepeat().getDayTypeVO().getValue());

                TypeManage typeManage = TypeManage.builder()
                        .dayType(bindingDayType)
                        .build();

                int intervalDays = Integer.parseInt(dto.getRepeat().getDayTypeVO().getValue());
                LocalDate criteriaDate = formatDate(targetDate);
                int endRepeat = 50;

                if (dto.getPeriod().isRepeatAgain()) {
                    for (int i = 0; i < endRepeat; i++) {
                        Schedule schedule = Schedule.builder()
                                .userId(dto.getUserId())
                                .eventName(dto.getEventName())
                                .category(dto.getCategory())
                                .startDate(criteriaDate.toString())
                                .endDate(criteriaDate.toString())
                                .startTime(dto.getStartTime())
                                .endTime(dto.getEndTime())
                                .isAllDay(dto.isAllDay())
                                .repeat(typeManage)
                                .isExclude(dto.isExclude())
                                .importance(dto.getImportance())
                                .amount(dto.getAmount())
                                .isFixAmount(dto.isFixAmount())
                                .period(createPeriodType(() -> PeriodType.builder()
                                        .isRepeatAgain(true)
                                        .repeatNumberOfTime("0")
                                        .repeatEndLine(null)
                                        .build()))
                                .priceType(judgmentPriceType(() -> dto.getPriceType().equals(PriceType.Plus) ? PriceType.Plus : PriceType.Minus))
                                .build();

                        if (i <= size) {
                            // entities 리스트에 엔터티가 이미 존재할 때
                            Schedule existingSchedule = entities.get(i);

                            // TODO (복사문제, 수정이 안됨.)
                            BeanUtils.copyProperties(schedule, existingSchedule);
                            log.info(existingSchedule.getStartDate());
                            log.info(existingSchedule.getEventName());
                            crudScheduleRepository.save(existingSchedule);
                            log.info("수정 성공");
                        } else {
                            // entities 리스트를 모두 순회한 경우
                            log.info("나머지 저장");
                            log.info(schedule.getStartDate());
                            crudScheduleRepository.save(schedule);
                        }
                        criteriaDate = criteriaDate.plusDays(intervalDays);
                    }
                }
            }
        }
                    /*if (endRepeat < entities.size()) {
                        for (int j = endRepeat; j < entities.size(); j++) {
                            crudScheduleRepository.delete(entities.get(j));
                        }
                    }*/
//                } else if (!dto.getPeriod().getRepeatNumberOfTime().equals("0")) {
//                    int repeatNumberOfTime = Integer.parseInt(dto.getPeriod().getRepeatNumberOfTime());
//
//                    for (int i = 0; i < repeatNumberOfTime; i++) {
//                        Schedule schedule = Schedule.builder()
//                                .userId(dto.getUserId())
//                                .eventName(dto.getEventName())
//                                .category(dto.getCategory())
//                                .startDate(criteriaDate.toString())  // 수정된 부분
//                                .endDate(criteriaDate.toString())
//                                .startTime(dto.getStartTime())
//                                .endTime(dto.getEndTime())
//                                .isAllDay(dto.isAllDay())
//                                .repeat(typeManage)
//                                .isExclude(dto.isExclude())
//                                .importance(dto.getImportance())
//                                .amount(dto.getAmount())
//                                .isFixAmount(dto.isFixAmount())
//                                .period(createPeriodType(() -> {
//                                    return PeriodType.builder()
//                                            .isRepeatAgain(false)
//                                            .repeatEndLine(null)
//                                            .build();
//                                }))
//                                .priceType(judgmentPriceType(() -> {
//                                    if (dto.getPriceType().equals(PriceType.Plus)) {
//                                        return PriceType.Plus;
//                                    } else return PriceType.Minus;
//                                }))
//                                .build();
//
//                        if (i > entities.size()) {
//                            crudScheduleRepository.save(schedule);
//                        } else if (k <= entities.size()) {
//                            BeanUtils.copyProperties(schedule, entities.get(k));
//                            k++;
//                            crudScheduleRepository.save(schedule);
//                        }
//                        criteriaDate = criteriaDate.plusDays(intervalDays);
//                    }
//
//                    if (repeatNumberOfTime < entities.size()) {
//                        for (int j = endRepeat; j < entities.size(); j++) {
//                            crudScheduleRepository.delete(entities.get(j));
//                        }
//                    }
//                } else if (dto.getPeriod().getRepeatEndLine() != null) {
//                    LocalDate endLine = formatDate(dto.getPeriod().getRepeatEndLine());
//
//                    while (!criteriaDate.isAfter(endLine)) {
//                        Schedule schedule = Schedule.builder()
//                                .userId(dto.getUserId())
//                                .eventName(dto.getEventName())
//                                .category(dto.getCategory())
//                                .startDate(criteriaDate.toString())  // 수정된 부분
//                                .endDate(criteriaDate.toString())
//                                .startTime(dto.getStartTime())
//                                .endTime(dto.getEndTime())
//                                .isAllDay(dto.isAllDay())
//                                .repeat(typeManage)
//                                .isExclude(dto.isExclude())
//                                .importance(dto.getImportance())
//                                .amount(dto.getAmount())
//                                .isFixAmount(dto.isFixAmount())
//                                .period(createPeriodType(() -> {
//                                    return PeriodType.builder()
//                                            .isRepeatAgain(false)
//                                            .repeatEndLine(endLine.toString())
//                                            .build();
//                                }))
//                                .priceType(judgmentPriceType(() -> {
//                                    if (dto.getPriceType().equals(PriceType.Plus)) {
//                                        return PriceType.Plus;
//                                    } else return PriceType.Minus;
//                                }))
//                                .build();
//
//                        if (k < entities.size()) {
//                            BeanUtils.copyProperties(schedule, entities.get(k));
//                            crudScheduleRepository.save(schedule);
//                            k++;
//                        } else {
//                            crudScheduleRepository.save(schedule);
//                        }
//                        criteriaDate = criteriaDate.plusDays(intervalDays);
//                    }
//
//                    if (k < entities.size()) {
//                        for (int i = k; i < entities.size(); i++) {
//                            crudScheduleRepository.delete(entities.get(k));
//                        }
//                    }
//                }
//            }
//
//        } else {
//            throw new RuntimeException();
//        }

        return true;
    }

    @NotNull
    private LocalDate formatDate(String convertDate) {
        return LocalDate.parse(convertDate, formatter);
    }


    /**
     *
     */
    public List<Schedule> findByContainsName(String name) {
        return crudScheduleRepository.findByContainsName(name);
    }

    private PeriodType createPeriodType(Supplier<PeriodType> supplier) {
        return supplier.get();
    }

    /**
     * 전체 일정 조회 userId에 따라서
     *
     * @return
     */
    public List<Schedule> findAllSchedule(String userId) {
        return crudScheduleRepository.findByUserId(userId);
    }

    /**
     * 월별로 일정 조회
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


    public List<Schedule> findScheduleByCategory(CategoryRequestDTO categoryRequestDTO, String
            currentSession) {
        return crudScheduleRepository.findScheduleByCategory(currentSession, categoryRequestDTO.getCategoryName());
    }

    private PriceType judgmentPriceType(Supplier<PriceType> supplier) {
        return supplier.get();
    }

    // 중복되는 일정이 등록되는지 검사하는 method
    private boolean isDuplicatedSaveSchedule(ScheduleRequestDTO dto) {
        List<Schedule> allSchedule = findAllSchedule(dto.getUserId());

        return allSchedule.stream().noneMatch(it ->
                it.getUserId().equals(dto.getUserId()) &&
                        it.getEventName().equals(dto.getEventName()) &&
                        it.getCategory().equals(dto.getCategory()) &&
                        it.getStartDate().equals(dto.getStartDate()) &&
                        it.getEndDate().equals(dto.getEndDate()) &&
                        it.getStartTime().equals(dto.getStartTime()) &&
                        it.getEndTime().equals(dto.getEndTime()));
    }


}
