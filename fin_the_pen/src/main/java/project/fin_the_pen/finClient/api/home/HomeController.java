package project.fin_the_pen.finClient.api.home;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.finClient.core.util.ConvertResponse;
import project.fin_the_pen.model.home.dto.HomeMonthRequestDto;
import project.fin_the_pen.model.home.service.HomeService;
import project.fin_the_pen.model.schedule.service.ScheduleService;
import project.fin_the_pen.model.schedule.vo.FindCertainMonthVO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "v2 홈 화면", description = "홈 화면 (로그인 후 이용가능!)")
public class HomeController {
    private final HomeService service;
    private final ScheduleService scheduleService;
    private final ConvertResponse convertResponse;

    @Operation(summary = "홈 화면", description = "홈 화면")
    @PostMapping("/home/month")
    public ResponseEntity<Object> homeMonth(@RequestBody HomeMonthRequestDto dto, HttpServletRequest request) {

        try {
            HashMap<Object, Object> responseMap = service.inquiryMonth(dto, request);
            return ResponseEntity.ok().body(responseMap);

        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/home/getMonthSchedules")
    @Operation(description = "user의 login된 id와 date로 해당하는 date의 월별 모든 일정들을 조회합니다.", summary = "월별 조회 (O)")
    public ResponseEntity<Object> findMonthSchedule(@RequestBody FindCertainMonthVO findCertainMonthVO, HttpServletRequest request) {
        if (findCertainMonthVO.getDate() == null) {
            return ResponseEntity.ok().body("현재 등록된 일정은 없습니다.");
        }

        Map<String, Object> responseMap =
                scheduleService.findMonthSchedule(findCertainMonthVO.getDate(), findCertainMonthVO.getUserId(), request);

        return convertResponse.getResponseEntity(responseMap);
    }
}
