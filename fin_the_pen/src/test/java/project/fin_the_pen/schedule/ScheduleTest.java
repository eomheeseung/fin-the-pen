package project.fin_the_pen.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ScheduleTest {


    @Test
    public void 일정등록() throws Exception {
        Logger log = LoggerFactory.getLogger(ScheduleTest.class);

        List<Integer> dates = new ArrayList<>();
        dates.add(1);
        dates.add(4);
        dates.add(5);
        dates.add(30);

        int inputYear = 2024;
        int inputMonth = 2;
        int inputDay = 2;

        LocalDate currentDate = LocalDate.of(inputYear, inputMonth, inputDay);
        LocalDate correctDate = LocalDate.of(2024, 2, 2);
        Assertions.assertEquals(currentDate, correctDate);

        HashMap<Object, Object> map = new HashMap<>();

        log.info(currentDate.toString() + "\n");

        int repeat = 3;
        int term = 2;
        int k=1;

        for (int i = 0; i < repeat; i++) {
            for (Integer date : dates) {
                try {
                    LocalDate tempDate = currentDate.withDayOfMonth(date);
                    if (!tempDate.isBefore(currentDate)) {
                        log.info("움직이는 date: {}", tempDate);
                        map.put(k, tempDate);
                        k++;
                    }
                } catch (DateTimeException e) {
                    log.info("유효하지 않은 날짜입니다. 다음으로 넘어갑니다.");
                }
            }

            currentDate = currentDate.plusMonths(term).withDayOfMonth(1);
            log.info("다음 date : {}", currentDate);
        }



        log.info("저장되는 목록");
        for (Object o : map.keySet()) {
            log.info(map.get(o).toString());
        }
    }
}