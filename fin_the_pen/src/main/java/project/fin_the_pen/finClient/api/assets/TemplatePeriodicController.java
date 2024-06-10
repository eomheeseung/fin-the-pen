package project.fin_the_pen.finClient.api.assets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.model.schedule.template.dto.response.TemplateResponseDto;
import project.fin_the_pen.model.schedule.template.TemplateService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 정기 입출금액 템플릿 관리
 * 자산관리의 정기템플릿 리스트를 보여주는 것과 일정등록에서 템플릿 리스트와는 다름
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/asset")
@Tag(name = "정기 입출금액 설정 ", description = "정기 입출금액 설정 및 화면")
public class TemplatePeriodicController {
    private final TemplateService templateService;

    @GetMapping("/template/view")
    @Operation(summary = "정기 입출금액 템플릿 화면 보여주기", description = "정기 입출금액의 템플릿들을 입금과 출금별로 구분해서 보여줍니다.")
    public ResponseEntity<Object> templateView(@RequestParam("user_id") String userId, HttpServletRequest request) {
        Map<String, List<TemplateResponseDto>> responseMap =
                templateService.viewAllDepositWithdrawList(userId, request);
        return ResponseEntity.ok().body(responseMap);
    }

    // 관리 -> 편집 (수정)


    // 관리 -> 삭제
    @DeleteMapping("/template/delete")
    @Operation(description = "정기템플릿을 선택해서 삭제합니다.", summary = "정기템플릿 선택삭제")
    public ResponseEntity<Object> templateDelete(@RequestParam("template_id") String templateId, HttpServletRequest request) {
        boolean flag = templateService.selectDelete(templateId, request);

        if (flag) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }
}
