package project.fin_the_pen.model.schedule.entity.type.year;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class YearScheduleFunc {
    /*
    연간 : MM월 n번째 D요일을 localDate객체로 만들어주는 method

     inputMonth 11(월)
     ordinalNumber 2(번째)
     dayOfWeek WEDNESDAY(변환된 요일)
     */
    public LocalDate parseMonthlyDate(String inputMonth, int ordinalNumber, DayOfWeek dayOfWeek) {
        try {
            int month = Integer.parseInt(inputMonth);
            LocalDate firstOfMonth = LocalDate.now().withMonth(month).withDayOfMonth(1);

            LocalDate date = firstOfMonth.with(TemporalAdjusters.nextOrSame(dayOfWeek))
                    .with(TemporalAdjusters.next(dayOfWeek))
                    .withDayOfMonth((ordinalNumber - 1) * 7 + 1);

            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LocalDate.now();
    }

    /*
     inputMonth 11(월)
     ordinalNumber 2(번째)
     dayOfWeek WEDNESDAY(변환된 요일)
     */
    public LocalDate parseNextMonthlyDate(LocalDate nextDate, int parseMonth, int weekValue, DayOfWeek dayOfWeek) {
        LocalDate result = nextDate;

        // 주어진 연도의 해당 월의 1일로 설정
        result = result.withMonth(parseMonth).withDayOfMonth(1);

        // 해당 월의 첫 날이 몇 번째 요일인지 계산
        int firstDayOfWeek = result.getDayOfWeek().getValue();

        // 목표하는 요일에 도달할 때까지 날짜를 더함
        int daysToAdd = (weekValue - 1) * 7 + (dayOfWeek.getValue() - firstDayOfWeek + 7) % 7;
        result = result.plusDays(daysToAdd);

        if (firstDayOfWeek > dayOfWeek.getValue()) {
            result = result.minusWeeks(1);
        }

        return result;
    }

    // 연간 : MM월 마지막주 D요일을 계산하는 method
    public LocalDate parseMonthlyLastDate(LocalDate currentDate, String inputMonth, DayOfWeek dayOfWeek) {
        // 입력된 월의 마지막 주차의 첫 번째 날짜 구하기
        LocalDate firstDayOfLastWeek = currentDate.withMonth(Integer.parseInt(inputMonth))
                .with(TemporalAdjusters.lastDayOfMonth())
                .with(TemporalAdjusters.previousOrSame(dayOfWeek));
//                .minusWeeks(1);

        // 입력된 dayOfWeek에 해당하는 날짜를 찾기
        LocalDate result =
                firstDayOfLastWeek.plusDays(dayOfWeek.getValue() - firstDayOfLastWeek.getDayOfWeek().getValue());
        return result;
    }


    public DayOfWeek parseKoreanDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek) {
            case "일요일":
                return DayOfWeek.SUNDAY;
            case "월요일":
                return DayOfWeek.MONDAY;
            case "화요일":
                return DayOfWeek.TUESDAY;
            case "수요일":
                return DayOfWeek.WEDNESDAY;
            case "목요일":
                return DayOfWeek.THURSDAY;
            case "금요일":
                return DayOfWeek.FRIDAY;
            case "토요일":
                return DayOfWeek.SATURDAY;
            default:
                throw new IllegalArgumentException("올바르지 않은 요일입니다.");
        }
    }
}
