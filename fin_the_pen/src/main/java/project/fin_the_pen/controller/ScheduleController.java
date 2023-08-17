package project.fin_the_pen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.data.schedule.ScheduleResponseDTO;
import project.fin_the_pen.data.schedule.category.CategoryRequestDTO;
import project.fin_the_pen.service.ScheduleService;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/createSchedule")
    public boolean registerSchedule(@RequestBody ScheduleRequestDTO scheduleRequestDTO, HttpSession session) {
        scheduleRequestDTO.setUserId(session.getAttribute("session").toString());
        log.info(session.getAttribute("session").toString());
        try {
            scheduleService.registerSchedule(scheduleRequestDTO);
        } catch (Exception e) {
            return false;
        }

        log.info("일정 - " + scheduleRequestDTO.getUserId() + "의 일정 이름: " + scheduleRequestDTO.getEventName());
        return true;
    }

    @PostMapping("/getAllSchedules")
//    @ResponseBody
    public String findSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("user_id"));
        log.info("json return");
        JSONArray array = scheduleService.findAllSchedule(map.get("user_id"));
        log.info(array.toString());

        return array.toString();
    }

    //일정 편집
    @PostMapping("/modifySchedule")
//    @ResponseBody
    public Boolean modifySchedule(@RequestBody ScheduleRequestDTO scheduleRequestDTO) {
        log.info(String.valueOf(scheduleRequestDTO.getId()));

        if (!scheduleService.modifySchedule(scheduleRequestDTO)) {
            return false;
        }
        return true;
    }

    @PostMapping("/deleteSchedule")
//    @ResponseBody
    public boolean deleteSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("id"));
        if (scheduleService.deleteSchedule(map.get("id"))) {
            return true;
        } else {
            return false;
        }
    }

    @PostMapping("/getMonthSchedules")
//    @ResponseBody
    public String findMonthSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("date"));
        return scheduleService.findMonthSchedule(map.get("date"), map.get("user_id")).toString();
    }

    /**
     * uuid 하나로만 일정 조회
     *
     * @param map
     * @return
     */
    @PostMapping("/findOne")
    @ResponseBody
    public ScheduleResponseDTO findOne(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(String.valueOf(map.get("id")));
        return scheduleService.findOne(map.get("id"));
    }

    @PostMapping("/findCategory")
    public String findScheduleCategory(@RequestBody CategoryRequestDTO categoryRequestDTO, HttpSession session) {
        JSONArray jsonArray = scheduleService
                .findScheduleCategory(categoryRequestDTO, session.getAttribute("session").toString());
        return jsonArray.toString();
    }

    @PostMapping("/find/contains/name")
    public String findByContainsName(@RequestBody String name) {
        return scheduleService.findByContainsName(name).toString();
    }
}
