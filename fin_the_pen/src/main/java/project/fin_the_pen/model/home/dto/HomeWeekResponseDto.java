package project.fin_the_pen.model.home.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HomeWeekResponseDto {
    private String weekOfNumber;

    private String period;

    private int plus;

    private int minus;
}
