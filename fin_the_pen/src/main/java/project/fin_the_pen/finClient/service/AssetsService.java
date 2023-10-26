package project.fin_the_pen.finClient.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetsService {
    private final ScheduleService scheduleService;

    private JSONArray assetsForSchedule(String date, String userId) {
        return scheduleService.findMonthSchedule(date, userId);
    }

    // TODO 나중에 정기일정도 고려해서 계산해야 함.
    private JSONObject assetsCalc(JSONArray jsonArray, JSONObject responseJson) {
        JSONObject totalJson = new JSONObject();

        AtomicInteger atomicIncome = new AtomicInteger(0);
        AtomicInteger atomicDeposit = new AtomicInteger(0);

        Iterator<Object> iterator = jsonArray.iterator();

        while (iterator.hasNext()) {
            org.json.JSONObject jsonObject = (org.json.JSONObject) iterator.next();

            if (jsonObject.get("type").equals("+")) {
                atomicIncome.addAndGet((int) jsonObject.get("expected_spending"));
            } else if (jsonObject.get("type").equals("-")) {
                atomicDeposit.addAndGet((int) jsonObject.get("expected_spending"));
            }
        }

        int remainSchedule = jsonArray.size();

        log.info("income :{}, deposit:{} ", atomicIncome.get(), atomicDeposit.get());
        totalJson.put("income", atomicIncome.get());
        totalJson.put("deposit", atomicDeposit.get());


        responseJson.put("remainSchedule", remainSchedule);
        responseJson.put("value", totalJson);

        return responseJson;
    }


    // 각 month별로 일정의 개수와 지출/수입을 알려줌
    public JSONObject assetsPrintSchedule(String date, String userId) {
        JSONObject responseJson = null;

        try {
            JSONArray jsonArray = assetsForSchedule(date, userId);
            return assetsCalc(jsonArray, new JSONObject());

        } catch (Exception e) {
            responseJson = new JSONObject();
            responseJson.put("data", "error"); // 예외 메시지 출력
        }

        log.info(responseJson.toString());
        return responseJson;
    }

}
