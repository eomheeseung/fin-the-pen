package project.fin_the_pen.model.home.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import project.fin_the_pen.config.jwt.JwtService;
import project.fin_the_pen.config.oauth2.socialDomain.SocialUserRepository;
import project.fin_the_pen.finClient.core.util.TokenParser;
import project.fin_the_pen.model.home.dto.HomeRequestDto;
import project.fin_the_pen.model.home.dto.HomeWeekResponseDto;
import project.fin_the_pen.model.home.repository.HomeRepository;
import project.fin_the_pen.model.report.repository.ReportRepository;
import project.fin_the_pen.model.schedule.dto.ScheduleResponseDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.user.repository.CRUDLoginRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomeService {
    private final HomeRepository homeRepository;
    private final ReportRepository reportRepository;
    private final CrudScheduleRepository scheduleRepository;
    private final SocialUserRepository socialUserRepository;
    private final CRUDLoginRepository loginRepository;
    private final JwtService jwtService;
    private final TokenParser tokenParser;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");



    public HashMap<Object, Object> inquiryMonth(HomeRequestDto dto,
                                                HttpServletRequest request) {
        String userId = getUserId(request);


        HashMap<Object, Object> responseMap = new HashMap<>();

        // 이제 입력받은 날짜의 월의 첫날과 마지막 날을 구해야 함...
        LocalDate parseDate = LocalDate.parse(dto.getCalenderDate());
        LocalDate startDate = parseDate.withDayOfMonth(1);
        LocalDate endDate = parseDate.withDayOfMonth(parseDate.lengthOfMonth());

//        String userId = dto.getUserId();
        String calenderDate = dto.getCalenderDate();
        String date = dto.getDate();

        // 수입
        List<String> incomeList = homeRepository.findAmountByUserIdAndPriceType(userId, PriceType.Plus,
                startDate.toString(), endDate.toString());

        // 지출
        List<String> expenseList = homeRepository.findAmountByUserIdAndPriceType(userId,
                PriceType.Minus, startDate.toString(), dto.getCalenderDate());

        // 지출 예정 금액
        List<String> expenseExpectList = homeRepository.findAmountByUserIdAndPriceType(userId,
                PriceType.Minus, parseDate.plusDays(1).toString(), endDate.toString());

        // 지출 목표액
        Optional<String> optionalS = reportRepository.findByAmountAndUserIdAndDate(dto.getDate(), userId);

        int goalAmount = 0;

        if (optionalS.isPresent()) {
            goalAmount = Integer.parseInt(optionalS.get());
        }

        int incomeSum = incomeList
                .stream()
                .mapToInt(Integer::parseInt)
                .sum();

        int expenseSum = expenseList
                .stream()
                .mapToInt(Integer::parseInt)
                .sum();

        int expenseExpectSum = expenseExpectList
                .stream()
                .mapToInt(Integer::parseInt)
                .sum();

        int availableSum = goalAmount - incomeSum - expenseSum - expenseExpectSum;

        responseMap.put("income", "+" + incomeSum + "원");
        responseMap.put("expense", "-" + expenseSum + "원");

        Supplier<String> supplier = () -> {
            if (availableSum < 0) {
                return "-" + Math.abs(availableSum) + "원";
            } else return "+" + Math.abs(availableSum) + "원";
        };


        responseMap.put("available", supplier.get());

        List<Schedule> responseArray = scheduleRepository.findByMonthSchedule(date, userId);

        List<ScheduleResponseDTO> responseDTOList = responseArray.stream()
                .map(this::createScheduleResponseDTO)
                .collect(Collectors.toList());

        responseMap.put("data", responseDTOList);
        responseMap.put("count", responseDTOList.size());

        return responseMap;
    }

    public HashMap<Object, Object> inquiryWeek(HomeRequestDto dto, HttpServletRequest request) {
        HashMap<Object, Object> responseMap = new HashMap<>();

        String userId = getUserId(request);

        // 이제 입력받은 날짜의 월의 첫날과 마지막 날을 구해야 함...
        LocalDate parseDate = LocalDate.parse(dto.getCalenderDate());
        LocalDate startDate = parseDate.withDayOfMonth(1);
        LocalDate endDate = parseDate.withDayOfMonth(parseDate.lengthOfMonth());

        // 수입
        List<String> incomeList = homeRepository.findAmountByUserIdAndPriceType(userId, PriceType.Plus, startDate.toString(), endDate.toString());

        // 지출
        List<String> expenseList = homeRepository.findAmountByUserIdAndPriceType(userId, PriceType.Minus, startDate.toString(), dto.getCalenderDate());

        // 지출 예정 금액
        List<String> expenseExpectList = homeRepository.findAmountByUserIdAndPriceType(userId, PriceType.Minus, parseDate.plusDays(1).toString(), endDate.toString());

        // 지출 목표액
        Optional<String> optionalS = reportRepository.findByAmountAndUserIdAndDate(dto.getDate(), userId);

        int goalAmount = 0;

        if (optionalS.isPresent()) {
            goalAmount = Integer.parseInt(optionalS.get());
        }

        int incomeSum = incomeList
                .stream()
                .mapToInt(Integer::parseInt)
                .sum();

        int expenseSum = expenseList
                .stream()
                .mapToInt(Integer::parseInt)
                .sum();

        int expenseExpectSum = expenseExpectList
                .stream()
                .mapToInt(Integer::parseInt)
                .sum();

        int availableSum = goalAmount - incomeSum - expenseSum - expenseExpectSum;

        responseMap.put("income", "+" + incomeSum + "원");
        responseMap.put("expense", "-" + expenseSum + "원");

        Supplier<String> supplier = () -> {
            if (availableSum < 0) {
                return "-" + Math.abs(availableSum) + "원";
            } else return "+" + Math.abs(availableSum) + "원";
        };


        responseMap.put("available", supplier.get());

        List<HomeWeekResponseDto> weekResponseDtoList = new ArrayList<>();

        // 홈화면 주별에서 4번
        // 주차 구하기
        WeekFields weekFields = WeekFields.of(new Locale("ko"));
        int weekNumber = startDate.get(weekFields.weekOfMonth());

        int maxWeeksInMonth = endDate.get(weekFields.weekOfMonth());

        log.info(String.valueOf(weekNumber));

        for (int week = 1; week <= maxWeeksInMonth; week++) {

            LocalDate firstDayOfWeek = startDate.with(weekFields.weekOfMonth(), week).with(DayOfWeek.MONDAY);
            LocalDate lastDayOfWeek = firstDayOfWeek.with(DayOfWeek.SUNDAY);

            HomeWeekResponseDto responseDto = new HomeWeekResponseDto();
            responseDto.setWeekOfNumber(week + "주차");
            responseDto.setPeriod(firstDayOfWeek + " ~ " + lastDayOfWeek);

            List<Schedule> findList = scheduleRepository.findByStartDateAndEndDate(userId, firstDayOfWeek.toString(), lastDayOfWeek.toString());

            int plusSum = 0;
            int minusSum = 0;

            for (Schedule schedule : findList) {
                if (schedule.getPriceType().equals(PriceType.Plus)) {
                    plusSum += Integer.parseInt(schedule.getAmount());
                } else {
                    minusSum += Integer.parseInt(schedule.getAmount());
                }
            }

            responseDto.setPlus(plusSum);
            responseDto.setMinus(minusSum);
            weekResponseDtoList.add(responseDto);
        }

        responseMap.put("week_schedule", weekResponseDtoList);

        return responseMap;
    }


    public HashMap<Object, Object> inquiryDay(HomeRequestDto dto, HttpServletRequest request) {
        HashMap<Object, Object> responseMap = new HashMap<>();

        List<Schedule> findList = scheduleRepository.findByStartDate(dto.getUserId(), dto.getCalenderDate());
        LocalDateTime nowDateTime = LocalDateTime.now();

        String userId = getUserId(request);

        // 시간에 따라서 분리
        int dayIncome = 0; // 수입
        int dayExpense = 0; // 지출
        int expenseExpect = 0; // 지출 예정
        int available = 0;

        Optional<String> optionalGoalAmount = reportRepository.findByAmountAndUserIdAndDate(dto.getDate(), userId);

        if (!findList.isEmpty()) {
            dayIncome = findList.stream()
                    .filter(schedule -> schedule.getPriceType() == PriceType.Plus)
                    .mapToInt(schedule -> Integer.parseInt(schedule.getAmount()))
                    .sum();

            dayExpense = findList.stream()
                    .filter(schedule -> schedule.getPriceType() != PriceType.Plus)
                    .map(schedule -> {
                        LocalTime localTime = LocalTime.parse(schedule.getStartTime());
                        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.parse(dto.getCalenderDate()), localTime);
                        return new AbstractMap.SimpleEntry<>(localDateTime, Integer.parseInt(schedule.getAmount()));
                    })
                    .filter(entry -> entry.getKey().isBefore(nowDateTime))
                    .mapToInt(Map.Entry::getValue)
                    .sum();

            expenseExpect = findList.stream()
                    .filter(schedule -> schedule.getPriceType() != PriceType.Plus)
                    .map(schedule -> {
                        LocalTime localTime = LocalTime.parse(schedule.getStartTime());
                        LocalDateTime localDateTime = LocalDateTime.of(LocalDate.parse(dto.getCalenderDate()), localTime);
                        return new AbstractMap.SimpleEntry<>(localDateTime, Integer.parseInt(schedule.getAmount()));
                    })
                    .filter(entry -> entry.getKey().isAfter(nowDateTime) || entry.getKey().isEqual(nowDateTime))
                    .mapToInt(Map.Entry::getValue)
                    .sum();

            if (optionalGoalAmount.isPresent()) {
                available = Integer.parseInt(optionalGoalAmount.get()) - dayExpense - expenseExpect;
            }

            responseMap.put("income", "+" + dayIncome);
            responseMap.put("dayExpense", "-" + dayExpense);
            responseMap.put("expect", "-" + expenseExpect);
            responseMap.put("available", available);

        } else {
            responseMap.put("income", 0);
            responseMap.put("dayExpense", 0);
        }

        return responseMap;
    }


    // TODO 홈 - 일정 리스트
    public Map<Object, Object> findScheduleList() {
        HashMap<Object, Object> responseMap = new HashMap<>();


        return responseMap;
    }

    /**
     * 추후에 구현
     *
     * @param date
     * @param userId
     * @return
     */
    private HashMap<Object, Object> calenderView(String date, String userId) {
        HashMap<Object, Object> calenderMap = new HashMap<>();

        List<Schedule> scheduleList = homeRepository.findByUserIdAndStartDate(date, userId);

        StringBuilder builder = new StringBuilder();
        LocalDate localDate = LocalDate.parse(date, formatter);

        Supplier<String> supplier = getDayOfWeekSupplier(localDate);

        builder.append(localDate.getMonthValue())
                .append("월 ")
                .append(localDate.getDayOfMonth())
                .append("일 ")
                .append(supplier.get());

        calenderMap.put("current_date", builder.toString());
        calenderMap.put("count", String.valueOf(scheduleList.size()));

        return calenderMap;
    }

    @NotNull
    private Supplier<String> getDayOfWeekSupplier(LocalDate localDate) {
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();

        return () -> {
            String value = "none";

            switch (dayOfWeek) {
                case MONDAY:
                    value = "월요일";
                    break;
                case TUESDAY:
                    value = "화요일";
                    break;
                case WEDNESDAY:
                    value = "수요일";
                    break;
                case THURSDAY:
                    value = "목요일";
                    break;
                case FRIDAY:
                    value = "금요일";
                    break;
                case SATURDAY:
                    value = "토요일";
                    break;
                case SUNDAY:
                    value = "일요일";
                    break;
            }
            return value;
        };
    }

    private ScheduleResponseDTO createScheduleResponseDTO(Schedule schedule) {
        return ScheduleResponseDTO.builder()
                .scheduleId(schedule.getId())
                .userId(schedule.getUserId())
                .eventName(schedule.getEventName())
                .category(schedule.getCategory())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .startTime(schedule.getStartTime())
                .repeatKind(schedule.getRepeatKind())
                .endTime(schedule.getEndTime())
                .allDay(schedule.isAllDay())
                .repeatOptions(schedule.getRepeatOptions())
                .period(schedule.getPeriod())
                .priceType(schedule.getPriceType())
                .isExclude(schedule.isExclude())
                .paymentType(schedule.getPaymentType().toString())
                .amount(schedule.getAmount())
                .isFixAmount(schedule.isFixAmount())
                .build();
    }

    private String getUserId(HttpServletRequest request) {
        String parseToken = tokenParser.parseBearerToken(request);
        log.info("홈화면에서 파싱된 토큰:{}", parseToken);

        /*String subject = jwtService.validateTokenAndGetSubject(parseToken);

        log.info("홈화면에서 토큰의 subject 확인:{}", parseToken);

        String socialType = jwtService.getSocialTypeFromToken(parseToken);

        log.info("subject (email), socialType:{}, {}", subject, socialType);

        if (socialType.equals(SocialType.NONE.toString())) {
            Optional<Users> byUserId = loginRepository.findByUserId(subject);

            log.info("checking home user id:{}", byUserId.get());
        } else {
            Optional<SocialUser> bySocialId = socialUserRepository.findBySocialId(subject);

            log.info("checking home social user id:{}", bySocialId.get());
        }*/

        return jwtService.validateTokenAndGetSubject(parseToken);
    }

}
