package project.fin_the_pen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;
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
        //TODO 나중에 주석 해제해야함. 세션으로 받아올 거라
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

    // TODO 반환 타입 json 형태인데... toString()을 사용해야 postman에서 보임
    @PostMapping ("/getAllSchedules")
    @ResponseBody
    public String findSchedule(@RequestBody ConcurrentHashMap<String, String> map) {
        log.info(map.get("user_id"));
        log.info("json return");
        JSONArray array = scheduleService.findAllSchedule(map.get("user_id"));
        log.info(array.toString());
        return array.toString();
    }
}
