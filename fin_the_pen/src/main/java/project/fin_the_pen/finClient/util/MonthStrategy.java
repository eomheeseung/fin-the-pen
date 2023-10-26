package project.fin_the_pen.finClient.util;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import project.fin_the_pen.finClient.data.schedule.Schedule;
import project.fin_the_pen.finClient.data.schedule.ScheduleResponseDTO;

import java.util.ArrayList;
import java.util.List;

import static project.fin_the_pen.finClient.util.CategoryStrategy.getJsonArray;

@Slf4j
public class MonthStrategy implements ScheduleStrategy {
    @Override
    public JSONArray execute(List<Schedule> scheduleList) {
        JSONArray jsonArray = new JSONArray();

        jsonArray.add(new ArrayList<ScheduleResponseDTO>());

        if (scheduleList.isEmpty()) {
            jsonArray.add(null);
            return jsonArray;
        }

        log.info(String.valueOf(scheduleList.size()));
        return getJsonArrayBySchedule(scheduleList, new JSONArray());
    }

    private JSONArray getJsonArrayBySchedule(List<Schedule> scheduleList, JSONArray jsonArray) {
        return getJsonArray(scheduleList, jsonArray);
    }
}
