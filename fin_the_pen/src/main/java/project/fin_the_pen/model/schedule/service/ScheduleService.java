package project.fin_the_pen.model.schedule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.finClient.core.error.customException.FailSaveScheduleException;
import project.fin_the_pen.finClient.core.error.customException.TokenNotFoundException;
import project.fin_the_pen.finClient.core.util.ScheduleModifyFunc;
import project.fin_the_pen.finClient.core.util.ScheduleTypeFunc;
import project.fin_the_pen.finClient.core.util.TokenManager;
import project.fin_the_pen.model.schedule.dto.ScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ScheduleResponseDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.type.DayType;
import project.fin_the_pen.model.schedule.entity.type.MonthType;
import project.fin_the_pen.model.schedule.entity.type.WeekType;
import project.fin_the_pen.model.schedule.entity.type.YearType;
import project.fin_the_pen.model.schedule.repository.ScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.usersToken.entity.UsersToken;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

import javax.servlet.http.HttpServletRequest;
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
    private final TokenManager tokenManager;

    private List convertSnakeList(List<ScheduleResponseDTO> list) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper.convertValue(list, List.class);
    }

    private ScheduleResponseDTO convertSnakeSingle(ScheduleResponseDTO responseDTO) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return objectMapper.convertValue(responseDTO, ScheduleResponseDTO.class);
    }

    public Map<Object, Object> registerSchedule(ScheduleDTO requestDTO, HttpServletRequest request) {
        boolean flag = false;

        try {
            String extractToken = tokenManager.parseBearerToken(request);

            if (extractToken == null)
                throw new RuntimeException();

            Optional<UsersToken> usersToken =
                    Optional.ofNullable(tokenRepository.findUsersToken(extractToken)
                            .orElseThrow(() -> new TokenNotFoundException("token not found")));

            String token = usersToken.get().getAccessToken();
            log.info("parseToken : {}", token);


            switch (requestDTO.getRepeat().getKindType()) {
                case "none":
                    if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerSchedule(requestDTO);
                    } else {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Minus));
                        flag = scheduleRepository.registerSchedule(requestDTO);
                    }
                    break;
                case "day":
                    DayType dayType = new DayType();
                    dayType.setValue(requestDTO.getRepeat().getValue());

                    if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerSchedule(requestDTO, dayType);
                    } else {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Minus));
                        flag = scheduleRepository.registerSchedule(requestDTO, dayType);
                    }
                    break;
                case "week":
                    WeekType weekType = new WeekType();
                    weekType.setValue(requestDTO.getRepeat().getValue());

                    if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerSchedule(requestDTO, weekType);
                    } else {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerSchedule(requestDTO, weekType);
                    }
                    break;
                case "month":
                    MonthType monthType = new MonthType();
                    monthType.setValue(requestDTO.getRepeat().getValue());

                    if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerSchedule(requestDTO, monthType);
                    } else {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Minus));
                        flag = scheduleRepository.registerSchedule(requestDTO, monthType);
                    }
                    break;
                case "year":
                    YearType yearType = new YearType();
                    yearType.setValue(requestDTO.getRepeat().getValue());

                    if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerSchedule(requestDTO, yearType);
                    } else {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Minus));
                        flag = scheduleRepository.registerSchedule(requestDTO, yearType);
                    }
                    break;
            }

            if (flag) {
                HashMap<Object, Object> responseMap = new HashMap<>();
                responseMap.put("data", objectMapper.convertValue(requestDTO.getUserId(), String.class));
                return responseMap;
            } else throw new FailSaveScheduleException("일정 등록 실패");

        } catch (DuplicatedScheduleException | FailSaveScheduleException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException("등록 오류입니다.");
        }
    }


    // 수정 완료
    public Map<String, Object> findAllSchedule(String userId, HttpServletRequest request) {
        Map<String, Object> responseMap = new HashMap<>();

        try {
            String accessToken = tokenManager.parseBearerToken(request);

            if (accessToken == null) {
                throw new RuntimeException();
            }
            Optional<UsersToken> findToken = Optional.ofNullable(tokenRepository.findUsersToken(accessToken)
                    .orElseThrow(() -> new TokenNotFoundException("token not found")));

            // 현재 토큰으로 로그인 된 사용자의 userId와 클라이언트로부터 전달받은 userId값이 일치하지 않은 경우 error!!!
            if (!findToken.get().getUserId().equals(userId)) {
                throw new Exception("error");
            } else {
                List<Schedule> responseArray = scheduleRepository.findAllSchedule(userId);

                if (responseArray.isEmpty()) {
                    responseMap.put("data", "error");
                } else {
                    List<ScheduleResponseDTO> responseDTOList = responseArray.stream()
                            .map(this::createScheduleResponseDTO)
                            .collect(Collectors.toList());

                    responseMap.put("data", convertSnakeList(responseDTOList));
                    responseMap.put("count", responseDTOList.size());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
        return responseMap;
    }

    /**
     * 일정 수정 기본.
     *
     * @return
     */
    /*public boolean modifySchedule(ScheduleDTO requestDTO) {
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
            responseMap.put("data", convertSnakeList(responseDTOList));
        }
        return responseMap;
    }


    /*public JSONArray findMonthSchedule(String date, String userId) {
        List<Schedule> byMonthSchedule = scheduleRepository.findMonthSchedule(date, userId);
        return getJsonArrayBySchedule(byMonthSchedule, new JSONArray());
    }
*/

    /**
     * 수입과 지출에 따라 계산해야 함.
     *
     * @param date
     * @param userId
     * @param request
     * @return
     */
    public Map<String, Object> findMonthSchedule(String date, String userId, HttpServletRequest request) {
        String accessToken = tokenManager.parseBearerToken(request);
        Map<String, Object> responseMap = new HashMap<>();

        try {
            if (accessToken == null) {
                throw new RuntimeException();
            }
            Optional<UsersToken> findToken = Optional.ofNullable(tokenRepository.findUsersToken(accessToken)
                    .orElseThrow(() -> new TokenNotFoundException("token not found")));

            // 현재 토큰으로 로그인 된 사용자의 userId와 클라이언트로부터 전달받은 userId값이 일치하지 않은 경우 error!!!
            if (!findToken.get().getUserId().equals(userId)) {
                throw new Exception("error");
            } else {
                List<Schedule> responseArray = scheduleRepository.findMonthSchedule(date, userId);

                if (responseArray.isEmpty()) {
                    responseMap.put("data", "error");
                } else {

                    Map<PriceType, Integer> result = responseArray.stream()
                            .collect(Collectors.groupingBy(
                                    Schedule::getPriceType,
                                    Collectors.summingInt(schedule -> Integer.parseInt(schedule.getAmount()))
                            ));

                    int deposit = result.getOrDefault(PriceType.Plus, 0);
                    int withdraw = result.getOrDefault(PriceType.Minus, 0);

                    List<ScheduleResponseDTO> responseDTOList = responseArray.stream()
                            .map(this::createScheduleResponseDTO)
                            .collect(Collectors.toList());

                    responseMap.put("data", convertSnakeList(responseDTOList));
                    responseMap.put("count", responseDTOList.size());
                    responseMap.put("deposit", deposit);
                    responseMap.put("withdraw", withdraw);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("error");
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
//                .repeat(schedule.getRepeat())
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

            responseMap.put("data", convertSnakeList(responseDTOList));
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

            responseMap.put("data", convertSnakeList(responseDTOList));
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
