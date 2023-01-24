package project.fin_the_pen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.service.ScheduleService;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("registerSchedule")
    public ScheduleRequestDTO registerSchedule(@RequestBody ScheduleRequestDTO scheduleRequestDTO, HttpSession session) {
        scheduleRequestDTO.setUserId(session.getAttribute("session").toString());
        scheduleService.registerSchedule(scheduleRequestDTO);
        log.info(scheduleRequestDTO.getUserId());
        log.info(scheduleRequestDTO.getEventName());
        return scheduleRequestDTO;
    }
}
