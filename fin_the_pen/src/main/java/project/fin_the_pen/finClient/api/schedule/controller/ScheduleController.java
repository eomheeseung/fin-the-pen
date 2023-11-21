package project.fin_the_pen.finClient.api.schedule.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.finClient.core.util.ConvertResponse;
import project.fin_the_pen.model.schedule.dto.ScheduleDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.service.ScheduleService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final ConvertResponse convertResponse;

    @PostMapping("/createSchedule")
    public boolean registerSchedule(@RequestBody ScheduleDTO dto, HttpSession session) {
        dto.setUserId(session.getAttribute("session").toString());
        log.info(session.getAttribute("session").toString());

        try {
            scheduleService.registerSchedule(dto);
        } catch (Exception e) {
            return false;
        }

        log.info("일정 - " + dto.getUserId() + "의 일정 이름: " + dto.getEventName());
        return true;
    }

    // 유저 한명의 모든 일정 조회
    @PostMapping("/getAllSchedules")
    public ResponseEntity<Object> findSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info("찾는 id {}", map.get("user_id"));
        Map<String, Object> responseMap = scheduleService.findAllSchedule(map.get("user_id"));
        return convertResponse.getResponseEntity(responseMap);
    }

    @PostMapping("/getMonthSchedules")
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
    public ResponseEntity<Object> findScheduleCategory(@RequestBody CategoryRequestDTO categoryRequestDTO, HttpSession session) {
        Map<String, Object> responseMap = scheduleService
                .findScheduleCategory(categoryRequestDTO, session.getAttribute("session").toString());

        return convertResponse.getResponseEntity(responseMap);
    }

    @PostMapping("/find/contains/name")
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
    @PostMapping("/modifySchedule")
    public ResponseEntity<Object> modifySchedule(@RequestBody ScheduleDTO dto) {
        log.info(String.valueOf(dto.getId()));
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
     * TODO 로그인하고나서 여기를 수정해야 함
     *  string으로 반환하면 되는데 jsonArray는 안됨
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
