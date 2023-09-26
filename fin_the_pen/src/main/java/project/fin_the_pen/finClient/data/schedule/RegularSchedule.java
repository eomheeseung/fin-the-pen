package project.fin_the_pen.finClient.data.schedule;

import lombok.Data;

import javax.persistence.*;

/**
 * 특수한 테이블 개별 schedule와 forgin
 * 1년 정기라고 가정하고,
 * 4,5,6월을 수정한다면, 4,5,6월에 대한 수정사항이 동일해도 3개의 row로 관리하자.
 *
 * 입금 / 출금, 값이 2이상의 것에 대해 enum으로 관리하자.
 */
@Entity
@Data
public class RegularSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /*
    TODO
     1. schedule와 어떻게 매핑을 할 것인지 상속구조로 할 것인지..
     schedule에 enum이 있는데 해당 enum 타입을 통해서 구별하게 할 것인지..
     */
    @Column(name = "schedule_id")
    private String scheduleId;
}
