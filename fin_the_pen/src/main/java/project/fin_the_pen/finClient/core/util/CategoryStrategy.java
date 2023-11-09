package project.fin_the_pen.finClient.core.util;

import org.json.simple.JSONArray;
import project.fin_the_pen.model.schedule.entity.Schedule;

import java.util.List;

public class CategoryStrategy implements ScheduleStrategy {
    @Override
    public JSONArray execute(List<Schedule> scheduleList) {
        return getJsonArrayBySchedule(scheduleList, new JSONArray());
    }
    private JSONArray getJsonArrayBySchedule(List<Schedule> scheduleList, JSONArray jsonArray) {
        return CommonFunc.getJsonArray(scheduleList, jsonArray);
    }


}