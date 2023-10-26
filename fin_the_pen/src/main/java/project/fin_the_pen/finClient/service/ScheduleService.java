package project.fin_the_pen.finClient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.data.schedule.Schedule;
import project.fin_the_pen.finClient.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.finClient.data.schedule.ScheduleResponseDTO;
import project.fin_the_pen.finClient.data.schedule.category.CategoryRequestDTO;
import project.fin_the_pen.finClient.data.schedule.type.PriceType;
import project.fin_the_pen.finClient.data.schedule.type.RegularType;
import project.fin_the_pen.finClient.repository.ScheduleRepository;
import project.fin_the_pen.finClient.util.*;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    ScheduleStrategy monthStrategy = new MonthStrategy();
    ScheduleStrategy sectionStrategy = new SectionStrategy();
    ScheduleStrategy categoryStrategy = new CategoryStrategy();

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


    public JSONArray findAllSchedule(String id) {
        List<Schedule> scheduleList = scheduleRepository.findAllSchedule(id);
        ScheduleStrategy allStrategy = new AllMonthStrategy();
        return allStrategy.execute(scheduleList);
    }

    public boolean modifySchedule(ScheduleRequestDTO scheduleRequestDTO) {
        if (!scheduleRepository.modifySchedule(scheduleRequestDTO)) {
            return false;
        }
        return true;
    }

    /*public JSONArray findScheduleCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
        List<Schedule> result = scheduleRepository.findScheduleByCategory(categoryRequestDTO, currentSession);
        return getJsonArrayBySchedule(result, new JSONArray());
    }*/

    public JSONArray findScheduleCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
        List<Schedule> result = scheduleRepository.findScheduleByCategory(categoryRequestDTO, currentSession);
        return new CategoryStrategy().execute(result);
    }


    /*public JSONArray findMonthSchedule(String date, String userId) {
        List<Schedule> byMonthSchedule = scheduleRepository.findMonthSchedule(date, userId);
        return getJsonArrayBySchedule(byMonthSchedule, new JSONArray());
    }
*/
    public JSONArray findMonthSchedule(String date, String userId) {
        List<Schedule> byMonthSchedule = scheduleRepository.findMonthSchedule(date, userId);
        return new MonthStrategy().execute(byMonthSchedule);
    }


   /* public JSONArray findMonthSectionSchedule(String startDate, String endDate, String userId) {
        List<Schedule> byMonthSchedule = scheduleRepository.findMonthSectionSchedule(startDate, endDate, userId);
        return getJsonArrayBySchedule(byMonthSchedule, new JSONArray());
    }*/

    public JSONArray findMonthSectionSchedule(String startDate, String endDate, String userId) {
        List<Schedule> byMonthSchedule = scheduleRepository.findMonthSectionSchedule(startDate, endDate, userId);
        return new SectionStrategy().execute(byMonthSchedule);
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
        List<Schedule> byContainsNameList = scheduleRepository.findByContainsName(name);
        ScheduleStrategy containStrategy = new ContainStrategy();
        return containStrategy.execute(byContainsNameList);

    }

    /**
     * callback
     *
     * @param scheduleRequestDTO
     * @param callback
     */
    private void isType(ScheduleRequestDTO scheduleRequestDTO, ScheduleTypeFunc callback) {
        callback.callbackMethod(scheduleRequestDTO);
    }

    /**
     * 리턴 받은 list를 get하는 함수
     *
     * @param scheduleList
     * @param jsonArray
     * @return
     */
    /*private JSONArray getJsonArrayBySchedule(List<Schedule> scheduleList, JSONArray jsonArray) {
        return getJsonArray(scheduleList, jsonArray);
    }*/
}
