package project.fin_the_pen.model.schedule.entity.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class UnitedType {
    private String value = "0";
    private String options;

}
