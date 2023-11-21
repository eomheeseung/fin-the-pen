package project.fin_the_pen.model.schedule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.util.*;
import project.fin_the_pen.model.schedule.dto.ScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ScheduleResponseDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.repository.ScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.type.RepeatType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ObjectMapper objectMapper;

    private List convertSnake(List<ScheduleResponseDTO> list) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper.convertValue(list, List.class);
    }

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


    // 수정 완료
    public Map<String, Object> findAllSchedule(String userId) {
        List<Schedule> responseArray = scheduleRepository.findAllSchedule(userId);
        Map<String, Object> responseMap = new HashMap<>();

        if (responseArray.isEmpty()) {
            responseMap.put("data", "error");
        } else {
            List<ScheduleResponseDTO> responseDTOList = responseArray.stream()
                    .map(this::createScheduleResponseDTO)
                    .collect(Collectors.toList());
            responseMap.put("data", convertSnake(responseDTOList));
        }

        return responseMap;
    }

    /**
     * 일정 수정 기본.
     *
     * @param requestDTO
     * @return
     */
    public boolean modifySchedule(ScheduleDTO requestDTO) {


        try {
            boolean flag = false;

            if (requestDTO.getRepeat().equals(RepeatType.None)) {
                if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                    flag = modifyIsType(requestDTO, (dto) ->
                            scheduleRepository.modifySchedule(dto, PriceType.Plus, RepeatType.None));
                } else {
                    flag = modifyIsType(requestDTO, (dto) ->
                            scheduleRepository.modifySchedule(dto, PriceType.Minus, RepeatType.None));
                }
            } else if (requestDTO.getRepeat().equals(RepeatType.AllDay)) {
                if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                    flag = modifyIsType(requestDTO, (dto) ->
                            scheduleRepository.modifySchedule(dto, PriceType.Plus, RepeatType.AllDay));
                } else {
                    flag = modifyIsType(requestDTO, (dto) ->
                            scheduleRepository.modifySchedule(dto, PriceType.Minus, RepeatType.AllDay));
                }
            } else if (requestDTO.getRepeat().equals(RepeatType.Week)) {
                if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                    flag = modifyIsType(requestDTO, (dto) ->
                            scheduleRepository.modifySchedule(dto, PriceType.Plus, RepeatType.Week));
                } else {
                    flag = modifyIsType(requestDTO, (dto) ->
                            scheduleRepository.modifySchedule(dto, PriceType.Minus, RepeatType.Week));
                }
            } else if (requestDTO.getRepeat().equals(RepeatType.Month)) {
                if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                    flag = modifyIsType(requestDTO, (dto) ->
                            scheduleRepository.modifySchedule(dto, PriceType.Plus, RepeatType.Month));
                } else {
                    flag = modifyIsType(requestDTO, (dto) ->
                            scheduleRepository.modifySchedule(dto, PriceType.Minus, RepeatType.Month));
                }
            }
            if (!flag) {
                throw new RuntimeException();
            }
            return flag;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /*public JSONArray findScheduleCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
        List<Schedule> result = scheduleRepository.findScheduleByCategory(categoryRequestDTO, currentSession);
        return getJsonArrayBySchedule(result, new JSONArray());
    }*/

    public Map<String, Object> findScheduleCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
        List<Schedule> responseArray = scheduleRepository.findScheduleByCategory(categoryRequestDTO, currentSession);
        Map<String, Object> responseMap = new HashMap<>();

        if (responseArray.isEmpty()) {
            responseMap.put("data", "error");
        } else {
            List<ScheduleResponseDTO> responseDTOList = responseArray.stream()
                    .map(this::createScheduleResponseDTO)
                    .collect(Collectors.toList());
            responseMap.put("data", convertSnake(responseDTOList));
        }
        return responseMap;
    }


    /*public JSONArray findMonthSchedule(String date, String userId) {
        List<Schedule> byMonthSchedule = scheduleRepository.findMonthSchedule(date, userId);
        return getJsonArrayBySchedule(byMonthSchedule, new JSONArray());
    }
*/
    public Map<String, Object> findMonthSchedule(String date, String userId) {
        List<Schedule> responseArray = scheduleRepository.findMonthSchedule(date, userId);
        Map<String, Object> responseMap = new HashMap<>();

        if (responseArray.isEmpty()) {
            responseMap.put("data", "error");
        } else {
            List<ScheduleResponseDTO> responseDTOList = responseArray.stream()
                    .map(this::createScheduleResponseDTO)
                    .collect(Collectors.toList());

            responseMap.put("data", convertSnake(responseDTOList));
        }

        return responseMap;
    }

    private ScheduleResponseDTO createScheduleResponseDTO(Schedule schedule) {
        return ScheduleResponseDTO.builder()
                .userId(schedule.getUserId())
                .eventName(schedule.getEventName())
                .category(schedule.getCategory())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .allDay(schedule.isAllDay())
                .repeat(schedule.getRepeat())
                .period(schedule.getPeriod())
                .priceType(schedule.getPriceType())
                .isExclude(schedule.isExclude())
                .importance(schedule.getImportance())
                .amount(schedule.getAmount())
                .isFixAmount(schedule.isFixAmount())
                .build();
    }

    public Map<String, Object> findMonthSectionSchedule(String startDate, String endDate, String userId) {
        List<Schedule> responseArray = scheduleRepository.findMonthSectionSchedule(startDate, endDate, userId);

        HashMap<String, Object> responseMap = new HashMap<>();

        if (responseArray.isEmpty())
            responseMap.put("data", "error");
        else {
            List<ScheduleResponseDTO> responseDTOList = responseArray.stream()
                    .map(this::createScheduleResponseDTO)
                    .collect(Collectors.toList());

            responseMap.put("data", convertSnake(responseDTOList));
        }
        return responseMap;
    }


    /*public boolean deleteSchedule(String uuid) {
        try {
            scheduleRepository.deleteSchedule(uuid);
        } catch (Exception e) {
            return false;
        }
        return true;
    }*/

//    public ScheduleResponseDTO findOne(String uuid) {
//        return scheduleRepository.findOneSchedule(uuid);
//    }

    public Map<String, Object> findByContainsName(String name) {
        List<Schedule> responseArray = scheduleRepository.findByContainsName(name);

        HashMap<String, Object> responseMap = new HashMap<>();

        if (responseArray.isEmpty()) {
            responseMap.put("data", "error");
        } else {
            List<ScheduleResponseDTO> responseDTOList = responseArray.stream()
                    .map(this::createScheduleResponseDTO)
                    .collect(Collectors.toList());

            responseMap.put("data", convertSnake(responseDTOList));
        }
        return responseMap;
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

    private boolean modifyIsType(ScheduleDTO dto, ScheduleModifyFunc callback) {
        return callback.modifyCallBack(dto);
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
