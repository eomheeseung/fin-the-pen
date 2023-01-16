package project.fin_the_pen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.fin_the_pen.data.schedule.ScheduleDAO;
import project.fin_the_pen.repository.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public void registerSchedule(ScheduleDAO scheduleDAO) {
        scheduleRepository.registerSchedule(scheduleDAO);
    }
}
