package project.fin_the_pen.finClient.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import project.fin_the_pen.finClient.data.schedule.Schedule;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetsService {
    private final ScheduleService scheduleService;
    private final ObjectMapper objectMapper;

    private JSONArray assetsForSchedule(String date, String userId)
            throws JsonProcessingException {
        JSONArray monthSchedule = scheduleService.findMonthSchedule(date, userId);

        return monthSchedule;
    }


    // TODO
    public JSONObject assetsPrintSchedule(String date, String userId) {
        JSONObject responseJson = null;
        int income;

        try {
            JSONArray jsonArray = assetsForSchedule(date, userId);
            List<Schedule> scheduleList = new ArrayList<>();


            int remainSchedule = scheduleList.size();2
            responseJson = new JSONObject();
            responseJson.put("remainSchedule", remainSchedule);


        } catch (
                JsonProcessingException e) {
            responseJson = new JSONObject();
            responseJson.put("data", "error");
        }
        return responseJson;
    }
}
