package project.fin_the_pen.model.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;
import project.fin_the_pen.finClient.core.util.TokenManager;
import project.fin_the_pen.model.assets.spend.entity.SpendAmount;
import project.fin_the_pen.model.assets.spend.repository.SpendAmountRepository;
import project.fin_the_pen.model.report.dto.ConsumeReportDetailRequestDto;
import project.fin_the_pen.model.report.dto.ConsumeReportRequestDTO;
import project.fin_the_pen.model.report.dto.ConsumeReportResponseDTO;
import project.fin_the_pen.model.report.dto.ReportRequestDemoDTO;
import project.fin_the_pen.model.report.repository.ReportRepository;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.repository.ScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.usersToken.repository.UsersTokenRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReportService {
    private final TokenManager tokenManager;
    private final UsersTokenRepository tokenRepository;
    private final ScheduleRepository scheduleRepository;
    private final ReportRepository reportRepository;
    private final CrudScheduleRepository crudScheduleRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final SpendAmountRepository spendAmountRepository;

    public HashMap<Object, Object> reportHome(ReportRequestDemoDTO dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String date = dto.getDate();

        try {
            if (dto.getDate() == null) {
                throw new RuntimeException();
            }

            HashMap<Object, Object> responseMap = new HashMap<>();

            List<String> findAmountList = inquiryAmountList(dto);

            String parsingDate = dto.getDate().substring(0, 7);
//            Optional<String> optionalGoalAmount = reportRepository.findByAmountAndUserIdAndDate(parsingDate, dto.getUserId());
            Optional<SpendAmount> optionalSpendAmount = spendAmountRepository.findByUserIdAndStartDate(userId, parsingDate);

            log.info("size:{}", findAmountList.size());
            log.info("실제:{}", optionalSpendAmount.isEmpty());

            // TODO!!!! 이 부분 로직 다시 검토
            if (findAmountList.isEmpty() || optionalSpendAmount.isEmpty()) {
                // 1 번
                responseMap.put("date", date);

                // 2번
                responseMap.put("totalSpentToday", "0");

                HashMap<Object, Object> expenditureMap = new HashMap<>();
                expenditureMap.put("goal_amount", "0");

                responseMap.put("expenseGoalAmount", "0");

                // 4번
                responseMap.put("availableAmount", "0");

                // 6번
                expenditureMap.put("goal_amount", "0");

                expenditureMap.put("1st_month_Amount", "0");

                expenditureMap.put("last_month_Amount", "0");

                expenditureMap.put("result_amount", "0");

                responseMap.put("expenditure_this_month", expenditureMap);

                responseMap.put("category_consume_report", "0");

                HashMap<Object, Object> fixedMap = new HashMap<>();


                LocalDate parseDate = LocalDate.parse(date);
                LocalDate previousDate = parseDate.minusMonths(1);

                fixedMap.put("current_month", date);
                fixedMap.put("previous_month", previousDate.toString());
                fixedMap.put("fixed_deposit", "0");

                fixedMap.put("previous_diff_plus", "0");

                fixedMap.put("fixed_withdraw", "0");

                fixedMap.put("previous_diff_minus", "0");

                responseMap.put("Nmonth_fixed", fixedMap);

                HashMap<Object, Object> monthReportMap = new HashMap<>();
                monthReportMap.put("second_previous", "0");
                monthReportMap.put("previous", "0");
                monthReportMap.put("current", "0");

                responseMap.put("month_report", monthReportMap);

                return responseMap;
            }

            log.info(String.valueOf(findAmountList.get(2)));

            int amountSum = findAmountList
                    .stream()
                    .mapToInt(Integer::parseInt)
                    .sum();

            // 1 번
            responseMap.put("date", dto.getDate());

            // 2번
            responseMap.put("totalSpentToday", String.valueOf(amountSum));

            // 3번
            String goalAmount = optionalSpendAmount.get().getSpendGoalAmount();

            log.info("3번 goalAmount:{}", goalAmount);

            HashMap<Object, Object> expenditureMap = new HashMap<>();

            Integer parseGoalAmount = Integer.valueOf(goalAmount);
            responseMap.put("expenseGoalAmount", String.valueOf(parseGoalAmount));

            // 4번
            int availableAmount = Integer.parseInt(goalAmount) - amountSum;
            responseMap.put("availableAmount", String.valueOf(availableAmount));

            // 6번
            expenditureMap.put("goal_amount", String.valueOf(parseGoalAmount));

            // 6-1 이번 달 1일부터 금일까지 사용한 금액 (보라색) : 사용한 금액
            String dtoDate = dto.getDate();
            LocalDate parseStartDate = LocalDate.parse(dtoDate, formatter).withDayOfMonth(1);

            List<String> byAmount1stMonth = crudScheduleRepository.findByAmountMonth(dto.getUserId(), PriceType.Minus, parseStartDate.toString(), dto.getDate());
            int amount1stMonthSum = byAmount1stMonth.stream().mapToInt(Integer::parseInt).sum();
            expenditureMap.put("1st_month_Amount", amount1stMonthSum);

            // 6-2 이번달 금일 이후, 금월 지출 예정 금액 (핑크색) : 지출 예정
            LocalDate stDate = LocalDate.parse(dto.getDate(), formatter).plusDays(1);
            LocalDate endDate = stDate.withDayOfMonth(stDate.lengthOfMonth());
            log.info(stDate.toString());

            List<String> byAmountLastMonth = crudScheduleRepository.findByAmountMonth(dto.getUserId(), PriceType.Minus, stDate.toString(), endDate.toString());
            int amountLastMonthSum = byAmountLastMonth.stream().mapToInt(Integer::parseInt).sum();
            expenditureMap.put("last_month_Amount", amountLastMonthSum);

            // 6-3 이번 달 사용 가능 금액 (지출 목표액 - 이번달 금일까지 사용한 총합) (하늘색) : 사용가능 금액
            int resultSum = Integer.parseInt(goalAmount) - amount1stMonthSum;

            expenditureMap.put("result_amount", String.valueOf(resultSum));


            responseMap.put("expenditure_this_month", expenditureMap);

            List<Map.Entry<Object, Object>> consumeList = new ArrayList<>(consumeReportInquiry(dto.getUserId(), dto.getDate()).entrySet());

            for (Map.Entry<Object, Object> entry : consumeList) {
                responseMap.put("category_consume_report", entry.getValue());
            }


            HashMap<Object, Object> fixedMap = new HashMap<>();

            try {
                // 7번
                LocalDate parseCurrentDate = LocalDate.parse(dto.getDate(), formatter);

                LocalDate parseBeforeDate = parseCurrentDate.minusMonths(1);
                LocalDate beforeStartDate = parseBeforeDate.withDayOfMonth(1);
                LocalDate beforeEndDate = parseBeforeDate.withDayOfMonth(parseCurrentDate.lengthOfMonth());

                List<String> beforePlusFixedAmount =
                        crudScheduleRepository.findByFixedAmountMonth(dto.getUserId(), PriceType.Plus, true, beforeStartDate.toString(), beforeEndDate.toString());

                int beforePlusSum = beforePlusFixedAmount.stream().mapToInt(Integer::parseInt).sum();
                log.info(String.valueOf(beforePlusSum));

                List<String> beforeMinusFixedAmount =
                        crudScheduleRepository.findByFixedAmountMonth(dto.getUserId(), PriceType.Minus, true, beforeStartDate.toString(), beforeEndDate.toString());

                int beforeMinusSum = beforeMinusFixedAmount.stream().mapToInt(Integer::parseInt).sum();
                log.info(String.valueOf(beforeMinusSum));

                LocalDate currentStartDate = parseCurrentDate.withDayOfMonth(1);
                LocalDate currentEndDate = parseCurrentDate.withDayOfMonth(parseCurrentDate.lengthOfMonth());

                List<String> currentPlusFixedAmount =
                        crudScheduleRepository.findByFixedAmountMonth(dto.getUserId(), PriceType.Plus, true, currentStartDate.toString(), currentEndDate.toString());
                int currentPlusSum = currentPlusFixedAmount.stream().mapToInt(Integer::parseInt).sum();

                List<String> currentMinusFixedAmount =
                        crudScheduleRepository.findByFixedAmountMonth(dto.getUserId(), PriceType.Minus, true, currentStartDate.toString(), currentEndDate.toString());
                int currentMinusSum = currentMinusFixedAmount.stream().mapToInt(Integer::parseInt).sum();

                LocalDate previousDate = parseCurrentDate.minusMonths(1);

                fixedMap.put("current_month", dtoDate);
                fixedMap.put("previous_month", previousDate.toString());
                fixedMap.put("fixed_deposit", String.valueOf(currentPlusSum));

                BiFunction<Integer, Integer, String> depositBiFunc = (current, previous) -> {
                    int diff = current - previous;

                    if (diff < 0) {
                        return "-" + diff;
                    } else {
                        return "+" + diff;
                    }
                };
                fixedMap.put("previous_diff_plus", depositBiFunc.apply(currentPlusSum, beforePlusSum));

                fixedMap.put("fixed_withdraw", currentMinusSum);

                BiFunction<Integer, Integer, String> withDrawBiFunc = (current, previous) -> {
                    int diff = current - previous;

                    if (diff < 0) {
                        return "+" + diff;
                    } else {
                        return "-" + diff;
                    }
                };

                fixedMap.put("previous_diff_minus", withDrawBiFunc.apply(currentMinusSum, beforeMinusSum));

                responseMap.put("Nmonth_fixed", fixedMap);
            } catch (NullPointerException e) {
                fixedMap.put("data", "error");
            }


            //8번
            LocalDate currentDate = LocalDate.parse(dto.getDate(), formatter);
            LocalDate previous = currentDate.minusMonths(1);
            LocalDate secondPrevious = previous.minusMonths(1);

            HashMap<Object, Object> monthReportMap = new HashMap<>();
            monthReportMap.put("second_previous", monthSum(dto.getUserId(), secondPrevious));
            monthReportMap.put("previous", monthSum(dto.getUserId(), previous));
            monthReportMap.put("current", monthSum(dto.getUserId(), currentDate));

            responseMap.put("month_report", monthReportMap);

            return responseMap;
        } catch (RuntimeException e) {
            throw new NotFoundDataException("찾는 데이터가 없습니다.");
        }
    }

    private int monthSum(String userId, LocalDate date) {
        return crudScheduleRepository.findByAmountMonth(userId,
                        PriceType.Minus,
                        date.withDayOfMonth(1).toString(),
                        date.toString())
                .stream()
                .mapToInt(Integer::parseInt)
                .sum();
    }


    // request dto date : 2024-02-01
    public Map<Object, Object> inquiryReport(ConsumeReportRequestDTO dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String date = dto.getDate();
        log.info(date);

//        LocalDateTime nowDateTime = LocalDateTime.now();
        LocalDateTime nowDateTime = LocalDateTime.of(2024, 2, 1, 23, 0);

        try {
            Map<Object, Object> responseMap = consumeReportInquiry(userId, date);
            Optional<String> optionalAmount = reportRepository.findByContainAmountAndUserIdAndDate(date, userId);

            List<Schedule> findList = crudScheduleRepository.findByStartDate(userId, date);

            responseMap.put("month", date);

            if (optionalAmount.isPresent()) {
                responseMap.put("goal_amount", optionalAmount.get());
                int dayExpense = 0;

                if (!findList.isEmpty()) {
                    dayExpense = findList.stream()
                            .filter(schedule -> schedule.getPriceType() != PriceType.Plus)
                            .map(schedule -> {
                                LocalTime localTime = LocalTime.parse(schedule.getStartTime());
                                LocalDateTime localDateTime = LocalDateTime.of(LocalDate.parse(date), localTime);
                                return new AbstractMap.SimpleEntry<>(localDateTime, Integer.parseInt(schedule.getAmount()));
                            })
                            .filter(entry -> entry.getKey().isBefore(nowDateTime))
                            .mapToInt(Map.Entry::getValue)
                            .sum();
                } else {
                    responseMap.put("expense", "?");
                }

                responseMap.put("expense", String.valueOf(dayExpense));
            } else {
                responseMap.put("month_amount", "?");
            }

            return responseMap;
        } catch (Exception e) {
            throw new RuntimeException("error");
        }
    }

    /**
     * TODO
     *  카테고리 소비 목표 금액은 설정 => 자산 페이지에서
     *
     * @param dto
     * @param request
     * @return
     */
    public Map<Object, Object> inquiryCategoryDetail(ConsumeReportDetailRequestDto dto, HttpServletRequest request) {
        String date = dto.getDate();
        String userId = dto.getUserId();
        String category = dto.getCategory();

        HashMap<Object, Object> responseMap = new HashMap<>();


//        실제로 사용할 값
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = localDateTime.withDayOfMonth(1);

        // 현재 시간 값 (가정)
//        LocalDateTime localDateTime = LocalDateTime.of(2024, 2, 13, 14, 0);

        List<Schedule> categoryList = crudScheduleRepository.findByCategoryBetweenDate(userId, category, date);

        log.info("find list size:{}", categoryList.size());

        int categoryExpense = 0;
        int categoryExpect = 0;
        int categoryBalance = 0;

        List<Schedule> responseList = new ArrayList<>();

        for (Schedule schedule : categoryList) {
            if (schedule.getPriceType() == PriceType.Minus) {
                // DB에 저장되어 있는 일정을 parsing -> 위의 localDateTime와 비교해서 예정 값 끌어오려고
                LocalDate parseDate = LocalDate.parse(schedule.getStartDate());
                LocalTime parseTime = LocalTime.parse(schedule.getStartTime(), timeFormatter);

                LocalDateTime parseDateTime = LocalDateTime.of(parseDate.getYear(),
                        parseDate.getMonth(),
                        parseDate.getDayOfMonth(),
                        parseTime.getHour(),
                        parseTime.getMinute());

                if (parseDateTime.isBefore(localDateTime)) {
                    categoryExpense += Integer.parseInt(schedule.getAmount());
                } else {
                    categoryExpect += Integer.parseInt(schedule.getAmount());
                }
                responseList.add(schedule);
            }
        }

        responseMap.put("current_date", dto.getDate());
        responseMap.put("category", dto.getCategory());
        responseMap.put("category_expense", String.valueOf(categoryExpense));
        responseMap.put("category_expect", String.valueOf(categoryExpect));
        responseMap.put("month_schedule", responseList);
        responseMap.put("schedule_count", responseList.size());


        return responseMap;
    }

    private List<String> inquiryAmountList(ReportRequestDemoDTO dto) {
        return crudScheduleRepository.findByAmount(dto.getDate(), dto.getUserId(), PriceType.Minus);
    }

    @NotNull
    private Map<Object, Object> consumeReportInquiry(String userId, String date) throws RuntimeException {
        Map<String, Integer> map = new HashMap<>();
        List<Schedule> responseArray = crudScheduleRepository.findByScheduleFromStartDate(userId, date);

        if (responseArray.isEmpty()) {
            HashMap<Object, Object> responseMap = new HashMap<>();
            responseMap.put("data", "error");
            return responseMap;
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

        List<Map.Entry<String, Integer>> topEntries = entryList.subList(0, Math.min(5, entryList.size()));
        int sum = topEntries.stream().mapToInt(Map.Entry::getValue).sum();

        List<ConsumeReportResponseDTO> consumeList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : topEntries) {
            int percentage = (int) ((entry.getValue() * 100.0) / sum);
            consumeList.add(new ConsumeReportResponseDTO(entry.getKey(), entry.getValue(), String.valueOf(percentage)));
        }

        HashMap<Object, Object> responseMap = new HashMap<>();
        responseMap.put("data", consumeList);

        return responseMap;
    }
}
