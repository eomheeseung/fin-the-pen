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
import project.fin_the_pen.model.report.dto.ConsumeReportRequestDTO;
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

    /*@PostMapping
    @Operation(description = "리포트 메인 화면입니다.", summary = "리포트 메인 화면")
    public ResponseEntity<Object> reportHome(@RequestBody ReportRequestDemoDTO dto, HttpServletRequest request) {
        try {
            log.info(dto.getDate());
            HashMap<Object, Object> map = reportService.reportHome(dto, request);

            return ResponseEntity.ok().body(map);
        } catch (NotFoundDataException e) {
            return ResponseEntity.internalServerError().build();
        }
    }*/

    @GetMapping
    @Operation(description = "리포트 메인 화면입니다.", summary = "리포트 메인 화면")
    public ResponseEntity<Object> reportHome(@RequestParam("date") String date, @RequestParam("userId") String userId, HttpServletRequest request) {
        try {
//            log.info(dto.getDate());
            ReportRequestDemoDTO dto = new ReportRequestDemoDTO();
            dto.setDate(date);
            dto.setUserId(userId);
            HashMap<Object, Object> map = reportService.reportHome(dto, request);

            return ResponseEntity.ok().body(map);
        } catch (NotFoundDataException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /*@PostMapping
    @Operation(description = "지출 목표액을 설정합니다.", summary = "지출 목표액 설정")
    public ResponseEntity<Object> setAmount(@RequestBody ExpenditureRequestDTO dto, HttpServletRequest request) {
        return ResponseEntity.ok().body(reportService.setAmount(dto, request));
    }*/


    /*
    TODO 2024-02-01같이 day까지 받아야 하는지...
     */
    @PostMapping("/basic")
    @Operation(description = "입력된 월의 소비 리포트를 조회합니다.", summary = "소비 리포트 조회")
    public ResponseEntity<Object> consumeReport(@RequestBody ConsumeReportRequestDTO dto, HttpServletRequest request) {
        return convertResponse.getResponseEntity(reportService.inquiryReport(dto, request));
    }

    @PostMapping("/detail")
    @Operation(description = "입력된 월의 소비 리포트를 조회합니다.", summary = "소비 리포트 조회")
    public ResponseEntity<Object> consumeDetailReport(@RequestBody ConsumeReportDetailRequestDto dto, HttpServletRequest request) {
        Map<Object, Object> objectObjectMap = reportService.inquiryCategoryDetail(dto, request);
        return ResponseEntity.ok().body(objectObjectMap);
    }
}
