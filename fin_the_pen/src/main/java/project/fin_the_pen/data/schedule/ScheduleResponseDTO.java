package project.fin_the_pen.data.schedule;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ScheduleResponseDTO {
    private UUID id;
    //    private String userId;
    private boolean alarm;
    private String eventName;
    private String date;
    private String startTime;
    private String endTime;
    private String repeatingCycle;
    private String repeatDeadline;
    private String repeatEndDate;
    private String category;
    private String type;
    private int expectedSpending;
    private boolean exclusion;
    private String importance;

    /*public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("alarm", alarm);
            jsonObject.put("event_name", eventName);
            jsonObject.put("date", eventName);
            jsonObject.put("start_time", eventName);
            jsonObject.put("repeating_cycle", eventName);
            jsonObject.put("repeat_deadline", eventName);
            jsonObject.put("repeat_endDate", eventName);
            jsonObject.put("category", eventName);
            jsonObject.put("type", eventName);
            jsonObject.put("expected_spending", eventName);
            jsonObject.put("importance", eventName);
            jsonObject.put("exclusion", eventName);
        } catch (JSONException e) {
            e.getMessage();
        }
        return jsonObject;
    }*/
}
