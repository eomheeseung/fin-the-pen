package project.fin_the_pen.codefAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.codefAPI.dto.analysis.DataApproval;

@Repository
public interface DataRepository extends JpaRepository<DataApproval, Long> {
}
