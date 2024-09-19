package project.fin_the_pen.model.schedule.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.fin_the_pen.finClient.core.error.customException.ExecuteException;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;
import project.fin_the_pen.model.schedule.dto.ScheduleResponseDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;
import project.fin_the_pen.model.schedule.template.dto.request.TemplateModifyRequestDto;
import project.fin_the_pen.model.schedule.template.dto.request.TemplateModifySelectedScheduleRequestDto;
import project.fin_the_pen.model.schedule.template.dto.response.TemplateResponseDto;
import project.fin_the_pen.model.schedule.template.dto.response.TemplateSimpleResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final CrudScheduleRepository scheduleRepository;

    /**
     * 간단하게 3개만 보여주는 작업
     *
     * @param userId
     * @return
     */
    public Map<String, Object> viewTemplateList(String userId, HttpServletRequest request) {
        List<Template> templates = templateRepository.findByUserId(userId);

        List<Template> sortedTemplates = templates.stream()
                .sorted((t1, t2) -> t2.getId().compareTo(t1.getId())).collect(Collectors.toList())
                .stream().limit(3).collect(Collectors.toList());

        List<String> responseList = new ArrayList<>();
        Map<String, Object> responseMap = new HashMap<>();

        for (Template sortedTemplate : sortedTemplates) {
            responseList.add(sortedTemplate.getTemplateName());
        }
        if (responseList.isEmpty()) {
            responseMap.put("data", "none");
        } else {
            responseMap.put("data", responseList);
        }


        return responseMap;
    }

    /**
     * 출금과 입금을 구분하지 않고 보여줌
     *
     * @param userId
     * @return
     */
    public Map<String, List<TemplateResponseDto>> viewAllTemplateList(String userId, HttpServletRequest request) {
        List<Template> updateList = templateRepository.findByUserId(userId);

        updateAmount(updateList);

        List<Template> findAllList = templateRepository.findByUserId(userId);

        Map<String, List<TemplateResponseDto>> responseMap = new HashMap<>();

        List<TemplateResponseDto> responseList =
                findAllList.stream().map(TemplateResponseDto::new).collect(Collectors.toList());


        responseMap.put("data", responseList);

        return responseMap;
    }


    /**
     * 출금과 입금을 구분
     *
     * @param userId
     * @return
     */
    public Map<String, List<TemplateResponseDto>> viewAllDepositWithdrawList(String userId, HttpServletRequest request) {
        List<Template> updateList = templateRepository.findByUserId(userId);

        updateAmount(updateList);

        List<Template> findAllList = templateRepository.findByUserId(userId);

        Map<String, List<TemplateResponseDto>> responseMap = new HashMap<>();

        List<TemplateResponseDto> depositList =
                findAllList.stream().filter(template -> template.getStatement().equals(TemplateBankStatement.DEPOSIT))
                        .map(TemplateResponseDto::new).collect(Collectors.toList());

        List<TemplateResponseDto> withdrawList =
                findAllList.stream().filter(template -> template.getStatement().equals(TemplateBankStatement.WITHDRAW))
                        .map(TemplateResponseDto::new).collect(Collectors.toList());

        responseMap.put("deposit", depositList);
        responseMap.put("withdraw", withdrawList);

        return responseMap;
    }

    // tempalte에 있는 모든 정기일정의 값들을 합산하는 method
    // 금액 계산은 과거부터 현재까지
    public void updateAmount(List<Template> templateList) {
        templateList.forEach(template -> {
            int totalAmount = template.getScheduleList().stream()
                    .mapToInt(schedule -> Integer.parseInt(schedule.getAmount()))
                    .sum();
            template.updateAmount(totalAmount);
            templateRepository.save(template);
        });
    }

    /**
     * 템플릿 선택 삭제
     *
     * @param templateIds
     * @param request
     * @return
     */
    public boolean selectDelete(List<String> templateIds, HttpServletRequest request) {
        List<Long> convertTemplateIds = templateIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        try {
            convertTemplateIds.forEach(templateRepository::deleteById);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    /**
     * case3)
     * DB에 동일한 정기 템플릿이 있는지에 대한 유무
     * response => templateId, templateName, categoryName
     * <p>
     * 이 다음에...?
     *
     * @param request
     * @return
     */
    public Map<String, Object> selectedTemplate(String userId, String categoryName, String eventName, HttpServletRequest request) {
        List<Template> findAllList = templateRepository.findByUserId(userId);

        TemplateSimpleResponseDto responseDto = new TemplateSimpleResponseDto();
        Optional<ScheduleResponseDTO> optionalScheduleResponseDTO = Optional.empty();

        Map<String, Object> responseMap = new HashMap<>();

        boolean isFind = false;

        for (Template template : findAllList) {
            List<Schedule> scheduleList = template.getScheduleList();

            for (Schedule schedule : scheduleList) {
                if (schedule.getEventName().equals(eventName) && schedule.getCategory().equals(categoryName)) {
                    optionalScheduleResponseDTO = Optional.of(new ScheduleResponseDTO(schedule.getId(), schedule.getUserId(),
                            schedule.getEventName(), schedule.getCategory(),
                            schedule.getStartDate(), schedule.getEndDate(),
                            schedule.getStartTime(), schedule.getEndTime(), schedule.isAllDay(),
                            schedule.getRepeatOptions(), schedule.getPeriod(),
                            schedule.getPriceType(), schedule.isExclude(), schedule.getPaymentType().name(),
                            schedule.getAmount(), schedule.isFixAmount(), schedule.getRepeatKind()));
                    isFind = true;
                    break;
                }
            }

            if (isFind) {
                responseDto.setTemplateName(template.getTemplateName());
                responseDto.setUserId(template.getUserId());
                responseDto.setCategoryName(template.getCategoryName());
                responseDto.setTemplateId(String.valueOf(template.getId()));

                responseMap.put("template_data", responseDto);

                optionalScheduleResponseDTO.ifPresentOrElse(
                        scheduleData -> responseMap.put("schedule_data", scheduleData),
                        () -> responseMap.put("schedule_data", "none")
                );

                break;
            }
        }

        if (!isFind) {
            throw new NotFoundDataException("템플릿이 없습니다.");
        }

        return responseMap;
    }

    /**
     * 자산관리 -> 정기템플릿 -> 템플릿에 있는 일정들을 개별로 수정하기 위해서 정기템플릿의 내부에 있는 모든 일정들을 보여주는 메소드
     */
    public Map<String, Object> templateInScheduleInfo(String templateId, String userId) {
        Long convertTemplateId = Long.valueOf(templateId);

        Map<String, Object> responseMap = new HashMap<>();

        Optional<Template> optionalTemplate = templateRepository.findByIdAndUserId(convertTemplateId, userId);

        if (optionalTemplate.isEmpty()) {
            throw new NotFoundDataException("입력 오류입니다.");
        } else {
            Template template = optionalTemplate.get();

            TemplateResponseDto templateResponseDto = new TemplateResponseDto();
            templateResponseDto.setAmount(template.getAmount());
            templateResponseDto.setTemplateName(template.getTemplateName());
            templateResponseDto.setId(template.getId());
            templateResponseDto.setStatement(template.getStatement());
            templateResponseDto.setUserId(template.getUserId());
            templateResponseDto.setCategoryName(template.getCategoryName());

            responseMap.put("template", templateResponseDto);


            List<Schedule> scheduleList = template.getScheduleList();

            List<ScheduleResponseDTO> responseScheduleList = scheduleList.stream().map(schedule -> new ScheduleResponseDTO(schedule.getId(), schedule.getUserId(), schedule.getEventName(),
                            schedule.getCategory(), schedule.getStartDate(), schedule.getEndDate(), schedule.getStartTime(),
                            schedule.getEndTime(), schedule.isAllDay(), schedule.getRepeatOptions(),
                            schedule.getPeriod(), schedule.getPriceType(), schedule.isExclude(),
                            schedule.getPaymentType().name(), schedule.getAmount(), schedule.isFixAmount(), schedule.getRepeatKind()))
                    .collect(Collectors.toList());

            responseMap.put("schedule", responseScheduleList);
            return responseMap;
        }
    }

    /**
     * 정기 템플릿 자체를 수정하는 경우,
     * 이때는 정기템플릿에 포함되어 있는 모든 일정들이 {이름, 카테고리} 수정되어야 함.
     */
    @Transactional
    public HttpStatus templateModify(TemplateModifyRequestDto dto) {
        Long convertTemplateId = Long.valueOf(dto.getTemplateId());
        String userId = dto.getUserId();
        String modifyTemplateName = dto.getTemplateName();
        String modifyCategoryName = dto.getCategoryName();

        Optional<Template> optionalTemplate = templateRepository.findByIdAndUserId(convertTemplateId, userId);

        if (optionalTemplate.isEmpty()) {
            throw new NotFoundDataException("입력 오류입니다.");
        } else {
            try {
                Template template = optionalTemplate.get();
                List<Schedule> scheduleList = template.getScheduleList();
                template.updateTemplateNameAndCategory(modifyTemplateName, modifyCategoryName);

                scheduleList.forEach(schedule -> {
                    schedule.updateEventNameAndCategoryName(modifyTemplateName, modifyCategoryName);
                    scheduleRepository.save(schedule);
                });

                templateRepository.save(template);

            } catch (RuntimeException e) {
                throw new ExecuteException(e.getMessage());
            }
            return HttpStatus.OK;
        }
    }

    /**
     * 템플릿 리스트 내부에서 일정들을 선택해서 수정하는 경우 {자산쪽만 수정이 가능하다.}
     */
    public HttpStatus templateModifySelectedSchedule(TemplateModifySelectedScheduleRequestDto dto) {
        Long convertTemplateId = Long.valueOf(dto.getTemplateId());
        String userId = dto.getUserId();
        String scheduleIdList = dto.getScheduleIdList();

        StringTokenizer tokenizer = new StringTokenizer(scheduleIdList, ",");

        List<Long> convertScheduleIdList = new ArrayList<>();

        while (tokenizer.hasMoreTokens()) {
            convertScheduleIdList.add(Long.valueOf(tokenizer.nextToken()));
        }

        String modifyAmount = dto.getAmount();
        String modifyIsFixed = dto.getIsFixed();
        String modifyIsExcluded = dto.getIsExcluded();
        String modifyPaymentType = dto.getPaymentType();

        Optional<Template> optionalTemplate = templateRepository.findByIdAndUserId(convertTemplateId, userId);

        if (optionalTemplate.isEmpty()) {
            throw new NotFoundDataException("not found data");
        } else {
            try {
                Template template = optionalTemplate.get();

                List<Schedule> scheduleList = template.getScheduleList();

                Set<Long> scheduleIdSet = new HashSet<>(convertScheduleIdList);

                // scheduleList를 순회하면서 scheduleId가 매치되는 스케줄을 업데이트
                scheduleList.forEach(schedule -> {
                    if (scheduleIdSet.contains(schedule.getId())) {
                        // 스케줄 업데이트 로직 (예: name과 description 변경)
                        schedule.updateExcluded(modifyIsExcluded);
                        schedule.updateFixed(modifyIsFixed);
                        schedule.updatePaymentType(modifyPaymentType);
                        schedule.updateAmount(modifyAmount);

                        scheduleRepository.save(schedule);
                    }
                });
            } catch (RuntimeException e) {
                throw new ExecuteException(e.getMessage());
            }

            return HttpStatus.OK;
        }
    }


    /**
     * case4)
     * 정기 템플릿을 선택할 경우 템플릿에 있는 일정을 가져와서 화면에 바인딩
     * 된듯..
     *
     * @param templateId
     * @param templateName
     * @param request
     * @return
     */
    public ScheduleResponseDTO responseTemplate(String templateId, String templateName, HttpServletRequest request) {
        Optional<Template> optionalTemplate =
                templateRepository.findByIdAndTemplateName(Long.valueOf(templateId), templateName);

        if (optionalTemplate.isEmpty()) {
            throw new NotFoundDataException("템플릿이 존재하지 않습니다.");
        } else {
            Template template = optionalTemplate.get();

            List<Schedule> scheduleList = template.getScheduleList();
            Optional<Schedule> first = scheduleList.stream().findFirst();

            if (first.isEmpty()) {
                throw new NotFoundDataException("템플릿에 등록된 일정이 없습니다.");
            } else {
                Schedule schedule = first.get();
                return createScheduleResponseDTO(schedule);
            }
        }
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
}
