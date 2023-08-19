package project.fin_the_pen.finClient.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.data.schedule.ScheduleRequestDTO;
import project.fin_the_pen.finClient.data.schedule.ScheduleResponseDTO;
import project.fin_the_pen.finClient.data.schedule.category.CategoryRequestDTO;
import project.fin_the_pen.finClient.repository.ScheduleRepository;

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

    public boolean modifySchedule(ScheduleRequestDTO scheduleRequestDTO) {
        if (!scheduleRepository.modifySchedule(scheduleRequestDTO)) {
            return false;
        }
        return true;
    }

    public JSONArray findScheduleCategory(CategoryRequestDTO categoryRequestDTO, String currentSession) {
        return scheduleRepository.findScheduleByCategory(categoryRequestDTO, currentSession);
    }

    public JSONArray findMonthSchedule(String date, String userId) {
        return scheduleRepository.findMonthSchedule(date, userId);
    }

    public boolean deleteSchedule(String uuid) {
        try {
            scheduleRepository.deleteSchedule(uuid);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public ScheduleResponseDTO findOne(String uuid) {
        return scheduleRepository.findOneSchedule(uuid);
    }

    public JSONArray findByContainsName(String name) {
        return scheduleRepository.findByContainsName(name);
    }
}
