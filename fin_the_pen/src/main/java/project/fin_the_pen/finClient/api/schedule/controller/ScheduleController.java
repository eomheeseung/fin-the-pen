package project.fin_the_pen.finClient.api.schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.finClient.core.error.customException.DuplicatedScheduleException;
import project.fin_the_pen.finClient.core.util.ConvertResponse;
import project.fin_the_pen.model.schedule.dto.DeleteScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.dto.ScheduleRequestDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.service.ScheduleService;
import project.fin_the_pen.model.schedule.vo.FindAllScheduleVO;
import project.fin_the_pen.model.schedule.vo.FindCertainMonthVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "API 테스트 / schedule", description = "사용자 로그인 후 이용가능!")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ConvertResponse convertResponse;

    /**
     * header에 authorization에 "Bearer ~"로 들어온 것을 파싱하고 db와 비교해서 로직 수행
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/createSchedule", produces = "application/json")
    @Operation(description = "일정을 등록하는 API입니다.  " + "(일정이름, 카테고리, 시작일자 및 시간, 종료일자 및 시간)이 동일하다면 중복된 일정으로 판단 <br>" +
            "매일의 경우 (value,kind_type)만 넣어주면 됩니다.<br>" +
            "특정 주간의 경우 (value, kind_type=week, day_of_XXX=MONDAY, SUNDAY...)으로 넣어주면 됩니다.<br>",
            summary = "일정등록 (O)")
    public ResponseEntity<Object> registerSchedule(@RequestBody ScheduleRequestDTO dto, HttpServletRequest request) {
        try {
            Map<Object, Object> responseMap = scheduleService.registerSchedule(dto, request);

            if (responseMap.get("data").equals(dto.getUserId())) {
                log.info("일정 - " + dto.getUserId() + " 의 일정 이름: " + dto.getEventName());
                return ResponseEntity.ok().body(responseMap);
            } else throw new RuntimeException();
        } catch (DuplicatedScheduleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            // 에러 핸들링 로직 추가
            log.error("일정 등록 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    /**
     * 유저 한명의 모든 일정 조회
     *
     * @param findAllScheduleVO
     * @param request
     * @return
     */
    @PostMapping(value = "/getAllSchedules", produces = "application/json")
    @Operation(description = "user의 login된 id로 모든 일정들을 조회합니다.", summary = "모든 일정 조회 (O)")
    public ResponseEntity<Object> findAllSchedule(@RequestBody FindAllScheduleVO findAllScheduleVO, HttpServletRequest request) {
        try {
            Map<String, Object> responseMap = scheduleService.findAllSchedule(findAllScheduleVO.getUserId(), request);
            log.info(responseMap.get("data").toString());
            return convertResponse.getResponseEntity(responseMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("오류 입니다.");
        }
    }

    @PostMapping("/getMonthSchedules")
    @Operation(description = "user의 login된 id와 date로 해당하는 date의 월별 모든 일정들을 조회합니다.", summary = "월별 조회 (O)")
    public ResponseEntity<Object> findMonthSchedule(@RequestBody FindCertainMonthVO findCertainMonthVO, HttpServletRequest request) {
        if (findCertainMonthVO.getDate() == null) {
            return ResponseEntity.ok().body("현재 등록된 일정은 없습니다.");
        }

        Map<String, Object> responseMap = scheduleService.findMonthSchedule(findCertainMonthVO.getDate(), findCertainMonthVO.getUserId(), request);

        return convertResponse.getResponseEntity(responseMap);
    }

    @PostMapping("/getMonthSchedules/section")
    public ResponseEntity<Object> findMonthSectionSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("date"));
        Map<String, Object> responseMap = scheduleService.findMonthSectionSchedule(map.get("startDate"), map.get("endDate"), map.get("user_id"));

        return convertResponse.getResponseEntity(responseMap);
    }

    @PostMapping("/findCategory")
    @Operation(description = "카테고리로 모든 일정을 조회합니다.", summary = "카테고리 조회")
    public ResponseEntity<Object> findScheduleCategory(@RequestBody CategoryRequestDTO categoryRequestDTO, HttpSession session) {
        Map<String, Object> responseMap = scheduleService.findScheduleCategory(categoryRequestDTO, session.getAttribute("session").toString());

        return convertResponse.getResponseEntity(responseMap);
    }

    @PostMapping("/find/contains/name")
    @Operation(description = "일정의 이름에 해당하는 단어(키워드)를 넣으면 match된 모든 일정을 조회합니다.", summary = "일정을 이름으로 검색")
    public ResponseEntity<Object> findByContainsName(@RequestBody ConcurrentHashMap<String, String> map) {
        Map<String, Object> responseMap = scheduleService.findByContainsName(map.get("name"));

        return convertResponse.getResponseEntity(responseMap);
    }


    /**
     * 일정 수정
     *
     * @return
     */
    @PostMapping("/modifySchedule")
    @Operation(description = "일정을 수정 <br>"+
            "modify_options에 들어가는 목록<br>"+
            " - nowFromAfter : 선택된 현재 일정부터 이후까지<br>"+
            " - exceptNowAfter : 현재 일정 제외하고 이후<br>" +
            " - all : 모든 일정", summary = "일정 수정")
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

    /**
     * 일정 삭제
     */
    @DeleteMapping("/deleteSchedule")
    @Operation(description = "일정을 삭제합니다.", summary = "일정 삭제")
    public ResponseEntity<Object> deleteSchedule(@RequestBody DeleteScheduleDTO dto, HttpServletRequest request) {
        try {
            scheduleService.deleteSchedule(dto, request);

            return ResponseEntity.ok().build();
        } catch (DuplicatedScheduleException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            // 에러 핸들링 로직 추가
            log.error("일정 삭제 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
