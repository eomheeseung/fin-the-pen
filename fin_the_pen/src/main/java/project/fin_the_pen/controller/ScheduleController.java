package project.fin_the_pen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.fin_the_pen.data.schedule.ScheduleDAO;
import project.fin_the_pen.service.ScheduleService;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("registerSchedule")
    public ScheduleDAO registerSchedule(@RequestBody ScheduleDAO scheduleDAO, HttpSession session) {
        scheduleDAO.setUserId(session.getAttribute("session").toString());
        scheduleService.registerSchedule(scheduleDAO);
        log.info(scheduleDAO.getUserId());
        log.info(scheduleDAO.getEventName());
        return scheduleDAO;
    }
}
