package project.fin_the_pen.model.assets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.model.assets.spend.dto.SpendAmountRequestDto;
import project.fin_the_pen.model.assets.spend.dto.SpendAmountResponseDto;
import project.fin_the_pen.model.assets.spend.entity.SpendAmount;
import project.fin_the_pen.model.assets.spend.entity.SpendAmountRegular;
import project.fin_the_pen.model.assets.spend.repository.SpendAmountRepository;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpendGoalAmountService {
    private final SpendAmountRepository spendAmountRepository;
    private final CrudScheduleRepository scheduleRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    // TODO 현재 일자가 2024-03인데 넘겨서 2024-04인 경우 지출액을 어떻게 보여줄 것인가
    public Map<String, SpendAmountResponseDto> viewSpendGoalAmount(String userId, String date, HttpServletRequest request) {
        List<SpendAmount> spendAmountList = spendAmountRepository.findByUserIdAndStartDateToList(userId, date);

        LocalDate now = LocalDate.now();
        LocalDate firstMonthDate = now.withDayOfMonth(1);
        List<String> firstMonthMinusList = scheduleRepository.findByAmountMonth(userId, PriceType.Minus, firstMonthDate.toString(), now.toString());

        HashMap<String, SpendAmountResponseDto> responseMap = new HashMap<>();

        log.info(String.valueOf(spendAmountList.size()));

        int first_month_amount = 0;

        if (!firstMonthMinusList.isEmpty()) {
            first_month_amount = firstMonthMinusList.stream().mapToInt(Integer::parseInt).sum();
        }

        if (!spendAmountList.isEmpty()) {
            Optional<SpendAmount> OnAmount = spendAmountList.stream()
                    .filter(spendAmount -> spendAmount.getRegular().equals(SpendAmountRegular.ON))
                    .findAny();

            Optional<SpendAmount> OffAmount = spendAmountList.stream()
                    .filter(spendAmount -> spendAmount.getRegular().equals(SpendAmountRegular.OFF))
                    .findAny();

            if (OffAmount.isPresent()) {
                SpendAmount OffSpendAmount = OffAmount.get();

                SpendAmountResponseDto responseDto = SpendAmountResponseDto.builder()
                        .spendGoalAmount(OffSpendAmount.getSpendGoalAmount())
                        .userId(OffSpendAmount.getUserId())
                        .startDate(OffSpendAmount.getStartDate())
                        .endDate(OffSpendAmount.getEndDate())
                        .date(date)
                        .spendAmount(String.valueOf(first_month_amount))
                        .build();

                responseMap.put("offSpendAmount", responseDto);
            }
            if (OnAmount.isPresent()) {
                SpendAmount OnSpendAmount = OnAmount.get();

                SpendAmountResponseDto responseDto = SpendAmountResponseDto.builder()
                        .spendGoalAmount(OnSpendAmount.getSpendGoalAmount())
                        .userId(OnSpendAmount.getUserId())
                        .startDate(OnSpendAmount.getStartDate())
                        .endDate(OnSpendAmount.getEndDate())
                        .date(date)
                        .spendAmount(String.valueOf(first_month_amount))
                        .build();

                responseMap.put("OnSpendAmount", responseDto);
            }
            if (spendAmountList.isEmpty()) {
                SpendAmountResponseDto responseDto = SpendAmountResponseDto.builder()
                        .userId(userId)
                        .spendGoalAmount("0")
                        .startDate("?")
                        .endDate("?")
                        .date(date)
                        .spendAmount(String.valueOf(first_month_amount))
                        .build();
                responseMap.put("data", responseDto);
            }
        }
        return responseMap;
    }


    /**
     * 지출 목표액 설정
     *
     * @param dto
     * @param request
     * @return
     */
    public boolean setSpendGoalAmount(SpendAmountRequestDto dto, HttpServletRequest request) {
        String spendGoalAmount = dto.getSpendGoalAmount();
        String startDate = dto.getStartDate();
        String endDate = dto.getEndDate();
        String userId = dto.getUserId();
        String regular = dto.getRegular();
        Boolean isBatch = dto.getIsBatch();

        YearMonth parseStartDate = YearMonth.parse(startDate, formatter);
        YearMonth parseEndDate = YearMonth.parse(endDate, formatter);

        try {
            // 정기가 아닌 경우
            if (regular.equals(SpendAmountRegular.OFF.toString())) {
                Optional<SpendAmount> optionalSpendAmount = spendAmountRepository.findByUserIdAndStartDate(userId, startDate);

                SpendAmount spendAmount;

                if (optionalSpendAmount.isPresent()) {
                    spendAmount = optionalSpendAmount.get();
                    spendAmount.update(userId, spendGoalAmount, startDate, endDate, SpendAmountRegular.OFF);
                } else {
                    spendAmount = SpendAmount.builder()
                            .spendGoalAmount(spendGoalAmount)
                            .endDate(endDate)
                            .startDate(startDate)
                            .regular(SpendAmountRegular.OFF)
                            .userId(userId)
                            .build();
                }
                spendAmountRepository.save(spendAmount);


            } else if (regular.equals(SpendAmountRegular.ON.toString())) {
                if (isBatch) {
                    Optional<SpendAmount> optionalSpendAmount = spendAmountRepository.findByUserIdAndStartDate(userId, startDate);

                    optionalSpendAmount.ifPresent(spendAmountRepository::delete);
                }
                regularOn(parseStartDate, parseEndDate, spendGoalAmount, userId);
            }
        } catch (RuntimeException e) {
            return false;
        }

        return true;
    }

    private void regularOn(YearMonth parseStartDate, YearMonth parseEndDate, String spendGoalAmount, String userId) {
        while (!parseStartDate.isAfter(parseEndDate)) {

            /*log.info("날짜:{}", parseStartDate);
            log.info("날짜:{}", parseEndDate);*/

            SpendAmount spendAmount = SpendAmount.builder()
                    .spendGoalAmount(spendGoalAmount)
                    .endDate(parseEndDate.toString())
                    .startDate(parseStartDate.toString())
                    .regular(SpendAmountRegular.ON)
                    .userId(userId)
                    .build();

            spendAmountRepository.save(spendAmount);

            parseStartDate = parseStartDate.plusMonths(1);
        }
    }
}
