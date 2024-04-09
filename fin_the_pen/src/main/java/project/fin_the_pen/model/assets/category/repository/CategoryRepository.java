package project.fin_the_pen.model.assets.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.assets.category.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.userId = :userId and c.date = :date and c.mediumName = :mediumName")
    Optional<Category> findByUserIdAndDateAndMediumName(@Param("userId") String userId, @Param("date") String date, @Param("mediumName") String mediumName);

    @Query("select c from Category c where c.userId = :userId and c.date = :date")
    List<Category> findByUserIdAndDate(@Param("userId") String userId, @Param("date") String date);
}
