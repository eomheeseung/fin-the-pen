package project.fin_the_pen.finClient.api.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestRequestDto {
    private String name;
    @JsonProperty(value = "phone_number")
    private String phoneNumber;
}
