package project.fin_the_pen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.*;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.service.ScheduleService;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/createSchedule")
    public boolean registerSchedule(@RequestBody ScheduleRequestDTO scheduleRequestDTO, HttpSession session) {
        //TODO 나중에 주석 해제해야함. 세션으로 받아올 거라
//        scheduleRequestDTO.setUserId(session.getAttribute("session").toString());
        try {
            scheduleService.registerSchedule(scheduleRequestDTO);
        } catch (Exception e) {
            return false;
        }

        log.info("일정 - " + scheduleRequestDTO.getUserId() + "의 일정 이름: " + scheduleRequestDTO.getEventName());
        return true;
    }

    @GetMapping("/getAllSchedules")
    @ResponseBody
    public JSONArray findSchedule(@RequestBody String id) {
        return scheduleService.findAllSchedule(id);
    }
}
