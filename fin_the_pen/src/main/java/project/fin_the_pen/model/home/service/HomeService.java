package project.fin_the_pen.model.home.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.error.customException.TokenNotFoundException;
import project.fin_the_pen.finClient.core.util.TokenManager;
import project.fin_the_pen.model.home.dto.HomeMonthRequestDto;
import project.fin_the_pen.model.home.repository.HomeRepository;
import project.fin_the_pen.model.report.repository.ReportRepository;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
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
        String accessToken = tokenManager.parseBearerToken(request);
        HashMap<Object, Object> responseMap = new HashMap<>();


        if (accessToken == null) {
            throw new TokenNotFoundException("token Not Found");
        }

        // 이제 입력받은 날짜의 월의 첫날과 마지막 날을 구해야 함...
        LocalDate parseDate = LocalDate.parse(dto.getMainDate(), formatter);
        LocalDate startDate = parseDate.withDayOfMonth(1);
        LocalDate endDate = parseDate.withDayOfMonth(parseDate.lengthOfMonth());

        // 수입
        List<String> incomeList = homeRepository.findAmountByUserIdAndPriceType(dto.getUserId(), PriceType.Plus, startDate.toString(), endDate.toString());

        // 지출
        List<String> expenseList = homeRepository.findAmountByUserIdAndPriceType(dto.getUserId(), PriceType.Minus, startDate.toString(), dto.getMainDate());

        // 지출 예정 금액
        List<String> expenseExpectList = homeRepository.findAmountByUserIdAndPriceType(dto.getUserId(), PriceType.Minus, parseDate.plusDays(1).toString(), endDate.toString());

        // 지출 목표액
        Integer goalAmount = Integer.parseInt(reportRepository.findByAmountAndUserIdAndDate(dto.getMainDate(), dto.getUserId()));


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
                return "-" + availableSum + "원";
            } else return "+" + availableSum + "원";
        };


        responseMap.put("available", supplier.get() + "원");


        // 4번 캘린더의 리스트를 보여주는 것은 controller의 findMonthSchedule에서 처리
        responseMap.put("calender", calenderView(dto.getCalenderDate(), dto.getUserId()));

        // TODO home 화면 -> 5번

        return responseMap;
    }

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
