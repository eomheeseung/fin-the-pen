package project.fin_the_pen.model.schedule.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemplateService {
    private final TemplateRepository templateRepository;

    public Map<String, Object> viewTemplateList(String userId) {
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

    public Map<String, List<TemplateResponseDto>> viewAllTemplateList(String userId) {
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
    // 시간에 따라서 금액이 빠지는 경우도 생각해야 함.
    public void updateAmount(List<Template> templateList) {
        templateList.forEach(template -> {
            int totalAmount = template.getScheduleList().stream()
                    .mapToInt(schedule -> Integer.parseInt(schedule.getAmount()))
                    .sum();
            template.updateAmount(totalAmount);
            templateRepository.save(template);
        });
    }
}
