package project.fin_the_pen.model.assets.domain.type;


import lombok.Getter;

@Getter
public enum PersonalCriteria {
    DAY("day"), MONTH("month");

    private final String name;
    PersonalCriteria(String name) {
        this.name = name;
    }
}
