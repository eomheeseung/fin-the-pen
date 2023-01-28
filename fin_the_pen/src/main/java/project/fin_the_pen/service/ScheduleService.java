package project.fin_the_pen.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.repository.ScheduleRepository;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public Boolean registerSchedule(ScheduleRequestDTO scheduleRequestDTO) {
        try {
            scheduleRepository.registerSchedule(scheduleRequestDTO);
        } catch (Exception e) {
            return null;
        }
        return true;
    }

    public JSONArray findAllSchedule(String id) {
        return scheduleRepository.findAllSchedule(id);
    }
}
