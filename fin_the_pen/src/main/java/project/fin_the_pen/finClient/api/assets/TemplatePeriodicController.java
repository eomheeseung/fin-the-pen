package project.fin_the_pen.finClient.api.assets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.model.schedule.template.TemplateResponseDto;
import project.fin_the_pen.model.schedule.template.TemplateService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 정기 입출금액 템플릿 관리
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
        Map<String, List<TemplateResponseDto>> responseMap = templateService.viewAllTemplateList(userId);
        return ResponseEntity.ok().body(responseMap);
    }
}
