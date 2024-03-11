package project.fin_the_pen.model.assets.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.assets.category.entity.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.userId = :userId and c.categoryName = :categoryName")
    Optional<Category> findByUserIdAnAndCategoryName(@Param("userId") String userId,@Param("categoryName")String categoryName);
}
