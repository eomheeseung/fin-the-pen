package project.fin_the_pen.model.assets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;
import project.fin_the_pen.model.assets.saving.domain.entity.SavingGoal;
import project.fin_the_pen.model.assets.saving.domain.entity.PersonalGoal;
import project.fin_the_pen.model.assets.saving.dto.personal.PersonalRequestDto;
import project.fin_the_pen.model.assets.saving.dto.personal.PersonalResponseDto;
import project.fin_the_pen.model.assets.saving.dto.savingGoal.TargetAmountRequestDto;
import project.fin_the_pen.model.assets.saving.dto.savingGoal.TargetAmountResponseDto;
import project.fin_the_pen.model.assets.saving.repository.AssertsRepository;
import project.fin_the_pen.model.assets.saving.repository.PersonalGoalRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetsService {
    private final AssertsRepository assertsRepository;
    private final PersonalGoalRepository personalGoalRepository;


    //TODO token validation이 안되는 것 같음
    /**
     * 저축 목표액 설정 -> SavingGoal table
     *
     * @param dto
     * @param request
     * @return
     */
    public boolean setTargetAmount(TargetAmountRequestDto dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String yearsGoalAmount = dto.getYearsGoalAmount();

        try {
            Optional<SavingGoal> findAsserts = assertsRepository.findByUserId(userId);

            SavingGoal savingGoal;

            if (findAsserts.isPresent()) {
                savingGoal = findAsserts.get();
                savingGoal.update(userId, yearsGoalAmount, String.valueOf(Integer.parseInt(yearsGoalAmount) / 12));
            } else {
                savingGoal = SavingGoal.builder()
                        .userId(userId)
                        .yearsGoalAmount(yearsGoalAmount)
                        .monthGoalAmount(String.valueOf(Integer.parseInt(yearsGoalAmount) / 12))
                        .build();
            }

            assertsRepository.save(savingGoal);
        } catch (RuntimeException e) {
            return false;
        }

        return true;
    }

    /**
     * 자산 설정 view method
     *
     * @param userId
     * @param request
     * @return
     */
    public TargetAmountResponseDto viewTargetAmount(String userId, HttpServletRequest request) {
        TargetAmountResponseDto responseDto = new TargetAmountResponseDto();
        responseDto.setUserId(userId);

        try {
            Optional<SavingGoal> findAsserts = assertsRepository.findByUserId(userId);

            if (findAsserts.isPresent()) {
                SavingGoal savingGoal = findAsserts.get();

                responseDto.setKeyId(String.valueOf(savingGoal.getId()));
                responseDto.setYearsGoalAmount(savingGoal.getYearsGoalAmount());
                responseDto.setMonthsGoalAmount(savingGoal.getMonthGoalAmount());

            } else {
                throw new NotFoundDataException("no data");
            }
        } catch (NotFoundDataException e) {
            responseDto.setYearsGoalAmount("?");
            responseDto.setMonthsGoalAmount("?");
        }
        return responseDto;
    }

    /**
     * 개인 목표 설정
     *
     * @param dto
     * @param request
     * @return
     */
    public boolean personalGoalSet(PersonalRequestDto dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String goalName = dto.getGoalName();
        String period = dto.getPeriod();
        String monthAmount = dto.getMonthAmount();
        String goalAmount = dto.getGoalAmount();


        try {
            Optional<PersonalGoal> findPersonalGoal = personalGoalRepository.findByUserId(userId);

            PersonalGoal personalGoal;

            if (findPersonalGoal.isPresent()) {
                personalGoal = findPersonalGoal.get();
                personalGoal.update(userId, goalName, period, goalAmount, monthAmount);

            } else {
                personalGoal = PersonalGoal.builder()
                        .userId(userId)
                        .goalName(goalName)
                        .period(period)
                        .goalAmount(goalAmount)
                        .monthAmount(monthAmount).build();
            }

            personalGoalRepository.save(personalGoal);
            return true;
        } catch (NotFoundDataException e) {
            return false;
        }
    }

    public PersonalResponseDto viewPersonalGoal(String userId, HttpServletRequest request) {
        Optional<PersonalGoal> optional = personalGoalRepository.findByUserId(userId);

        PersonalResponseDto responseDto = new PersonalResponseDto();
        responseDto.setUserId(userId);

        if (optional.isPresent()) {
            PersonalGoal findPersonalGoal = optional.get();

            responseDto.setGoalName(findPersonalGoal.getGoalName());
            responseDto.setPeriod(findPersonalGoal.getPeriod());
            responseDto.setGoalAmount(findPersonalGoal.getGoalAmount());
            responseDto.setMonthAmount(findPersonalGoal.getMonthAmount());

        } else {
            responseDto.setGoalName("?");
            responseDto.setPeriod("?");
            responseDto.setGoalAmount("?");
            responseDto.setMonthAmount("?");
        }
        return responseDto;
    }

    /**
     * 자산 설정 초기화 method
     *
     * @param userId
     * @param request
     * @return
     */
    public boolean initTargetAmount(String userId, HttpServletRequest request) {
        try {
            Optional<SavingGoal> findAsserts = assertsRepository.findByUserId(userId);

            findAsserts.ifPresent(assertsRepository::delete);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }


}
