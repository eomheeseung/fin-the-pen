package project.fin_the_pen.finClient.api.report.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.finClient.core.error.customException.NotFoundDataException;
import project.fin_the_pen.finClient.core.util.ConvertResponse;
import project.fin_the_pen.model.report.dto.ConsumeReportRequestDTO;
import project.fin_the_pen.model.report.dto.ExpenditureRequestDTO;
import project.fin_the_pen.model.report.dto.ReportRequestDemoDTO;
import project.fin_the_pen.model.report.service.ReportService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
@Tag(name = "API 테스트 / report", description = "사용자 로그인 후 이용가능!")
public class ReportController {
    private final ReportService reportService;
    private final ConvertResponse convertResponse;

    @PostMapping("/report/home")
    @Operation(description = "리포트 메인 화면입니다.", summary = "리포트 메인 화면")
    public ResponseEntity<Object> reportHome(@RequestBody ReportRequestDemoDTO dto, HttpServletRequest request) {
        try {
            log.info(dto.getDate());
            HashMap<Object, Object> map = reportService.reportHome(dto, request);

            return ResponseEntity.ok().body(map);
        } catch (NotFoundDataException e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    /* TODO
        지출 목표액이 중복되어 저장됨, 1번 저장되고 다시 저장하려고 하면 delete -> create / update query 날려야 함.
        userId당 하나의 report amount가 있어야 함.
     */
    @PostMapping("/report/set-amount")
    @Operation(description = "지출 목표액을 설정합니다.", summary = "지출 목표액 설정")
    public ResponseEntity<Object> setAmount(@RequestBody ExpenditureRequestDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok().body(reportService.setAmount(dto, request));
    }


    @PostMapping("/report/inquiry")
    @Operation(description = "입력된 월의 소비 리포트를 조회합니다.", summary = "소비 리포트 조회")
    public ResponseEntity<Object> consumeReport(@RequestBody ConsumeReportRequestDTO dto, HttpServletRequest request) {
        Map<String, Object> responseMap =
                reportService.inquiryReport(dto, request);

        return convertResponse.getResponseEntity(responseMap);
    }
}
