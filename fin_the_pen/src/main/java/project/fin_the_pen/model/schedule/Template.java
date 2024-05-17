package project.fin_the_pen.model.schedule;

import javax.persistence.*;

@Entity
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String templateName;
    private String categoryName;


}
