package project.fin_the_pen.finClient.api.report.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;
import project.fin_the_pen.finClient.core.util.ConvertResponse;
import project.fin_the_pen.model.report.dto.ConsumeReportDetailRequestDto;
import project.fin_the_pen.model.report.dto.ReportRequestDemoDTO;
import project.fin_the_pen.model.report.service.ReportService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/report/month")
@Slf4j
@Tag(name = "API 테스트 / report", description = "사용자 로그인 후 이용가능!")
public class ReportMonthController {
    private final ReportService reportService;
    private final ConvertResponse convertResponse;

    @GetMapping
    @Operation(description = "리포트 메인 화면입니다.", summary = "리포트 메인 화면")
    public ResponseEntity<Object> reportHome(@RequestParam("date") String date,
                                             @RequestParam("user_id") String userId,
                                             HttpServletRequest request) {
        try {
            ReportRequestDemoDTO dto = new ReportRequestDemoDTO();
            dto.setDate(date);
            dto.setUserId(userId);
            HashMap<Object, Object> map = reportService.reportHome(dto, request);

            return ResponseEntity.ok().body(map);
        } catch (NotFoundDataException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 소비 리포트 상세
     * TODO 카테고리로 조회하므로 카테고리 이름으로 구별이 되어져야 함.
     *  현재는 시간값으로만 계산이 됨
     *  예를들어, 소비지출목표액에서 카테고리이름을 "여가"로 지정했는데
     *  "외식"도 계산값에서 빠지는 문제가 발생함.
     */
    @PostMapping("/details")
    @Operation(description =
            "아래의 api에서 /asset/category-amount/set 에서 date에 지출목표액을 설정할 달을 입력하고, <br>" +
                    "medium_name에 상위 분류를 넣고, medium_value로 지출 목표액을 설정하고, <br> " +
                    "small_map에 하위분류들을 적어주시면 됩니다.", summary = "카테고리로 월별 리포트 소비 목록 조회")
    public ResponseEntity<Object> reportDetails(@RequestBody ConsumeReportDetailRequestDto dto, HttpServletRequest request) {
        Map<Object, Object> responseMap = reportService.inquiryCategoryDetail(dto, request);

        return ResponseEntity.ok().body(responseMap);
    }
}
