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
import project.fin_the_pen.model.report.ConsumeReportRequestDTO;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.dto.ScheduleResponseDTO;
import project.fin_the_pen.model.schedule.dto.TypeManageDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.repository.ScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.usersToken.entity.UsersToken;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Consumer;
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

    /*
    TODO 반복은 모두 완료되었는데 여기서 "기간"을 조건으로 넣어서 해야 함.
     Ex)
     currentDate : 2023-12-25
     endLine : 2050-12-05
     - 2년 주기로 endLine까지가 아닌 3회만 반복한다면, (2023, 2025, 2027까지만 저장될 것임.)
     - 현재 만들어 놓은 코드는 endLine까지 계속 반복되는 code
     */
    public Map<Object, Object> registerSchedule(ScheduleRequestDTO requestDTO, HttpServletRequest request) {
        boolean flag = false;

        try {
            String extractToken = tokenManager.parseBearerToken(request);

            if (extractToken == null)
                throw new RuntimeException();

            tokenRepository.findUsersToken(extractToken).orElseThrow(() -> new TokenNotFoundException("Token not found"));

            switch (requestDTO.getRepeat().getKindType()) {
                case "none":
                    if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerNoneSchedule(requestDTO);
                    } else {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Minus));
                        flag = scheduleRepository.registerNoneSchedule(requestDTO);
                    }
                    break;
                case "day":
                    if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerDaySchedule(requestDTO);
                    } else {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Minus));
                        flag = scheduleRepository.registerDaySchedule(requestDTO);
                    }
                    break;
                case "week":
                    if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerWeekSchedule(requestDTO);
                    } else {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Minus));
                        flag = scheduleRepository.registerWeekSchedule(requestDTO);
                    }
                    break;
                case "month":
                    if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerMonthSchedule(requestDTO);
                    } else {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Minus));
                        flag = scheduleRepository.registerMonthSchedule(requestDTO);
                    }
                    break;
                case "year":
                    if (requestDTO.getPriceType().equals(PriceType.Plus)) {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Plus));
                        flag = scheduleRepository.registerYearSchedule(requestDTO);
                    } else {
                        isType(requestDTO, (dto) ->
                                dto.setPriceType(PriceType.Minus));
                        flag = scheduleRepository.registerYearSchedule(requestDTO);
                    }
                    break;
            }

            if (flag) {
                HashMap<Object, Object> responseMap = new HashMap<>();
                responseMap.put("data", objectMapper.convertValue(requestDTO.getUserId(), String.class));
                return responseMap;
            } else throw new FailSaveScheduleException("일정 등록 실패");

        } catch (DuplicatedScheduleException | FailSaveScheduleException | TokenNotFoundException e) {
            log.info(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
//            throw new RuntimeException("등록 오류입니다.");
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

            Optional<UsersToken> findToken = tokenRepository.findUsersToken(accessToken)
                    .map(Optional::of)
                    .orElseThrow(() -> new TokenNotFoundException("Token not found"));

            findToken
                    .filter(token -> token.getUserId().equals(userId))
                    .orElseThrow(() -> new Exception("Error"));

            List<Schedule> responseArray = scheduleRepository.findAllSchedule(userId);

            responseMap.put("data", responseArray.isEmpty() ? "error" :
                    convertSnakeList(responseArray.stream()
                            .map(this::createScheduleResponseDTO)
                            .collect(Collectors.toList())));

            responseMap.put("count", responseArray.size());
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
        return responseMap;
    }


    // TODO
    public Map<Object, Object> modifySchedule(ModifyScheduleDTO modifyScheduleDTO, HttpServletRequest request) {
        try {
            boolean flag = false;

            try {
                String extractToken = tokenManager.parseBearerToken(request);

                if (extractToken == null)
                    throw new RuntimeException();

                tokenRepository.findUsersToken(extractToken).orElseThrow(() -> new TokenNotFoundException("Token not found"));
                String options = modifyScheduleDTO.getOptions();
                TypeManageDTO repeat = modifyScheduleDTO.getRepeat();

                switch (options) {
                    case "nowFromAfter":
                        if (repeat.getKindType().equals("day")) {
                            if (modifyScheduleDTO.getPriceType().equals(PriceType.Plus)) {
                                isType(modifyScheduleDTO, (dto) -> dto.setPriceType(PriceType.Plus));
                                flag = scheduleRepository.modifyNowFromAfter(modifyScheduleDTO, "day");

                            } else {
                                isType(modifyScheduleDTO, (dto) -> dto.setPriceType(PriceType.Minus));
                                flag = scheduleRepository.modifyNowFromAfter(modifyScheduleDTO, "day");
                            }
                        } else if (repeat.getKindType().equals("week")) {
                            if (modifyScheduleDTO.getPriceType().equals(PriceType.Plus)) {
                                isType(modifyScheduleDTO, (dto) -> dto.setPriceType(PriceType.Plus));
                                flag = scheduleRepository.modifyNowFromAfter(modifyScheduleDTO, "week");
                            } else {
                                isType(modifyScheduleDTO, (dto) -> dto.setPriceType(PriceType.Minus));
                                flag = scheduleRepository.modifyNowFromAfter(modifyScheduleDTO, "week");
                            }
                        } else if (repeat.getKindType().equals("month")) {
                            if (modifyScheduleDTO.getPriceType().equals(PriceType.Plus)) {
                                isType(modifyScheduleDTO, (dto) -> dto.setPriceType(PriceType.Plus));
                                flag = scheduleRepository.modifyNowFromAfter(modifyScheduleDTO, "month");
                            } else {
                                isType(modifyScheduleDTO, (dto) -> dto.setPriceType(PriceType.Minus));
                                flag = scheduleRepository.modifyNowFromAfter(modifyScheduleDTO, "month");
                            }
                        } else if (repeat.getKindType().equals("year")) {
                            if (modifyScheduleDTO.getPriceType().equals(PriceType.Plus)) {
                                isType(modifyScheduleDTO, (dto) -> dto.setPriceType(PriceType.Plus));
                                flag = scheduleRepository.modifyNowFromAfter(modifyScheduleDTO, "year");
                            } else {
                                isType(modifyScheduleDTO, (dto) -> dto.setPriceType(PriceType.Minus));
                                flag = scheduleRepository.modifyNowFromAfter(modifyScheduleDTO, "year");
                            }
                        }

                        break;
                    /*case "exceptNowAfter":
                        break;
                    case "all":
                        break;*/
                }


                if (flag) {
                    HashMap<Object, Object> responseMap = new HashMap<>();
                    responseMap.put("data", objectMapper.convertValue(modifyScheduleDTO.getUserId(), String.class));
                    return responseMap;
                } else throw new FailSaveScheduleException("일정 수정 실패");
            } catch (TokenNotFoundException e) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException();
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

    public Map<String, Object> inquiryReport(ConsumeReportRequestDTO dto, HttpServletRequest request) {
        String accessToken = tokenManager.parseBearerToken(request);
        Map<String, Object> responseMap = new HashMap<>();

        String userId = dto.getUserId();
        String date = dto.getDate();
        log.info("실행");

        Map<String, Integer> map = new HashMap<>();

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

                    responseArray.forEach(schedule -> {
                        String category = schedule.getCategory();
                        Integer amount = Integer.parseInt(schedule.getAmount());

                        if (schedule.getPriceType().equals(PriceType.Minus)) {
                            map.compute(category, (key, value) -> (value == null) ? amount : value + amount);
                        }
                    });
                }

                // Map의 entry를 List로 변환
                List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());

                // Comparator를 사용하여 value를 기준으로 내림차순 정렬
                entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

                // 정렬된 결과를 다시 Map으로 저장
                Map<String, Integer> sortedMap = entryList.stream()
                        .limit(5)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                responseMap.put("data", sortedMap);

            }
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
        return responseMap;
    }

    private ScheduleResponseDTO createScheduleResponseDTO(Schedule schedule) {
        return ScheduleResponseDTO.builder()
                .id(schedule.getId())
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
    private void isType(ScheduleRequestDTO dto, ScheduleTypeFunc callback) {
        callback.callbackMethod(dto);
    }

    private void isType(ModifyScheduleDTO dto, Consumer<ModifyScheduleDTO> consumer) {
        consumer.accept(dto);
    }

    private boolean modifyIsType(ScheduleRequestDTO dto, ScheduleModifyFunc callback) {
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
