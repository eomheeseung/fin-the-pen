package project.fin_the_pen.finClient.util;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import project.fin_the_pen.finClient.data.schedule.Schedule;

import java.util.List;

import static project.fin_the_pen.finClient.util.CategoryStrategy.getJsonArray;

@Slf4j
public class ContainStrategy implements ScheduleStrategy {
    @Override
    public JSONArray execute(List<Schedule> scheduleList) {
        JSONArray jsonArray = new JSONArray();

        if (scheduleList.isEmpty()) {
            jsonArray.add(null);
            return jsonArray;
        }

        log.info(String.valueOf(scheduleList.size()));
        return getJsonArrayBySchedule(scheduleList, jsonArray);
    }

    private JSONArray getJsonArrayBySchedule(List<Schedule> scheduleList, JSONArray jsonArray) {
        return getJsonArray(scheduleList, jsonArray);
    }
}
