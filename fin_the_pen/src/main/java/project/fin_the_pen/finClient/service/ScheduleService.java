package project.fin_the_pen.finClient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.data.schedule.*;
import project.fin_the_pen.finClient.data.schedule.category.CategoryRequestDTO;
import project.fin_the_pen.finClient.data.schedule.type.PriceType;
import project.fin_the_pen.finClient.data.schedule.type.RepeatType;
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

    /* public Boolean registerSchedule(ScheduleAllDTO allDTO) {

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
    public Boolean registerSchedule(ScheduleAllDTO allDTO) {

        ScheduleDTO scheduleDTO = allDTO.getScheduleDTO();
        AssetRequestDTO assetDto = allDTO.getAssetDto();

        try {
            if (scheduleDTO.getRepeat().equals(RepeatType.None)) {
                if (assetDto.getPriceType().equals(PriceType.Plus)) {
                    isType(allDTO, (dto) ->
                            scheduleRepository.registerSchedule(allDTO, PriceType.Plus, RepeatType.None));
                } else {
                    isType(allDTO, (dto) ->
                            scheduleRepository.registerSchedule(allDTO, PriceType.Minus, RepeatType.None));
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
    private void isType(ScheduleAllDTO allDTO, ScheduleTypeFunc callback) {
        callback.callbackMethod(allDTO);
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
