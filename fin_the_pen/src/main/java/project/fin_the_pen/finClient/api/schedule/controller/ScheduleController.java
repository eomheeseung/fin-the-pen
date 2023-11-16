package project.fin_the_pen.finClient.api.schedule.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.model.schedule.dto.ScheduleDTO;
import project.fin_the_pen.model.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.model.schedule.service.ScheduleService;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

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
        ResponseEntity<Object> responseEntity = null;

        if (responseMap.get("data").equals("error")) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return responseEntity;
        }

        responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);

        return responseEntity;
    }

    //일정 편집
//    @PostMapping("/modifySchedule")
//    @ResponseBody
//    public Boolean modifySchedule(@RequestBody ScheduleDTO scheduleRequestDTO) {
//        log.info(String.valueOf(scheduleRequestDTO.getId()));
//        if (!scheduleService.modifySchedule(scheduleRequestDTO)) {
//            return false;
//        }
//        return true;
//    }

    @PostMapping("/deleteSchedule")
    public boolean deleteSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("id"));
        if (scheduleService.deleteSchedule(map.get("id"))) {
            return true;
        } else {
            return false;
        }
    }

    // 여기를 수정해야 함 
    /*
     * TODO 로그인하고나서 여기를 수정해야 함
     *  string으로 반환하면 되는데 jsonArray는 안됨
     *
     *
     * */
    @PostMapping("/getMonthSchedules")
    public ResponseEntity<Object> findMonthSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        ResponseEntity<Object> responseEntity = null;
        Map<String, Object> responseMap = scheduleService.findMonthSchedule(map.get("data"), map.get("user_id"));

        if (responseMap.get("data").equals("error")) {
            responseEntity = new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            return responseEntity;
        }
        responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/getMonthSchedules/section")
    public ResponseEntity<Object> findMonthSectionSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("date"));
        ResponseEntity<Object> responseEntity;

        Map<String, Object> responseMap = scheduleService.findMonthSectionSchedule(map.get("startDate"),
                map.get("endDate"),
                map.get("user_id"));

        if (responseMap.get("data").equals("error")) {
            responseEntity = new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            return responseEntity;
        }
        responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        return responseEntity;
    }

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
    @PostMapping("/findCategory")
    public ResponseEntity<Object> findScheduleCategory(@RequestBody CategoryRequestDTO categoryRequestDTO, HttpSession session) {
        Map<String, Object> responseMap = scheduleService
                .findScheduleCategory(categoryRequestDTO, session.getAttribute("session").toString());

        ResponseEntity<Object> responseEntity = null;

        if (responseMap.get("data").equals("error")) {
            responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return responseEntity;
        }
        responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/find/contains/name")
    public ResponseEntity<Object> findByContainsName(@RequestBody ConcurrentHashMap<String, String> map) {
        ResponseEntity<Object> responseEntity;
        Map<String, Object> responseMap = scheduleService.findByContainsName(map.get("name"));

        if (responseMap.get("data").equals("error")) {
            responseEntity = new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            return responseEntity;
        }

        responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        log.info(responseMap.toString());
        return responseEntity;
    }
}
