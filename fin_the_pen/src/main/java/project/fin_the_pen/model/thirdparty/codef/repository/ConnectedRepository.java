package project.fin_the_pen.model.thirdparty.codef.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.fin_the_pen.model.thirdparty.codef.entity.IndividualConnectedID;

/**
 * connectedId를 DB에 넣고 CRUD 하는 Repository
 * JpaRepository에 이미 @Transactional이 있어서 따로 붙일 필요는 없다.
 */
@Repository
public interface ConnectedRepository extends JpaRepository<IndividualConnectedID, Long> {
}
