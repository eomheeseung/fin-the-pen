package project.fin_the_pen.finClient.util;

import org.json.simple.JSONArray;
import project.fin_the_pen.finClient.data.schedule.Schedule;

import java.util.List;

public interface ScheduleStrategy {
    public JSONArray execute(List<Schedule> scheduleList);
}
