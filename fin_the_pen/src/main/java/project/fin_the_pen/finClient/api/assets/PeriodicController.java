package project.fin_the_pen.finClient.api.assets;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.model.assets.periodic.dto.PeriodicDeleteRequestDto;
import project.fin_the_pen.model.assets.periodic.dto.PeriodicViewRequestDto;
import project.fin_the_pen.model.assets.service.PeriodicService;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.service.ScheduleService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/asset")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "정기 입출금액 설정 ", description = "정기 입출금액 설정 및 화면")
public class PeriodicController {
    private final ScheduleService scheduleService;
    private final PeriodicService periodicService;
    /**
     * 정기 입출 금액
     *
     * @param request
     * @return
     */
    @Operation(summary = "정기 입출금액 설정 view,", description = "정기 입출금액이 설정된 것을 보여주는 화면")
    @PostMapping("/period-amount/view")
    public ResponseEntity<Object> viewPeriodicAmount(@RequestBody PeriodicViewRequestDto dto, HttpServletRequest request) {
        Map<Object, Object> responseMap = periodicService.viewPeriodAmount(dto, request);

        return ResponseEntity.ok().body(responseMap);
    }

    @Operation(summary = "정기 입출금액 설정 page,", description = "정기 입출금액 설정 및 수정")
    @PostMapping("/period-amount/set")
    public ResponseEntity<Object> setPeriodicAmount(@RequestBody ScheduleRequestDTO dto, HttpServletRequest request) {
        Map<Object, Object> responseMap = scheduleService.registerSchedule(dto, request);

        return ResponseEntity.ok().body(responseMap);
    }

    /**
     * 수정
     *
     * @return
     */
    @PostMapping("/period-amount/modify")
    @Operation(description = "일정을 수정 <br>"+
            "modify_options에 들어가는 목록<br>"+
            " - nowFromAfter : 선택된 현재 일정부터 이후까지<br>"+
            " - exceptNowAfter : 현재 일정 제외하고 이후<br>" +
            " - all : 모든 일정", summary = "정기 입출금액 수정")
    public ResponseEntity<Object> modifySchedule(@RequestBody ModifyScheduleDTO modifyScheduleDTO, HttpServletRequest request) {
        try {
            Boolean flag = scheduleService.modifySchedule(modifyScheduleDTO, request);

            return ResponseEntity.ok().body(flag);
        } catch (DuplicatedScheduleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            // 에러 핸들링 로직 추가
            log.error("일정 수정 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Operation(summary = "정기 입출금액 삭제", description = "정기 입출 금액 삭제")
    @DeleteMapping("/periond-amount/delete")
    public ResponseEntity<Object> deletePeriodAmount(@RequestBody PeriodicDeleteRequestDto dto, HttpServletRequest request) {
        boolean flag = periodicService.deletePeriodAmount(dto, request);

        return ResponseEntity.ok().body(flag);
    }
}
