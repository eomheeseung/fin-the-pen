package project.fin_the_pen.model.assets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;
import project.fin_the_pen.model.assets.periodic.dto.PeriodAmountDeleteRequestDto;
import project.fin_the_pen.model.assets.periodic.dto.PeriodicAmountRequestDto;
import project.fin_the_pen.model.assets.periodic.entity.PeriodicAmount;
import project.fin_the_pen.model.assets.periodic.repository.PeriodicRepository;
import project.fin_the_pen.model.schedule.type.PriceType;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeriodicService {
    private final PeriodicRepository periodicRepository;

    /**
     * 정기 입출 금액 등록
     *
     * @param dto
     * @param request
     * @return
     */
    public boolean setPeriodicAmount(PeriodicAmountRequestDto dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String amount = dto.getAmount();
        String period = dto.getPeriod();
        Boolean fixed = dto.getFixed();
        String nickName = dto.getNickName();
        PriceType priceType = dto.getPriceType();
        String selectDate = dto.getSelectDate();

        try {
            Optional<PeriodicAmount> optionalPeriodic = periodicRepository.findByUserIdAndNickName(userId, nickName);

            if (optionalPeriodic.isPresent()) {
                periodicRepository.save(optionalPeriodic.get().update(userId, nickName, amount, priceType, period, selectDate, fixed));
            } else {
                // 등록
                PeriodicAmount savePeriod = PeriodicAmount
                        .builder()
                        .userId(userId)
                        .amount(amount)
                        .date(selectDate)
                        .fixed(fixed)
                        .nickName(nickName)
                        .period(period)
                        .priceType(priceType)
                        .build();

                periodicRepository.save(savePeriod);
            }

        } catch (NotFoundDataException e) {
            return false;
        }

        return true;
    }

    /**
     * 정기 입출금액 화면
     */
    public HashMap<Object, Object> viewPeriodAmount(String userId, HttpServletRequest request) {
        List<PeriodicAmount> findList = periodicRepository.findByUserId(userId);

        HashMap<Object, Object> responseMap = new HashMap<>();

        if (findList.isEmpty()) {
            responseMap.put("data", "no data");
        } else {
            List<PeriodicAmount> depositList =
                    findList.stream()
                            .filter(periodicAmount -> periodicAmount.getPriceType().equals(PriceType.Plus))
                            .collect(Collectors.toList());

            List<PeriodicAmount> withdrawList =
                    findList.stream()
                            .filter(periodicAmount -> periodicAmount.getPriceType().equals(PriceType.Minus))
                            .collect(Collectors.toList());

            responseMap.put("deposit", depositList);
            responseMap.put("withdraw", withdrawList);
        }

        return responseMap;
    }

    public boolean deletePeriodAmount(PeriodAmountDeleteRequestDto dto, HttpServletRequest request) {
        Long id = dto.getId();
        String userId = dto.getUserId();

        Optional<PeriodicAmount> optionalPeriodic = periodicRepository.findByIdAndUserId(id, userId);

        try {
            if (optionalPeriodic.isPresent()) {
                periodicRepository.delete(optionalPeriodic.get());
            } else {
                return true;
            }
        } catch (RuntimeException e) {
            return false;
        }

        return true;
    }
}
