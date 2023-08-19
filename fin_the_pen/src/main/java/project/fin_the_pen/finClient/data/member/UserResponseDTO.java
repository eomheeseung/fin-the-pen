package project.fin_the_pen.finClient.data.member;

import lombok.Data;

import java.util.Date;

@Data
public class UserResponseDTO {
    private Long id;
    private String user_id;
    private String name;
    private Date baby;
    private Date registerDate;
    private String userRole;
    private String phone_number;

    public UserResponseDTO(Long id, String user_id, String name, Date baby, Date registerDate, String userRole, String phone_number) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.baby = baby;
        this.registerDate = registerDate;
        this.userRole = userRole;
        this.phone_number = phone_number;
    }
}
