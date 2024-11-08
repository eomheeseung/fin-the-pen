package project.fin_the_pen.model.assets.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.assets.category.entity.SmallCategory;

import java.util.Optional;

@Repository
public interface SmallCategoryRepository extends JpaRepository<SmallCategory, Long> {
    Optional<SmallCategory> findBySmallName(String smallName);

}
