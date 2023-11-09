package project.fin_the_pen.finClient.core.util;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import project.fin_the_pen.finClient.core.error.customException.NotFoundScheduleException;
import project.fin_the_pen.model.schedule.entity.Schedule;

import java.util.List;

@Slf4j
public class MonthStrategy implements ScheduleStrategy {
    @Override
    public JSONArray execute(List<Schedule> scheduleList) {
        JSONArray jsonArray = new JSONArray();

        // TODO!!!!!
        jsonArray.add(scheduleList);

        if (scheduleList.isEmpty()) {
            throw new NotFoundScheduleException("error");
        }

        return getJsonArrayBySchedule(scheduleList, new JSONArray());
    }

    private JSONArray getJsonArrayBySchedule(List<Schedule> scheduleList, JSONArray jsonArray) {
        return CommonFunc.getJsonArray(scheduleList, jsonArray);
    }
}
