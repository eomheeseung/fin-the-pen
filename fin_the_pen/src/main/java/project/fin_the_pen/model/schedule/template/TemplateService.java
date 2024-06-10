package project.fin_the_pen.model.schedule.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;
import project.fin_the_pen.model.schedule.dto.ScheduleResponseDTO;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.template.dto.request.ImportTemplateRequestDto;
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
     * @param templateId
     * @param request
     * @return
     */
    public boolean selectDelete(String templateId, HttpServletRequest request) {
        Long key = Long.valueOf(templateId);

        try {
            templateRepository.deleteById(key);
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }

    /**
     * case3)
     * DB에 동일한 정기 템플릿이 있는지에 대한 유무
     * response => templateId, templateName, categoryName
     *
     * @param dto
     * @param request
     * @return
     */
    public TemplateSimpleResponseDto selectedTemplate(ImportTemplateRequestDto dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String categoryName = dto.getCategoryName();
        String eventName = dto.getEventName();

        List<Template> findAllList = templateRepository.findByUserId(userId);

        TemplateSimpleResponseDto responseDto = new TemplateSimpleResponseDto();

        boolean isFind = false;

        for (Template template : findAllList) {
            List<Schedule> scheduleList = template.getScheduleList();

            for (Schedule schedule : scheduleList) {
                if (schedule.getEventName().equals(eventName) && schedule.getCategory().equals(categoryName)) {
                    isFind = true;
                    break;
                }
            }

            if (isFind) {
                responseDto.setTemplateName(template.getTemplateName());
                responseDto.setUserId(template.getUserId());
                responseDto.setCategoryName(template.getCategoryName());
                responseDto.setTemplateId(String.valueOf(template.getId()));
                break;
            }
        }

        if (!isFind) {
            throw new NotFoundDataException("템플릿이 없습니다.");
        }

        return responseDto;

    }

    /**
     * template에 추가하는 경우
     */
    /*public TemplateSimpleResponseDto importAndSaveTemplate(String templateId, HttpServletRequest request) {

    }*/


    /**
     * case4)
     *  정기 템플릿을 선택할 경우 템플릿에 있는 일정을 가져와서 화면에 바인딩
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
