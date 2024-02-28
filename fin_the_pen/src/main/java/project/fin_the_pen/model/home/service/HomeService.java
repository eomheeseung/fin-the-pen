package project.fin_the_pen.model.home.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.util.TokenManager;
import project.fin_the_pen.model.home.dto.HomeMonthRequestDto;
import project.fin_the_pen.model.home.dto.HomeWeekResponseDto;
import project.fin_the_pen.model.home.repository.HomeRepository;
import project.fin_the_pen.model.report.repository.ReportRepository;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomeService {
    private final HomeRepository homeRepository;
    private final TokenManager tokenManager;
    private final ReportRepository reportRepository;
    private final CrudScheduleRepository scheduleRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public HashMap<Object, Object> inquiryMonth(HomeMonthRequestDto dto, HttpServletRequest request) {

        HashMap<Object, Object> responseMap = new HashMap<>();

        // 이제 입력받은 날짜의 월의 첫날과 마지막 날을 구해야 함...
        LocalDate parseDate = LocalDate.parse(dto.getCalenderDate());
        LocalDate startDate = parseDate.withDayOfMonth(1);
        LocalDate endDate = parseDate.withDayOfMonth(parseDate.lengthOfMonth());

        // 수입
        List<String> incomeList = homeRepository.findAmountByUserIdAndPriceType(dto.getUserId(), PriceType.Plus, startDate.toString(), endDate.toString());

        // 지출
        List<String> expenseList = homeRepository.findAmountByUserIdAndPriceType(dto.getUserId(), PriceType.Minus, startDate.toString(), dto.getCalenderDate());

        // 지출 예정 금액
        List<String> expenseExpectList = homeRepository.findAmountByUserIdAndPriceType(dto.getUserId(), PriceType.Minus, parseDate.plusDays(1).toString(), endDate.toString());

        // 지출 목표액
        Optional<String> optionalS = reportRepository.findByAmountAndUserIdAndDate(dto.getMainDate(), dto.getUserId());

        int goalAmount = 0;

        if (optionalS.isPresent()) {
            goalAmount = Integer.parseInt(optionalS.get());
        }


        // TODO
        //  Q) 지출 예정이 애매함(시간 값까지 해야 하는지...)

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


        // 4번 캘린더의 리스트를 보여주는 것은 controller의 findMonthSchedule에서 처리
//        responseMap.put("calender", calenderView(dto.getCalenderDate(), dto.getUserId()));

        List<Schedule> byStartDate = scheduleRepository.findByStartDate(dto.getUserId(), dto.getCalenderDate());
        responseMap.put("today_schedule", byStartDate);

        return responseMap;
    }

    public HashMap<Object, Object> inquiryWeek(HomeMonthRequestDto dto, HttpServletRequest request) {
        HashMap<Object, Object> responseMap = new HashMap<>();

        // 이제 입력받은 날짜의 월의 첫날과 마지막 날을 구해야 함...
        LocalDate parseDate = LocalDate.parse(dto.getCalenderDate());
        LocalDate startDate = parseDate.withDayOfMonth(1);
        LocalDate endDate = parseDate.withDayOfMonth(parseDate.lengthOfMonth());

        // 수입
        List<String> incomeList = homeRepository.findAmountByUserIdAndPriceType(dto.getUserId(), PriceType.Plus, startDate.toString(), endDate.toString());

        // 지출
        List<String> expenseList = homeRepository.findAmountByUserIdAndPriceType(dto.getUserId(), PriceType.Minus, startDate.toString(), dto.getCalenderDate());

        // 지출 예정 금액
        List<String> expenseExpectList = homeRepository.findAmountByUserIdAndPriceType(dto.getUserId(), PriceType.Minus, parseDate.plusDays(1).toString(), endDate.toString());

        // 지출 목표액
        Optional<String> optionalS = reportRepository.findByAmountAndUserIdAndDate(dto.getMainDate(), dto.getUserId());

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

            List<Schedule> findList = scheduleRepository.findByStartDateAndeEndDate(dto.getUserId(), firstDayOfWeek.toString(), lastDayOfWeek.toString());

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
            responseMap.put(String.valueOf(week), responseDto);
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
}
