package project.fin_the_pen.model.report.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;
import project.fin_the_pen.model.assets.category.entity.Category;
import project.fin_the_pen.model.assets.category.entity.SmallCategory;
import project.fin_the_pen.model.assets.category.repository.CategoryRepository;
import project.fin_the_pen.model.assets.category.repository.SmallCategoryRepository;
import project.fin_the_pen.model.assets.spend.entity.SpendAmount;
import project.fin_the_pen.model.assets.spend.entity.SpendAmountRegular;
import project.fin_the_pen.model.assets.spend.repository.SpendAmountRepository;
import project.fin_the_pen.model.report.dto.ConsumeReportDetailRequestDto;
import project.fin_the_pen.model.report.dto.ConsumeReportRequestDTO;
import project.fin_the_pen.model.report.dto.ConsumeReportResponseDTO;
import project.fin_the_pen.model.report.dto.ReportRequestDemoDTO;
import project.fin_the_pen.model.report.repository.ReportRepository;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

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
    private final CategoryRepository categoryRepository;
    //    private final TokenManager tokenManager;
//    private final UsersTokenRepository tokenRepository;
//    private final ScheduleRepository scheduleRepository;
    private final ReportRepository reportRepository;
    private final CrudScheduleRepository crudScheduleRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final SpendAmountRepository spendAmountRepository;

    private final SmallCategoryRepository smallCategoryRepository;


    public HashMap<Object, Object> reportHome(ReportRequestDemoDTO dto, HttpServletRequest request) {
        String currentDate = dto.getDate();
        String userId = dto.getUserId();

        try {
            if (currentDate == null) {
                throw new NotFoundDataException("no date");
            }

            HashMap<Object, Object> responseMap = new HashMap<>();

            // yyyy-MM 까지만 추출
            String subCurrentDate = currentDate.substring(0, 7);
            log.info("1. currentDate substring 추출: {}", subCurrentDate);

            // 지출 목표액 가져옴 (04-05, 지출 목표액이 일괄처리가 아닌 경우 OFF가 우선순위가 높음.)
            Optional<SpendAmount> optionalSpendOffAmount =
                    spendAmountRepository.findByUserIdAndStartDate(userId, subCurrentDate)
                            .filter(spendAmount -> spendAmount.getRegular().equals(SpendAmountRegular.OFF));

            Optional<SpendAmount> optionalSpendOnAmount =
                    spendAmountRepository.findByUserIdAndStartDate(userId, subCurrentDate)
                            .filter(spendAmount -> spendAmount.getRegular().equals(SpendAmountRegular.ON));

            Optional<SpendAmount> optionalSpendAmount = Optional.empty();

            if (optionalSpendOffAmount.isPresent()) {
                optionalSpendAmount = optionalSpendOffAmount;
            } else {
                if (optionalSpendOnAmount.isPresent()) {
                    optionalSpendAmount = optionalSpendOnAmount;
                }
            }

            // 현재:03-27 /  03-01 ~ 03-27까지 minus 금액들
            LocalDate firstMonthDate = LocalDate.parse(currentDate, formatter).withDayOfMonth(1);
            log.info("1일:{}", firstMonthDate);
            log.info("현재:{}", currentDate);
            List<String> firstMonthMinusList = crudScheduleRepository.findByAmountMonth(userId, PriceType.Minus, firstMonthDate.toString(), currentDate);

            // 03-28 ~ 03-31까지의 minus 금액들
            LocalDate localFirstDate = LocalDate.parse(currentDate, formatter).plusDays(1);
            log.info("현재 +1:{}", localFirstDate);
            LocalDate localLastDate = localFirstDate.withDayOfMonth(localFirstDate.lengthOfMonth());
            log.info("달의 마지막 날:{}", localLastDate);
            List<String> lastMonthMinusList = crudScheduleRepository.findByAmountMonth(userId, PriceType.Minus, localFirstDate.toString(), localLastDate.toString());

            // 2024-03의 모든 일정의 minus 금액들
            List<Schedule> allMonthPlusList = crudScheduleRepository.findByStartDateAndeAndEndDatePriceType(userId, PriceType.Plus, firstMonthDate.toString(), localLastDate.toString());
            List<Schedule> allMonthMinusList = crudScheduleRepository.findByStartDateAndeAndEndDatePriceType(userId, PriceType.Minus, firstMonthDate.toString(), localLastDate.toString());


            // 1번
            responseMap.put("current_date", subCurrentDate);

            // 2번
            if (!firstMonthMinusList.isEmpty()) {
                int firstMonthAmount = firstMonthMinusList.stream().mapToInt(Integer::parseInt).sum();
                log.info("2번 오늘까지 소비한 값 : {}", firstMonthAmount);
                responseMap.put("first_month_amount", firstMonthAmount);
            } else {
                responseMap.put("first_month_amount", "0");
            }

            // 3번
            if (optionalSpendAmount.isPresent()) {
                SpendAmount spendAmount = optionalSpendAmount.get();
                responseMap.put("spend_amount", spendAmount.getSpendGoalAmount());
            } else {
                responseMap.put("spend_amount", "0");
            }

            // 4번
            getAvailableAmount(firstMonthMinusList, lastMonthMinusList, optionalSpendAmount, responseMap);

            // 5번
            getCategoryConsumeList(userId, firstMonthDate, currentDate, responseMap);

            // 6번
            getConsumeForecast(optionalSpendAmount, responseMap, firstMonthMinusList, lastMonthMinusList);

            // 7번
            LocalDate previousStartDate = firstMonthDate.minusMonths(1);
            LocalDate previousEndDate = previousStartDate.withDayOfMonth(previousStartDate.lengthOfMonth());

            List<Schedule> allPreviousMonthPlusList =
                    crudScheduleRepository.findByStartDateAndeAndEndDatePriceType(userId, PriceType.Plus, previousStartDate.toString(), previousEndDate.toString());

            List<Schedule> allPreviousMonthMinusList =
                    crudScheduleRepository.findByStartDateAndeAndEndDatePriceType(userId, PriceType.Minus, previousStartDate.toString(), previousEndDate.toString());

            getFixedPlusMinusAmount(previousStartDate, allMonthPlusList, allMonthMinusList, allPreviousMonthPlusList, allPreviousMonthMinusList, responseMap);


            // 8번
            LocalDate secondStartDate = previousStartDate.minusMonths(1);
            LocalDate secondEndDate = secondStartDate.withDayOfMonth(secondStartDate.lengthOfMonth());

            List<Schedule> allSecondMonthMinusList =
                    crudScheduleRepository.findByStartDateAndeAndEndDatePriceType(userId, PriceType.Minus, secondStartDate.toString(), secondEndDate.toString());

            getMonthlyReport(allMonthMinusList, allPreviousMonthMinusList, allSecondMonthMinusList, responseMap);


            return responseMap;
        } catch (RuntimeException e) {
            throw new NotFoundDataException("찾는 데이터가 없습니다.");
        }
    }

    private void getAvailableAmount(List<String> firstMonthMinusList, List<String> lastMonthMinusList, Optional<SpendAmount> optionalSpendAmount, HashMap<Object, Object> responseMap) {
        int firstMonthAmount = 0;
        int lastMonthAmount = 0;
        int availableAmount = 0;

        if (!firstMonthMinusList.isEmpty()) {
            firstMonthAmount = firstMonthMinusList.stream().mapToInt(Integer::parseInt).sum();
        }
        if (!lastMonthMinusList.isEmpty()) {
            lastMonthAmount = lastMonthMinusList.stream().mapToInt(Integer::parseInt).sum();
        }

        if (optionalSpendAmount.isPresent()) {
            availableAmount = Integer.parseInt(optionalSpendAmount.get().getSpendGoalAmount()) - firstMonthAmount - lastMonthAmount;
        }

        responseMap.put("available_amount", availableAmount);
    }

    // 8번
    private void getMonthlyReport(List<Schedule> allMonthMinusList, List<Schedule> allPreviousMonthMinusList, List<Schedule> allSecondMonthMinusList, HashMap<Object, Object> responseMap) {
        int currentMinusSum = 0;
        int previousMinusSum = 0;
        int secondMinusSum = 0;

        if (!allMonthMinusList.isEmpty()) {
            currentMinusSum = allMonthMinusList.stream().map(Schedule::getAmount).mapToInt(Integer::parseInt).sum();
        }

        if (!allPreviousMonthMinusList.isEmpty()) {
            previousMinusSum = allPreviousMonthMinusList.stream().map(Schedule::getAmount).mapToInt(Integer::parseInt).sum();
        }

        if (!allSecondMonthMinusList.isEmpty()) {
            secondMinusSum = allSecondMonthMinusList.stream().map(Schedule::getAmount).mapToInt(Integer::parseInt).sum();
        }

        HashMap<Object, Object> monthlyMap = new HashMap<>();
        monthlyMap.put("current_amount", currentMinusSum);
        monthlyMap.put("previous_amount", previousMinusSum);
        monthlyMap.put("second_amount", secondMinusSum);

        responseMap.put("monthly_report", monthlyMap);
    }

    // 7번
    private void getFixedPlusMinusAmount(LocalDate previousStartDate, List<Schedule> allMonthPlusList, List<Schedule> allMonthMinusList,
                                         List<Schedule> allPreviousMonthPlusList, List<Schedule> allPreviousMonthMinusList,
                                         HashMap<Object, Object> responseMap) {
        int currentFixedPlusSum = 0;
        int currentFixedMinusSum = 0;
        int previousFixedPlusSum = 0;
        int previousFixedMinusSum = 0;

        HashMap<Object, Object> fixedMap = new HashMap<>();

        if (!allMonthPlusList.isEmpty()) {
            currentFixedPlusSum = allMonthPlusList.stream()
                    .filter(schedule -> !schedule.getRepeatKind().equals(RepeatKind.NONE.toString()))
                    .map(Schedule::getAmount)
                    .mapToInt(Integer::parseInt)
                    .sum();
        }
        if (!allMonthMinusList.isEmpty()) {
            currentFixedMinusSum = allMonthMinusList.stream()
                    .filter(schedule -> !schedule.getRepeatKind().equals(RepeatKind.NONE.toString()))
                    .map(Schedule::getAmount)
                    .mapToInt(Integer::parseInt)
                    .sum();
        }
        if (!allPreviousMonthPlusList.isEmpty()) {
            previousFixedPlusSum = allPreviousMonthPlusList.stream()
                    .filter(schedule -> !schedule.getRepeatKind().equals(RepeatKind.NONE.toString()))
                    .map(Schedule::getAmount)
                    .mapToInt(Integer::parseInt)
                    .sum();
        }

        if (!allPreviousMonthMinusList.isEmpty()) {
            previousFixedMinusSum = allPreviousMonthMinusList.stream()
                    .filter(schedule -> !schedule.getRepeatKind().equals(RepeatKind.NONE.toString()))
                    .map(Schedule::getAmount)
                    .mapToInt(Integer::parseInt)
                    .sum();
        }

        fixedMap.put("current_fixed_plus", currentFixedPlusSum);
        fixedMap.put("previous_date", previousStartDate.toString().substring(0, 7));
        fixedMap.put("current_fixed_Minus", currentFixedMinusSum);

        BiFunction<Integer, Integer, String> biPlusFunction = (current, previous) -> {
            String value;
            if (current > previous) {
                value = "+" + (current - previous);

            } else if (current < previous) {
                value = "-" + (current - previous);
            } else {
                value = "0";
            }
            return value;
        };

        BiFunction<Integer, Integer, String> biMinusFunction = (current, previous) -> {
            String value;

            if (current < previous) {
                value = "+" + (current - previous);

            } else if (current > previous) {
                value = "-" + (current - previous);
            } else {
                value = "0";
            }
            return value;
        };


        fixedMap.put("diff_plus", biPlusFunction.apply(currentFixedPlusSum, previousFixedPlusSum));
        fixedMap.put("diff_minus", biMinusFunction.apply(currentFixedMinusSum, previousFixedMinusSum));

        responseMap.put("Nmonth_fixed", fixedMap);
    }

    // 6번
    private void getConsumeForecast(Optional<SpendAmount> optionalSpendAmount,
                                    HashMap<Object, Object> responseMap,
                                    List<String> firstMonthMinusList,
                                    List<String> lastMonthMinusList) {
        HashMap<Object, Object> expenditureMap = new HashMap<>();

        if (optionalSpendAmount.isEmpty()) {
            expenditureMap.put("spend_amount", "0");
            expenditureMap.put("first_Nmonth_Amount", "0");
            expenditureMap.put("last_Nmonth_Amount", "0");
            expenditureMap.put("available_Nmonth_amount", "0");
            responseMap.put("expenditure_data", expenditureMap);
        } else {
            SpendAmount spendAmount = optionalSpendAmount.get();
            // 지출 목표액
            expenditureMap.put("spend_amount", spendAmount.getSpendGoalAmount());

            // n월 
            int firstNMonthAmount = 0;

            // n월 
            int lastNMonthAmount = 0;
            
            // n월 사용 가능 금액
            int availableNMonthAmount = 0;


            // firstMonthMinusList : 현재 달의 1일 부터 현재 달의 날까지 Minus들
            if (!firstMonthMinusList.isEmpty()) {
                firstNMonthAmount = firstMonthMinusList.stream().mapToInt(Integer::parseInt).sum();
                expenditureMap.put("first_Nmonth_Amount", String.valueOf(firstNMonthAmount));
            }

            // lastMonthMinusList : 현재 달의 날부터 현재 달의 마지막날까지 Minus들
            if (!lastMonthMinusList.isEmpty()) {
                lastNMonthAmount = lastMonthMinusList.stream().mapToInt(Integer::parseInt).sum();
                expenditureMap.put("first_Nmonth_Amount", String.valueOf(lastNMonthAmount));
            }

            availableNMonthAmount = Integer.parseInt(spendAmount.getSpendGoalAmount()) - firstNMonthAmount - lastNMonthAmount;
            expenditureMap.put("available_Nmonth_amount", String.valueOf(availableNMonthAmount));

            responseMap.put("expenditure_data", expenditureMap);
        }
    }


    // 5번
    private void getCategoryConsumeList(String userId, LocalDate firstMonthDate, String currentDate, HashMap<Object, Object> responseMap) {
        List<Schedule> byAmountMonth = crudScheduleRepository.findByStartDateAndeAndEndDatePriceType(userId, PriceType.Minus, firstMonthDate.toString(), currentDate);
        Map<String, Integer> map = new HashMap<>();

        if (byAmountMonth.isEmpty()) {
            responseMap.put("category_consume_list", "?");
        } else {
            byAmountMonth.forEach(schedule -> {
                String category = schedule.getCategory();
                Integer amount = Integer.parseInt(schedule.getAmount());

                if (schedule.getPriceType().equals(PriceType.Minus)) {
                    map.compute(category, (key, value) -> (value == null) ? amount : value + amount);
                }
            });

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

            responseMap.put("category_consume_list", consumeList);
        }
    }


    // request dto date : 2024-02-01
    public Map<Object, Object> inquiryReport(ConsumeReportRequestDTO dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String date = dto.getDate();
        log.info(date);

        LocalDateTime nowDateTime = LocalDateTime.now();
//        LocalDateTime nowDateTime = LocalDateTime.of(2024, 2, 1, 23, 0);

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
     * 소비리포트 상세
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

        // 2024-08-01
        String parseMonth = date.substring(0, 7);
        log.info("parsing month :{}", parseMonth);

        // 현재 지출한 금액
        int currentValue = 0;

        // 예정된 금액
        int expectValue = 0;

        // 카테고리 목표 지출액 - 현재 지출한 금액 - 예정된 금액
        int balanceValue = 0;

        Optional<Category> optionalCategory =
                categoryRepository.findByUserIdAndMediumNameAndDate(userId, category, parseMonth);

        Optional<SmallCategory> optionalSmallCategory = smallCategoryRepository.findBySmallName(category);
//        log.info(optionalSmallCategory.get().getSmallName());

        String categoryValue;
        int convertCategoryValue = 0;


        if (optionalCategory.isPresent()) {
            categoryValue = optionalCategory.get().getMediumValue();
            log.info(optionalCategory.get().getMediumValue());
            responseMap.put("category_goal_amount", categoryValue);
            convertCategoryValue = Integer.parseInt(categoryValue);

            return categoryCalcFunc(date, userId, category, convertCategoryValue, responseMap);

        } else if (optionalSmallCategory.isPresent()) {
            categoryValue = optionalSmallCategory.get().getSmallValue();
            log.info(optionalSmallCategory.get().getSmallName());
            responseMap.put("category_goal_amount", categoryValue);
            convertCategoryValue = Integer.parseInt(categoryValue);

            return categoryCalcFunc(date, userId, category, convertCategoryValue, responseMap);
        } else {
            categoryValue = "?";
            responseMap.put("category_goal_amount", categoryValue);
            responseMap.put("currentValue", String.valueOf(currentValue));
            responseMap.put("expectValue", String.valueOf(expectValue));
            responseMap.put("balanceValue", String.valueOf(balanceValue));
            return responseMap;
        }


        /*String categoryValue;
        int convertCategoryValue = 0;

        if (optionalCategory.isEmpty()) {
            categoryValue = "?";
            responseMap.put("category_goal_amount", categoryValue);
            responseMap.put("currentValue", String.valueOf(currentValue));
            responseMap.put("expectValue", String.valueOf(expectValue));
            responseMap.put("balanceValue", String.valueOf(balanceValue));
            return responseMap;

        } else {
            categoryValue = optionalCategory.get().getMediumValue();
            log.info(optionalCategory.get().getMediumValue());
            responseMap.put("category_goal_amount", categoryValue);
            convertCategoryValue = Integer.parseInt(categoryValue);
        }

        categoryCalcFunc(date, userId, category, convertCategoryValue, responseMap);

        return responseMap;*/

    }

    private Map<Object, Object> categoryCalcFunc(String date, String userId, String category, int convertCategoryValue, HashMap<Object, Object> responseMap) {
        int currentValue;
        int expectValue;
        int balanceValue;
        LocalDate currentDate = LocalDate.parse(date, formatter);
        LocalDate firstDate = currentDate.withDayOfMonth(1);
        LocalDate lastDate = currentDate.withDayOfMonth(currentDate.lengthOfMonth());


        List<Schedule> firstToCurrentList =
                crudScheduleRepository.findByStartDateAndEndDate(userId, firstDate.toString(), currentDate.toString());

        List<Schedule> currentToLastList =
                crudScheduleRepository.findByStartDateAndEndDate(userId, currentDate.plusDays(1).toString(), lastDate.toString());

        List<Schedule> wholeList =
                crudScheduleRepository.findByStartDateAndEndDate(userId, firstDate.toString(), lastDate.toString());

        currentValue = firstToCurrentList.stream()
                .filter(schedule -> schedule.getCategory().equals(category))
                .filter(schedule -> schedule.getPriceType().equals(PriceType.Minus))
                .mapToInt(schedule -> Integer.parseInt(schedule.getAmount()))
                .sum();

        expectValue = currentToLastList.stream().filter(schedule -> schedule.getCategory().equals(category))
                .filter(schedule -> schedule.getPriceType().equals(PriceType.Minus))
                .mapToInt(schedule -> Integer.parseInt(schedule.getAmount()))
                .sum();

        balanceValue = convertCategoryValue - currentValue - expectValue;

        responseMap.put("currentValue", String.valueOf(currentValue));
        responseMap.put("expectValue", String.valueOf(expectValue));
        responseMap.put("balanceValue", String.valueOf(balanceValue));

        responseMap.put("list", wholeList);


        responseMap.put("name", category + "소비 리포트");

        return responseMap;
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
