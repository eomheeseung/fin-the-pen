package project.fin_the_pen.finClient.schedule.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.error.customException.NotFoundScheduleException;
import project.fin_the_pen.finClient.core.util.*;
import project.fin_the_pen.finClient.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.type.RepeatType;
import project.fin_the_pen.model.schedule.repository.ScheduleRepository;
import project.fin_the_pen.finClient.schedule.dto.ScheduleDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    ScheduleStrategy monthStrategy = new MonthStrategy();
    ScheduleStrategy sectionStrategy = new SectionStrategy();
    ScheduleStrategy categoryStrategy = new CategoryStrategy();

    /* public Boolean registerSchedule(ScheduleAllDTO dto) {

         try {
             if (scheduleRequestDTO.getRepeat().equals("없음")) {
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
     }*/
    // TODO 1. service/ repeat, period 에 따라서
    public Boolean registerSchedule(ScheduleDTO requestDTO) {

        try {
            if (requestDTO.getRepeat().equals(RepeatType.None)) {
                if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                    isType(requestDTO, (dto) ->
                            scheduleRepository.registerSchedule(dto, PriceType.Plus, RepeatType.None));
                } else {
                    isType(requestDTO, (dto) ->
                            scheduleRepository.registerSchedule(dto, PriceType.Minus, RepeatType.None));
                }
            } else if (requestDTO.getRepeat().equals(RepeatType.AllDay)) {
                if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                    isType(requestDTO, (dto) ->
                            scheduleRepository.registerSchedule(dto, PriceType.Plus, RepeatType.AllDay));
                } else {
                    isType(requestDTO, (dto) ->
                            scheduleRepository.registerSchedule(dto, PriceType.Minus, RepeatType.AllDay));
                }
            } else if (requestDTO.getRepeat().equals(RepeatType.Week)) {
                if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                    isType(requestDTO, (dto) ->
                            scheduleRepository.registerSchedule(dto, PriceType.Plus, RepeatType.Week));
                } else {
                    isType(requestDTO, (dto) ->
                            scheduleRepository.registerSchedule(dto, PriceType.Minus, RepeatType.Week));
                }
            } else if (requestDTO.getRepeat().equals(RepeatType.Month)) {
                if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                    isType(requestDTO, (dto) ->
                            scheduleRepository.registerSchedule(dto, PriceType.Plus, RepeatType.Month));
                } else {
                    isType(requestDTO, (dto) ->
                            scheduleRepository.registerSchedule(dto, PriceType.Minus, RepeatType.Month));
                }
            }
        } catch (Exception e) {
            return null;
        }
        return true;
    }

    private void isRepeat(RepeatType repeatType) {

    }


    public JSONArray findAllSchedule(String id) {
        List<Schedule> scheduleList = scheduleRepository.findAllSchedule(id);
        ScheduleStrategy allStrategy = new AllMonthStrategy();
        return allStrategy.execute(scheduleList);
    }

    /*public boolean modifySchedule(ScheduleDTO scheduleRequestDTO) {
        if (!scheduleRepository.modifySchedule(scheduleRequestDTO)) {
            return false;
        }
        return true;
    }*/

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
    public JSONObject findMonthSchedule(String date, String userId) {
        List<Schedule> byMonthSchedule = scheduleRepository.findMonthSchedule(date, userId);

        byMonthSchedule.stream().forEach(schedule -> {
            log.info(schedule.getEventName());
        });
        // 이 위까지는 다 잘 됨

        JSONArray responseJsonArray = null;
        JSONObject responseJson = null;


        try {
            responseJsonArray = new MonthStrategy().execute(byMonthSchedule);
            log.info("response Json Array 사이즈 :{}", responseJsonArray.size());
            responseJson = new JSONObject();
            responseJson.put("data", responseJsonArray);

            return responseJson;
        } catch (NotFoundScheduleException e) {
            responseJson = new JSONObject();
            responseJson.put("data", "error");
            return responseJson;
        }
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

//    public ScheduleResponseDTO findOne(String uuid) {
//        return scheduleRepository.findOneSchedule(uuid);
//    }

    public JSONArray findByContainsName(String name) {
        List<Schedule> byContainsNameList = scheduleRepository.findByContainsName(name);
        ScheduleStrategy containStrategy = new ContainStrategy();
        return containStrategy.execute(byContainsNameList);

    }

    /**
     * callback
     *
     * @param
     * @param callback
     */
    private void isType(ScheduleDTO dto, ScheduleTypeFunc callback) {
        callback.callbackMethod(dto);
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
