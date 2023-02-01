package project.fin_the_pen.service;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.fin_the_pen.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.data.schedule.ScheduleResponseDTO;
import project.fin_the_pen.repository.ScheduleRepository;

import java.util.UUID;

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

    public JsonObject findAllSchedule(String id) {
        return scheduleRepository.findAllSchedule(id);
    }

    public boolean modifySchedule(ScheduleRequestDTO scheduleRequestDTO) {
        if (!scheduleRepository.modifySchedule(scheduleRequestDTO)) {
            return false;
        }
        return true;
    }

    public ScheduleResponseDTO findOne(UUID uuid) {
        return scheduleRepository.findOneSchedule(uuid);
    }
}
