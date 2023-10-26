package project.fin_the_pen.finClient.util;

import org.json.simple.JSONArray;
import project.fin_the_pen.finClient.data.schedule.Schedule;

import java.util.List;

import static project.fin_the_pen.finClient.util.CategoryStrategy.getJsonArray;

public class SectionStrategy implements ScheduleStrategy {
    @Override
    public JSONArray execute(List<Schedule> scheduleList) {
        return getJsonArrayBySchedule(scheduleList, new JSONArray());
    }

    private JSONArray getJsonArrayBySchedule(List<Schedule> scheduleList, JSONArray jsonArray) {
        return getJsonArray(scheduleList, jsonArray);
    }
}
