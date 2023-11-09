package project.fin_the_pen.finClient.core.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import project.fin_the_pen.model.schedule.entity.Schedule;
import project.fin_the_pen.model.schedule.type.PriceType;

import java.util.List;

@Slf4j
public class CommonFunc {
    public static JSONArray getJsonArray(List<Schedule> scheduleList, JSONArray jsonArray) {

        /*scheduleList.stream().forEach(s -> {
            log.info(s.getEventName(), "\n");
        });*/

        /*id?: string;
        user_id?: string;
        event_name: string;
        category: string;
        start_date: string;
        end_date: string;
        start_time: string;
        end_time: string;
        all_day: boolean;
        repeat: string;
        period: string;
        price_type: string;
        amount: string;
        is_fix_Amount: boolean;
        importance: string;
        exclusion: boolean;*/

        scheduleList.stream()
                .forEach(schedule -> {
                    JSONObject jsonObject = new JSONObject()
                            .put("user_id", schedule.getUserId())
                            .put("event_name", schedule.getEventName())
                            .put("category", schedule.getCategory())
                            .put("start_date", schedule.getStartDate())
                            .put("end_date", schedule.getEndDate())
                            .put("start_time", schedule.getStartTime())
                            .put("end_time", schedule.getEndTime())
                            .put("all_day", schedule.isAllDay())
                            .put("repeat", schedule.getRepeat())
                            .put("period", schedule.getPeriod())
//                            .put("price_type", schedule.getPriceType())
                            .put("amount", schedule.getAmount())
                            .put("importance", schedule.getImportance())
                            .put("is_fix", schedule.isFixAmount())
                            .put("exclusion", schedule.isExclude());


                    if (schedule.getPriceType().equals(PriceType.Plus)) {
                        jsonObject.put("price_type", "+");
                    } else if (schedule.getPriceType().equals(PriceType.Minus)) {
                        jsonObject.put("price_type", "-");
                    }

                    log.info("이벤트 이름: {}", jsonObject.get("event_name"));

                    jsonArray.add(jsonObject);
                });

        return jsonArray;
    }
}
