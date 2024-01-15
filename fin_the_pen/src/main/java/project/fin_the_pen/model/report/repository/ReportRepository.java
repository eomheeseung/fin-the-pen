package project.fin_the_pen.model.report.repository;

import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.report.entity.Reports;

@Repository
public interface ReportRepository extends JpaRepository<Reports, Long> {
}
