package project.fin_the_pen.finClient.api.report.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.finClient.core.util.ConvertResponse;
import project.fin_the_pen.model.report.dto.ConsumeReportRequestDTO;
import project.fin_the_pen.model.report.dto.ExpenditureRequestDTO;
import project.fin_the_pen.model.report.service.ReportService;
import project.fin_the_pen.model.schedule.service.ScheduleService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController(value = "/report")
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    private final ReportService reportService;
    private final ConvertResponse convertResponse;

    @PostMapping("/set-amount")
    @Operation(description = "지출 목표액을 설정합니다.", summary = "지출 목표액 설정")
    public ResponseEntity<Object> setAmount(@RequestBody ExpenditureRequestDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok().body(reportService.setAmount(dto, request));
    }


    @PostMapping("/inquiry")
    @Operation(description = "입력된 월의 소비 리포트를 조회합니다.", summary = "리포트 조회")
    public ResponseEntity<Object> consumeReport(@RequestBody ConsumeReportRequestDTO dto, HttpServletRequest request) {
        Map<String, Object> responseMap =
                reportService.inquiryReport(dto, request);

        return convertResponse.getResponseEntity(responseMap);
    }
}
