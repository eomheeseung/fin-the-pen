package project.fin_the_pen.thirdparty.codefAPI.token.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Token {
    @Id
    private String token;
}
