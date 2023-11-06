package project.fin_the_pen.model.token.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Token {
    @Id
    @Column(length = 1000)
    private String accessToken;
}
