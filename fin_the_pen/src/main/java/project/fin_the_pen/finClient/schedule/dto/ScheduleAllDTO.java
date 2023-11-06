package project.fin_the_pen.finClient.schedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ScheduleAllDTO {

    @JsonProperty(value = "schedule")
    private ScheduleDTO scheduleDTO;

    @JsonProperty(value = "assets")
    private AssetRequestDTO assetDto;
}
