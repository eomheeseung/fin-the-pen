package project.fin_the_pen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.repository.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public void registerSchedule(ScheduleRequestDTO scheduleRequestDTO) {
        scheduleRepository.registerSchedule(scheduleRequestDTO);
    }
}
