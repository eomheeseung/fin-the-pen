package project.fin_the_pen.model.assets.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.assets.category.entity.CategoryDetail;

@Repository
public interface CategoryDetailRepository extends JpaRepository<CategoryDetail, Long> {
}
