package project.fin_the_pen.schedule;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeekSaveTest {
    @Test
    public void 일정등록() {
        Logger log = LoggerFactory.getLogger(WeekSaveTest.class);

        int term = 2;
        int repeat = 3;

        List<String> days = new ArrayList<>();
        days.add("MONDAY");
        days.add("TUESDAY");
        days.add("FRIDAY");

        List<LocalDate> saveList = new ArrayList<>();

        LocalDate localDate = LocalDate.of(2024, 2, 1);

        for (int i = 0; i < repeat; i++) {
            for (String day : days) {
                // 주 간격을 곱하여 해당 주차의 해당 요일을 계산합니다.
                LocalDate targetDate = localDate.with(DayOfWeek.valueOf(day)).plusWeeks(i * term);

                if (targetDate.isBefore(localDate)) {
                    continue;
                }
                saveList.add(targetDate);
            }
        }

        for (LocalDate date : saveList) {
            log.info("save Date:{}, dayOfWeek:{}", date, date.getDayOfWeek());
        }
    }
}
