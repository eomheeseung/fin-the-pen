package project.fin_the_pen.finClient.core.util;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.util.List;

public class CommonFunc {
    public static JSONArray getJsonArray(List<Schedule> scheduleList, JSONArray jsonArray) {
        scheduleList.stream()
                .forEach(schedule -> {
                    JSONObject jsonObject = new JSONObject()
                            .put("id", schedule.getId())
                            .put("event_name", schedule.getEventName())
                            .put("start_date", schedule.getStartDate())
                            .put("end_date", schedule.getEndDate())
                            .put("start_time", schedule.getStartTime())
                            .put("end_time", schedule.getEndTime())
                            .put("allDay",schedule.isAllDay())
                            .put("repeat", schedule.getRepeat())
                            .put("period", schedule.getPeriod())
                            .put("priceType", schedule.getPriceType())
                            .put("exclusion", schedule.isExclude())
                            .put("importance", schedule.getImportance())
                            .put("amount", schedule.getAmount())
                            .put("is_fix",schedule.isFixAmount());

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
