package project.fin_the_pen.finClient.schedule.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.finClient.schedule.dto.ScheduleDTO;
import project.fin_the_pen.finClient.schedule.dto.category.CategoryRequestDTO;
import project.fin_the_pen.finClient.schedule.service.ScheduleService;

import javax.servlet.http.HttpSession;
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
    // TODO 1!!!!
    @PostMapping("/getAllSchedules")
    public String findSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info("찾는 id {}", map.get("user_id"));
        JSONArray array = scheduleService.findAllSchedule(map.get("user_id"));
        log.info(array.toString());

        return array.toString();
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

    @PostMapping("/getMonthSchedules")
    public String findMonthSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("date"));

        return scheduleService.findMonthSchedule(map.get("date"), map.get("user_id")).toString();
    }

    @PostMapping("/getMonthSchedules/section")
    public String findMonthSectionSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("date"));

        return scheduleService.findMonthSectionSchedule(map.get("startDate"),
                map.get("endDate"),
                map.get("user_id")).toString();
//        return scheduleService.findMonthSchedule(map.get("date"), map.get("user_id")).toString();
    }

    /**
     * uuid 하나로만 일정 조회
     *
     * @param map
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
    public String findScheduleCategory(@RequestBody CategoryRequestDTO categoryRequestDTO, HttpSession session) {
        JSONArray jsonArray = scheduleService
                .findScheduleCategory(categoryRequestDTO, session.getAttribute("session").toString());
        return jsonArray.toString();
    }

    @PostMapping("/find/contains/name")
    public String findByContainsName(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(scheduleService.findByContainsName(map.get("name")).toString());
        return scheduleService.findByContainsName(map.get("name")).toString();
    }
}
