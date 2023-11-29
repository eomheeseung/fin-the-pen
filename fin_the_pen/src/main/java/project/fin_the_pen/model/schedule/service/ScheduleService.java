package project.fin_the_pen.model.schedule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.error.customException.TokenNotFoundException;
import project.fin_the_pen.finClient.core.util.ScheduleModifyFunc;
import project.fin_the_pen.finClient.core.util.ScheduleTypeFunc;
import project.fin_the_pen.model.schedule.dto.ScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ScheduleResponseDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.repository.ScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.usersToken.entity.UsersToken;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ObjectMapper objectMapper;
    private final UsersTokenRepository tokenRepository;

    private List convertSnake(List<ScheduleResponseDTO> list) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper.convertValue(list, List.class);
    }

    // TODO 1. 토큰 인증체계
    public Boolean registerSchedule(ScheduleDTO requestDTO, String extractToken) {
        String accessToken = extractToken.substring(7);

        try {
            Optional<UsersToken> usersToken =
                    Optional.ofNullable(tokenRepository.findUsersToken(accessToken)
                            .orElseThrow(() -> new TokenNotFoundException("token not found")));

            String token = usersToken.get().getUsersToken();
            log.info("parseToken : {}", token);

            if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                isType(requestDTO, (dto) ->
                        scheduleRepository.registerSchedule(dto, PriceType.Plus, token));
            } else {
                isType(requestDTO, (dto) ->
                        scheduleRepository.registerSchedule(dto, PriceType.Minus, token));
            }

        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
        return true;
    }


    // 수정 완료
    public Map<String, Object> findAllSchedule(String extractToken) {
        String accessToken = extractToken.substring(7);

        try {
            Optional<UsersToken> usersToken =
                    Optional.ofNullable(tokenRepository.findUsersToken(accessToken)
                            .orElseThrow(() -> new TokenNotFoundException("token not found")));
            String token = usersToken.get().getUsersToken();

            List<Schedule> responseArray = scheduleRepository.findAllSchedule(token);
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
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
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

            if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                flag = modifyIsType(requestDTO, (dto) ->
                        scheduleRepository.modifySchedule(dto, PriceType.Plus));
            } else {
                flag = modifyIsType(requestDTO, (dto) ->
                        scheduleRepository.modifySchedule(dto, PriceType.Minus));
            }
            if (!flag) {
                throw new RuntimeException();
            }
            return flag;
        } catch (
                RuntimeException e) {
            return false;
        }

    }

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
                // token
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
