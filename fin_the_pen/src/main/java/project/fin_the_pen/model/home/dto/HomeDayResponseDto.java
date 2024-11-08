package project.fin_the_pen.model.home.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeDayResponseDto {
    private String eventName;
    private String startTime;
    private String endTime;
    private String amount;
}
