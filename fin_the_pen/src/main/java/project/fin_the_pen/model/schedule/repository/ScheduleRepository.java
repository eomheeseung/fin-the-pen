package project.fin_the_pen.model.schedule.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.ScheduleManage;
import project.fin_the_pen.finClient.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.type.RepeatType;
import project.fin_the_pen.finClient.schedule.dto.AssetRequestDTO;
import project.fin_the_pen.finClient.schedule.dto.ScheduleAllDTO;
import project.fin_the_pen.finClient.schedule.dto.ScheduleDTO;
import project.fin_the_pen.finClient.core.util.ScheduleTypeFunc;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScheduleRepository {
    private final CRUDScheduleRepository scheduleRepository;
    private final CRUDRegularScheduleRepository regularScheduleRepository;
    private final ManageRepository manageRepository;

    // TODO 1
    public Boolean registerSchedule(ScheduleAllDTO dto, PriceType priceType, RepeatType repeatType) {
        try {
            ScheduleDTO scheduleDTO = dto.getScheduleDTO();
            AssetRequestDTO assetDto = dto.getAssetDto();

            Schedule schedule = Schedule.builder()
                    .id(dto.getScheduleDTO().getId())
                    .userId(scheduleDTO.getUserId())
                    .eventName(scheduleDTO.getEventName())
                    .category(scheduleDTO.getCategory())
                    .startDate(scheduleDTO.getStartDate())
                    .endDate(scheduleDTO.getEndDate())
                    .startTime(scheduleDTO.getStartTime())
                    .endTime(scheduleDTO.getEndTime())
                    .allDay(scheduleDTO.isAllDay())
                    .repeat(repeatType)
                    .period(scheduleDTO.getPeriod())
                    .priceType(priceType)
                    .isExclude(assetDto.isExclude())
                    .importance(assetDto.getImportance())
                    .amount(assetDto.getAmount())
                    .isFixAmount(assetDto.isFixAmount())
                    .build();


            log.info(scheduleDTO.getStartTime());
            log.info(assetDto.getAmount());
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
     * TODO 2
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

    /*public boolean modifySchedule(ScheduleDTO scheduleRequestDTO) {
        Schedule findSchedule = getSingleSchedule(scheduleRequestDTO.getId());

        if (findSchedule == null) {
            return false;
        } else {
            // 정기 일정인 경우
            if (!findSchedule.getRegularType().equals(RegularType.None)) {
                RegularType regularType = null;
                PriceType priceType = null;

                if (!scheduleRequestDTO.getRepeatingCycle().equals("없음")) {
                    if (scheduleRequestDTO.getPriceType().equals("+")) {
                        priceType = PriceType.Plus;
                        regularType = RegularType.Deposit;
                    } else {
                        priceType = PriceType.Minus;
                        regularType = RegularType.Withdrawal;
                    }
                }


                RegularSchedule regularSchedule = RegularSchedule.builder().userId(findSchedule.getUserId())
                        .scheduleId(findSchedule.getId())
                        .eventName(scheduleRequestDTO.getEventName())
                        .alarm(scheduleRequestDTO.isAlarm())
                        .date(scheduleRequestDTO.getDate())
                        .startTime(scheduleRequestDTO.getStartTime())
                        .endTime(scheduleRequestDTO.getEndTime())
                        .repeatingCycle(scheduleRequestDTO.getRepeatingCycle())
                        .repeatDeadline(scheduleRequestDTO.getRepeatDeadLine())
                        .repeatEndDate(scheduleRequestDTO.getRepeatEndDate())
                        .category(scheduleRequestDTO.getCategory())
                        .expectedSpending(scheduleRequestDTO.getExpectedSpending())
                        .importance(scheduleRequestDTO.getImportance())
                        .exclusion(scheduleRequestDTO.isExclusion())
                        .regularType(regularType)
                        .priceType(priceType).build();

                regularScheduleRepository.save(regularSchedule);
            } else {
                // 정기 일정이 아닌경우
                try {
                    findSchedule.setEventName(scheduleRequestDTO.getEventName());
                    findSchedule.setAlarm(scheduleRequestDTO.isAlarm());
                    findSchedule.setDate(scheduleRequestDTO.getDate());
                    findSchedule.setStartTime(scheduleRequestDTO.getStartTime());
                    findSchedule.setEndTime(scheduleRequestDTO.getEndTime());
                    findSchedule.setRepeatingCycle(scheduleRequestDTO.getRepeatingCycle());
                    findSchedule.setRepeatDeadline(scheduleRequestDTO.getRepeatDeadLine());
                    findSchedule.setRepeatEndDate(scheduleRequestDTO.getRepeatEndDate());
                    findSchedule.setCategory(scheduleRequestDTO.getCategory());

                    isType(scheduleRequestDTO, dto -> {
                        if (dto.getPriceType().equals("+")) {
                            findSchedule.setPriceType(PriceType.Plus);
                        } else {
                            findSchedule.setPriceType(PriceType.Minus);
                        }
                    });


                    findSchedule.setExpectedSpending(scheduleRequestDTO.getExpectedSpending());
                    findSchedule.setImportance(scheduleRequestDTO.getImportance());
                    findSchedule.setExclusion(scheduleRequestDTO.isExclusion());
//                entityManager.merge(findSchedule);
                    repository.save(findSchedule);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }*/

    /**
     * callback method
     * enum type에 따라서 다르게 overriding
     *
     * @param dto
     * @param callBack
     */
    private void isType(ScheduleAllDTO dto, ScheduleTypeFunc callBack) {
        callBack.callbackMethod(dto);
    }

    public List<Schedule> findScheduleByCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
        /*List<Schedule> resultList =
                entityManager.createQuery("select s from Schedule s where s.userId= :userId and s.category = :categoryName", Schedule.class)
                        .setParameter("userId", currentSession)
                        .setParameter("categoryName", categoryRequestDTO.getCategoryName())
                        .getResultList();*/
        return scheduleRepository.findScheduleByCategory(currentSession, categoryRequestDTO.getCategoryName());
    }

    public boolean deleteSchedule(String uuid) {
        Schedule singleSchedule = getSingleSchedule(uuid);
        try {
            scheduleRepository.delete(singleSchedule);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Schedule getSingleSchedule(String uuid) {
        return scheduleRepository.findById(uuid).get();
    }

    private static void manageSave(Schedule schedule) {
        ScheduleManage manage = new ScheduleManage();
        manage.setDeleteFlag(false);
        manage.setSchedule(schedule);
        schedule.setScheduleManage(manage);
    }
}
