package project.fin_the_pen.model.thirdparty.codef.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class IndividualConnectedID {
    @Id
    @GeneratedValue
    private Long id;

    private String connectedId;
}
