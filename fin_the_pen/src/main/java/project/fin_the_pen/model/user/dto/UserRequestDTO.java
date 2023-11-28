package project.fin_the_pen.model.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserRequestDTO {
    @JsonProperty("user_id")
    private String userId;
    private String password;
    private String name;
    private Date date;
    private Date registerDate;
    private Date baby;
    private String userRole;
    
    @JsonProperty("phone_number")
    private String phoneNumber;
}
