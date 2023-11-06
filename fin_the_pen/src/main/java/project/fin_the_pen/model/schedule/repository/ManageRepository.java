package project.fin_the_pen.model.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.schedule.entity.ScheduleManage;

@Repository
public interface ManageRepository extends JpaRepository<ScheduleManage, Long> {
}
