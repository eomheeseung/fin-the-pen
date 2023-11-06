package project.fin_the_pen.finClient.core.util;

import org.json.simple.JSONArray;
import project.fin_the_pen.model.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleStrategy {
    public JSONArray execute(List<Schedule> scheduleList);
}
