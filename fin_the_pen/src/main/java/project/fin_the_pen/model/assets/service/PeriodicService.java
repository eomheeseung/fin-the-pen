package project.fin_the_pen.model.assets.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.fin_the_pen.model.assets.periodic.dto.PeriodicDeleteRequestDto;
import project.fin_the_pen.model.assets.periodic.dto.PeriodicViewRequestDto;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.entity.type.RepeatKind;
import project.fin_the_pen.model.schedule.repository.CrudScheduleRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PeriodicService {
    private final CrudScheduleRepository scheduleRepository;

    public Map<Object, Object> viewPeriodAmount(PeriodicViewRequestDto dto, HttpServletRequest request) {
        String userId = dto.getUserId();
        String startDate = dto.getStartDate();
        String endDate = dto.getEndDate();


        List<Schedule> findScheduleList = scheduleRepository.findByStartDateAndeEndDate(userId, startDate, endDate);

        List<Schedule> filterToRegularList =
                findScheduleList.stream()
                        .filter(schedule -> !schedule.getRepeatKind().equals(RepeatKind.NONE.name()))
                        .collect(Collectors.toList());



        HashMap<Object, Object> responseMap = new HashMap<>();

        responseMap.put("data", filterToRegularList);


        return responseMap;
    }

    public boolean deletePeriodAmount(PeriodicDeleteRequestDto dto, HttpServletRequest request) {
        Long id = dto.getId();
        String userId = dto.getUserId();

        try {
            scheduleRepository.deleteById(id);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
