package project.fin_the_pen.model.schedule.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.schedule.dto.DeleteScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.service.modify.ModifyDaySchedule;
import project.fin_the_pen.model.schedule.service.modify.ModifyMonthSchedule;
import project.fin_the_pen.model.schedule.service.modify.ModifyWeekSchedule;
import project.fin_the_pen.model.schedule.service.modify.ModifyYearSchedule;
import project.fin_the_pen.model.schedule.service.register.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ScheduleRepository {
    private final CRUDScheduleRepository crudScheduleRepository;
    //    private final CRUDRegularScheduleRepository regularScheduleRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final RegisterNoneSchedule registerNoneSchedule;
    private final RegisterDaySchedule registerDaySchedule;
    private final RegisterWeekSchedule registerWeekSchedule;
    private final RegisterMonthSchedule registerMonthSchedule;
    private final RegisterYearSchedule registerYearSchedule;

    private final ModifyYearSchedule modifyYearSchedule;
    private final ModifyMonthSchedule modifyMonthSchedule;
    private final ModifyWeekSchedule modifyWeekSchedule;
    private final ModifyDaySchedule modifyDaySchedule;


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

    /**
     * "년도" 단위 반복
     */
    public Boolean registerYearSchedule(ScheduleRequestDTO dto) {
        return registerYearSchedule.registerSchedule(dto);
    }

    /*
    TODO test, query할때 userId도 같이 넘겨서 @Param에 넣어야 할 것 같음. 아니면 stream filter를 사용하던가.
        @Query("SELECT s FROM Schedule s WHERE TO_DATE(s.startDate, 'yyyy-MM-dd') > TO_DATE(:targetDate, 'yyyy-MM-dd') and s.eventName = :eventName")
        에서 and userId와 같이...
     */

    /**
     * 현재부터 이 이후의 일정들
     * 1. week의 경우
     * => 1.21일이 저장하려는 요일의 조건에 해당되지 않은 경우 1.21은 삭제됨
     * <p>
     * repeatType에는 day, week, month, year이 들어옴.
     *
     * @param dto
     * @return
     */
    public Boolean modifyNowFromAfter(ModifyScheduleDTO dto, String repeatType) {
        Optional<Schedule> findModifySchedule =
                crudScheduleRepository.findByIdAndUserId(dto.getUserId(), Long.parseLong(dto.getScheduleId()));

        if (findModifySchedule.isPresent()) {
            String targetDate = findModifySchedule.get().getStartDate();
            log.info("삭제할 시점의 date:{}", targetDate);
            List<Schedule> entities = crudScheduleRepository.findByAllDayNowAfter(targetDate, findModifySchedule.get().getEventName());

            log.info("DB에서 가져온 id:{}", dto.getScheduleId());
            log.info("수정할 list 사이즈:{}", entities.size());

            crudScheduleRepository.deleteAll(entities);

            log.info("수정된 list 사이즈:{}", entities.size());

            switch (repeatType) {
                case "day":
                    modifyDaySchedule.modifySchedule(dto);
                    break;
                case "week":
                    modifyWeekSchedule.modifySchedule(dto);
                    break;
                case "month":
                    modifyMonthSchedule.modifySchedule(dto);
                    break;
                case "year":
                    modifyYearSchedule.modifySchedule(dto);
                    break;
            }
        }
        return true;
    }

    /**
     * 현재 선택한 일정 이후의 일정들만 수정
     *
     * @param dto
     * @param repeatType
     * @return
     */
    public Boolean modifyExceptNowAfter(ModifyScheduleDTO dto, String repeatType) {
        Optional<Schedule> findModifySchedule =
                crudScheduleRepository.findByIdAndUserId(dto.getUserId(), Long.parseLong(dto.getScheduleId()));

        if (findModifySchedule.isPresent()) {
            String targetDate = findModifySchedule.get().getStartDate();
            log.info("삭제할 시점의 date:{}", targetDate);
            List<Schedule> entities = crudScheduleRepository.findByAllExceptNotAfter(targetDate, findModifySchedule.get().getEventName());

            log.info("DB에서 가져온 id:{}", dto.getScheduleId());
            log.info("수정할 list 사이즈:{}", entities.size());

            crudScheduleRepository.deleteAll(entities);

            log.info("수정된 list 사이즈:{}", entities.size());

            switch (repeatType) {
                case "none":

                case "day":
                    modifyDaySchedule.modifySchedule(dto);
                    break;
                case "week":
                    modifyWeekSchedule.modifySchedule(dto);
                    break;
                case "month":
                    modifyMonthSchedule.modifySchedule(dto);
                    break;
                case "year":
                    modifyYearSchedule.modifySchedule(dto);
                    break;
            }
        }
        return true;
    }

    public Boolean modifyAllSchedule(ModifyScheduleDTO dto, String repeatType) {
        Optional<Schedule> findModifySchedule =
                crudScheduleRepository.findByIdAndUserId(dto.getUserId(), Long.parseLong(dto.getScheduleId()));

        if (findModifySchedule.isPresent()) {
            Schedule target = findModifySchedule.get();

            log.info("전체 수정 : {}", target.getId());
            log.info("전체 수정할 이름 : {}", target.getEventName());

            List<Schedule> entities =
                    crudScheduleRepository.findByEventName(target.getEventName(), target.getUserId());

            log.info("list size:{}", entities.size());

            crudScheduleRepository.deleteAll(entities);

            log.info("삭제 후 :{}", entities.size());

            log.info("수정할 list 사이즈:{}", entities.size());

            switch (repeatType) {
                case "day":
                    modifyDaySchedule.modifySchedule(dto);
                    break;
                case "week":
                    modifyWeekSchedule.modifySchedule(dto);
                    break;
                case "month":
                    modifyMonthSchedule.modifySchedule(dto);
                    break;
                case "year":
                    modifyYearSchedule.modifySchedule(dto);
                    break;
            }
        }
        return true;
    }

    public Boolean deleteNowFromAfter(DeleteScheduleDTO dto) {
        Optional<Schedule> findSchedule = crudScheduleRepository.findByIdAndUserId(dto.getUserId(), Long.parseLong(dto.getScheduleId()));

        if (findSchedule.isPresent()) {
            String targetDate = findSchedule.get().getStartDate();

            try {
                List<Schedule> entities = crudScheduleRepository.findByAllDayNowAfter(targetDate, findSchedule.get().getEventName());
                log.info("수정할 list 사이즈:{}", entities.size());

                crudScheduleRepository.deleteAll(entities);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public Boolean deleteExceptNotAfter(DeleteScheduleDTO dto) {
        Optional<Schedule> findSchedule = crudScheduleRepository.findByIdAndUserId(dto.getUserId(), Long.parseLong(dto.getScheduleId()));

        if (findSchedule.isPresent()) {
            String targetDate = findSchedule.get().getStartDate();

            try {
                List<Schedule> entities = crudScheduleRepository.findByAllExceptNotAfter(targetDate, findSchedule.get().getEventName());
                log.info("수정할 list 사이즈:{}", entities.size());

                crudScheduleRepository.deleteAll(entities);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public Boolean deleteAllSchedule(DeleteScheduleDTO dto) {
        Optional<Schedule> findSchedule = crudScheduleRepository.findByIdAndUserId(dto.getUserId(), Long.parseLong(dto.getScheduleId()));

        if (findSchedule.isPresent()) {
            try {
                List<Schedule> entities = crudScheduleRepository.findByEventName(findSchedule.get().getEventName(), findSchedule.get().getUserId());
                log.info("수정할 list 사이즈:{}", entities.size());

                crudScheduleRepository.deleteAll(entities);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
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
