package project.fin_the_pen.finClient.api.schedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.finClient.core.util.ConvertResponse;
import project.fin_the_pen.model.schedule.dto.ScheduleDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.service.ScheduleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
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
    @Operation(description = "일정을 등록하는 API입니다.", summary = "일정등록")
    public ResponseEntity<Object> registerSchedule(@RequestBody ScheduleDTO dto, HttpServletRequest request) {
        String extractToken = request.getHeader("Authorization");

        if (extractToken == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Boolean flag = scheduleService.registerSchedule(dto, extractToken);
            if (flag) {
                log.info("일정 - " + dto.getUserId() + " 의 일정 이름: " + dto.getEventName());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            // 에러 핸들링 로직 추가
            log.error("일정 등록 중 에러 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일정 등록 중 에러 발생");
        }
    }


    // 유저 한명의 모든 일정 조회
    @GetMapping(value = "/getAllSchedules", produces = "application/json")
    @Operation(description = "user의 login된 id로 모든 일정들을 조회합니다.", summary = "모든 일정 조회")
    public ResponseEntity<Object> findAllSchedule(HttpServletRequest request) {

        String extractToken = request.getHeader("Authorization");

        if (extractToken == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Map<String, Object> responseMap = scheduleService.findAllSchedule(extractToken);
            log.info(responseMap.get("data").toString());
            return convertResponse.getResponseEntity(responseMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/getMonthSchedules")
    @Operation(description = "user의 login된 id와 date로 해당하는 date의 월별 모든 일정들을 조회합니다.", summary = "월별 조회")
    public ResponseEntity<Object> findMonthSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        Map<String, Object> responseMap = scheduleService.findMonthSchedule(map.get("date"), map.get("user_id"));
        return convertResponse.getResponseEntity(responseMap);
    }

    @PostMapping("/getMonthSchedules/section")
    public ResponseEntity<Object> findMonthSectionSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("date"));
        Map<String, Object> responseMap = scheduleService.findMonthSectionSchedule(map.get("startDate"),
                map.get("endDate"),
                map.get("user_id"));

        return convertResponse.getResponseEntity(responseMap);
    }

    @PostMapping("/findCategory")
    @Operation(description = "카테고리로 모든 일정을 조회합니다.", summary = "카테고리 조회")
    public ResponseEntity<Object> findScheduleCategory(@RequestBody CategoryRequestDTO
                                                               categoryRequestDTO, HttpSession session) {
        Map<String, Object> responseMap = scheduleService
                .findScheduleCategory(categoryRequestDTO, session.getAttribute("session").toString());

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
     * @param dto
     * @return
     */
    @PutMapping("/modifySchedule")
    @Operation(description = "일정을 수정합니다.", summary = "일정 수정")
    public ResponseEntity<Object> modifySchedule(@RequestBody ScheduleDTO dto) {
        log.info(String.valueOf(dto.getUserId()));
        ResponseEntity<Object> responseEntity = null;

        boolean flag = scheduleService.modifySchedule(dto);
        Map<String, Boolean> responseMap = new HashMap<>();

        if (flag) {
            responseMap.put("data", true);
            responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            responseMap.put("data", false);
            responseEntity = new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    /*@PostMapping("/deleteSchedule")
    public boolean deleteSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("id"));
        if (scheduleService.deleteSchedule(map.get("id"))) {
            return true;
        } else {
            return false;
        }
    }*/

    // 여기를 수정해야 함
    /*
     *
     *
     * */


    /**
     * uuid 하나로만 일정 조회
     *
     * @param
     * @return
     */
//    @PostMapping("/findOne")
//    @ResponseBody
//    public ScheduleResponseDTO findOne(@RequestBody ConcurrentHashMap<String, String> map) {
//        log.info(String.valueOf(map.get("id")));
//
//        ScheduleResponseDTO find = scheduleService.findOne(map.get("id"));
//        log.info(find.getId());
//
//        return find;
//    }

}
