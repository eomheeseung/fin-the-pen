package project.fin_the_pen.finClient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.finClient.data.schedule.ScheduleResponseDTO;
import project.fin_the_pen.finClient.data.schedule.type.RegularType;
import project.fin_the_pen.finClient.util.ScheduleTypeFunc;
import project.fin_the_pen.finClient.data.schedule.category.CategoryRequestDTO;
import project.fin_the_pen.finClient.data.schedule.type.PriceType;
import project.fin_the_pen.finClient.repository.ScheduleRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public Boolean registerSchedule(ScheduleRequestDTO scheduleRequestDTO) {
        // 일정이 정기적인 일정인지 아닌지
        try {
            if (scheduleRequestDTO.getRepeatingCycle().equals("없음")) {
                if (scheduleRequestDTO.getPriceType().equals("+")) {
                    isType(scheduleRequestDTO, (dto) ->
                            scheduleRepository.registerSchedule(scheduleRequestDTO, PriceType.Plus, RegularType.None));
                } else {
                    isType(scheduleRequestDTO, (dto) ->
                            scheduleRepository.registerSchedule(scheduleRequestDTO, PriceType.Minus, RegularType.None));
                }
            } else {
                // +가 정기 입금, -가 출금
                if (scheduleRequestDTO.getPriceType().equals("+")) {
                    isType(scheduleRequestDTO, (dto) ->
                            scheduleRepository.registerSchedule(scheduleRequestDTO, PriceType.Plus, RegularType.Deposit));
                } else {
                    isType(scheduleRequestDTO, (dto) ->
                            scheduleRepository.registerSchedule(scheduleRequestDTO, PriceType.Minus, RegularType.Withdrawal));
                }
            }

        } catch (Exception e) {
            return null;
        }
        return true;
    }

    /**
     * template callback
     *
     * @param scheduleRequestDTO
     * @param callback
     */
    private void isType(ScheduleRequestDTO scheduleRequestDTO, ScheduleTypeFunc callback) {
        callback.callbackMethod(scheduleRequestDTO);
    }

    public JSONArray findAllSchedule(String id) {
        return scheduleRepository.findAllSchedule(id);
    }

    public boolean modifySchedule(ScheduleRequestDTO scheduleRequestDTO) {
        if (!scheduleRepository.modifySchedule(scheduleRequestDTO)) {
            return false;
        }
        return true;
    }

    public JSONArray findScheduleCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
        return scheduleRepository.findScheduleByCategory(categoryRequestDTO, currentSession);
    }

    public JSONArray findMonthSchedule(String date, String userId) {
        return scheduleRepository.findMonthSchedule(date, userId);
    }

    public JSONArray findMonthSectionSchedule(String startDate, String endDate, String userId) {
        return scheduleRepository.findMonthSectionSchedule(startDate, endDate, userId);
    }

    public boolean deleteSchedule(String uuid) {
        try {
            scheduleRepository.deleteSchedule(uuid);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public ScheduleResponseDTO findOne(String uuid) {
        return scheduleRepository.findOneSchedule(uuid);
    }

    public JSONArray findByContainsName(String name) {
        return scheduleRepository.findByContainsName(name);
    }
}
