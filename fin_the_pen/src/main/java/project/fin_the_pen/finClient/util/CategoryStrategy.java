package project.fin_the_pen.finClient.util;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import project.fin_the_pen.finClient.data.schedule.Schedule;
import project.fin_the_pen.finClient.data.schedule.type.PriceType;

import java.util.List;

public class CategoryStrategy implements ScheduleStrategy {
    @Override
    public JSONArray execute(List<Schedule> scheduleList) {
        return getJsonArrayBySchedule(scheduleList, new JSONArray());
    }
    private JSONArray getJsonArrayBySchedule(List<Schedule> scheduleList, JSONArray jsonArray) {
        return getJsonArray(scheduleList, jsonArray);
    }

    public static JSONArray getJsonArray(List<Schedule> scheduleList, JSONArray jsonArray) {
        scheduleList.stream()
                .forEach(schedule -> {
                    JSONObject jsonObject = new JSONObject()
                            .put("id", schedule.getId())
                            .put("alarm", schedule.isAlarm())
                            .put("event_name", schedule.getEventName())
                            .put("date", schedule.getDate())
                            .put("start_time", schedule.getStartTime())
                            .put("end_time", schedule.getEndTime())
                            .put("repeating_cycle", schedule.getRepeatingCycle())
                            .put("repeat_deadline", schedule.getRepeatDeadline())
                            .put("repeat_endDate", schedule.getRepeatEndDate())
                            .put("category", schedule.getCategory())
                            .put("exclusion", schedule.isExclusion())
                            .put("importance", schedule.getImportance())
                            .put("expected_spending", schedule.getExpectedSpending());

                    // enum으로 저장하거나 사용하면 find할때돼 enum타입을 사용해야 함.
                    // "Minus".equals()와 같이 사용하면 찾을 수 없음
                    if (PriceType.Minus.equals(schedule.getPriceType())) {
                        jsonObject.put("type", "-");
                    } else if (PriceType.Plus.equals(schedule.getPriceType())) {
                        jsonObject.put("type", "+");
                    }
                    jsonArray.add(jsonObject);
                });

        return jsonArray;
    }
}
