package project.fin_the_pen.finClient.core.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import project.fin_the_pen.model.schedule.entity.Schedule;

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
                            .put("price_type", schedule.getPriceType())
                            .put("amount", schedule.getAmount())
                            .put("importance", schedule.getImportance())
                            .put("is_fix", schedule.isFixAmount())
                            .put("exclusion", schedule.isExclude());

                    // enum으로 저장하거나 사용하면 find할때돼 enum타입을 사용해야 함.
                    // "Minus".equals()와 같이 사용하면 찾을 수 없음
//                    if (PriceType.Minus.equals(schedule.getPriceType())) {
//                        jsonObject.put("type", "-");
//                    } else if (PriceType.Plus.equals(schedule.getPriceType())) {
//                        jsonObject.put("type", "+");
//                    }
                    jsonArray.add(jsonObject);
                });

        return jsonArray;
    }
}
