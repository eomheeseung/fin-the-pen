package project.fin_the_pen.model.assets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;
import project.fin_the_pen.model.assets.domain.entity.Asserts;
import project.fin_the_pen.model.assets.domain.entity.PersonalGoal;
import project.fin_the_pen.model.assets.domain.type.PersonalCriteria;
import project.fin_the_pen.model.assets.dto.personal.PersonalRequestDto;
import project.fin_the_pen.model.assets.dto.personal.PersonalResponseDto;
import project.fin_the_pen.model.assets.dto.targetAmount.TargetAmountRequestDto;
import project.fin_the_pen.model.assets.dto.targetAmount.TargetAmountResponseDto;
import project.fin_the_pen.model.assets.repository.AssertsRepository;
import project.fin_the_pen.model.assets.repository.PersonalGoalRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetsService {
    private final AssertsRepository assertsRepository;
    private final PersonalGoalRepository personalGoalRepository;


    public boolean setTargetAmount(TargetAmountRequestDto dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String yearsGoalAmount = dto.getYearsGoalAmount();

        try {
            Optional<Asserts> findAsserts = assertsRepository.findByUserId(userId);

            Asserts asserts;

            if (findAsserts.isPresent()) {
                asserts = findAsserts.get();
                asserts.update(userId, yearsGoalAmount, String.valueOf(Integer.parseInt(yearsGoalAmount) / 12));
            } else {
                asserts = Asserts.builder()
                        .userId(userId)
                        .yearsGoalAmount(yearsGoalAmount)
                        .monthGoalAmount(String.valueOf(Integer.parseInt(yearsGoalAmount) / 12))
                        .build();
            }

            assertsRepository.save(asserts);
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
            Optional<Asserts> findAsserts = assertsRepository.findByUserId(userId);

            if (findAsserts.isPresent()) {
                Asserts asserts = findAsserts.get();

                responseDto.setKeyId(String.valueOf(asserts.getId()));
                responseDto.setYearsGoalAmount(asserts.getYearsGoalAmount());
                responseDto.setMonthsGoalAmount(asserts.getMonthGoalAmount());

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
        PersonalCriteria criteria;

        if (dto.getCriteria().equals(PersonalCriteria.MONTH.getName())) {
            criteria = PersonalCriteria.MONTH;
        } else {
            criteria = PersonalCriteria.DAY;
        }

        String period = dto.getPeriod();
        String requiredAmount = dto.getRequiredAmount();
        boolean remittance = dto.isRemittance();
        boolean popOn = dto.isPopOn();

        try {
            Optional<PersonalGoal> findPersonalGoal = personalGoalRepository.findByUserId(userId);

            PersonalGoal personalGoal;

            if (findPersonalGoal.isPresent()) {
                personalGoal = findPersonalGoal.get();
                personalGoal.update(userId, goalName, period, criteria, requiredAmount, remittance, popOn);


            } else {
                personalGoal = PersonalGoal.builder()
                        .userId(userId)
                        .goalName(goalName)
                        .period(period)
                        .requiredAmount(requiredAmount)
                        .isPopOn(popOn)
                        .criteria(criteria)
                        .isRemittance(remittance).build();
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

        if (optional.isPresent()) {
            PersonalGoal findPersonalGoal = optional.get();

            responseDto.setUserId(findPersonalGoal.getUserId());
            responseDto.setGoalName(findPersonalGoal.getGoalName());
            responseDto.setCriteria(findPersonalGoal.getCriteria().getName());
            responseDto.setPeriod(findPersonalGoal.getPeriod());
            responseDto.setGoalAmount(findPersonalGoal.getRequiredAmount());
            responseDto.setIsPopOn(String.valueOf(findPersonalGoal.getIsPopOn()));
            responseDto.setIsRemittance(String.valueOf(findPersonalGoal.getIsRemittance()));
            responseDto.setRequiredAmount(findPersonalGoal.getRequiredAmount());

        } else {
            responseDto.setUserId("?");
            responseDto.setGoalName("?");
            responseDto.setCriteria("?");
            responseDto.setPeriod("?");
            responseDto.setGoalAmount("?");
            responseDto.setIsRemittance("?");
            responseDto.setIsPopOn("?");
            responseDto.setRequiredAmount("?");
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
            Optional<Asserts> findAsserts = assertsRepository.findByUserId(userId);

            findAsserts.ifPresent(assertsRepository::delete);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }


}
