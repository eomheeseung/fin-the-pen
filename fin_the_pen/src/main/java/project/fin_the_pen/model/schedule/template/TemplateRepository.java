package project.fin_the_pen.model.schedule.template;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByUserIdAndTemplateNameAndCategoryName(String userId, String templateName, String categoryName);

    List<Template> findByUserId(String userId);
}
