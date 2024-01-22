package project.fin_the_pen.model.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.fin_the_pen.model.schedule.dto.ModifyScheduleDTO;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.TypeManage;
import project.fin_the_pen.model.schedule.type.PriceType;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@Setter
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 일단 schedule와 같은 column을 가져간다고 하자.
    @Column(name = "user_id")
    private String userId;

    // 제목
    @Column(name = "event_name")
    private String eventName;

    // 카테고리
    @Column(name = "category")
    private String category;

    // 시작 일자
    @Column(name = "start_date")
    private String startDate;

    // 종료 일자
    @Column(name = "end_date")
    private String endDate;

    // 시작 시간
    @Column(name = "start_time")
    private String startTime;

    // 종료 시간
    @Column(name = "end_time")
    private String endTime;

    // 하루 종일
    @Column(name = "is_all_day")
    private boolean isAllDay;

    // 반복
    @Column(name = "repeat")
    @Embedded
    private TypeManage repeat;

    // 반복 기간
    @Column(name = "period")
//    @Embedded
    private PeriodType period;

    // 자산 ================================================

    // 입금, 지출
    @Column(name = "price_type")
    private PriceType priceType;

    // 예산에서 제외할 것인지
    @Column(name = "exclusion")
    private boolean isExclude;

    // 중요도
    @Column(name = "importance")
    private String importance;

    // 자산 설정
    @Column(name = "set_amount")
    private String amount;

    // 금액 고정
    @Column(name = "fix_amount")
    private boolean isFixAmount;

    // ScheduleType로 하나 만들어야 함.
//    @Column(name = "regular_type")
//    private RegularType regularType;

    /*@OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL)
    private ScheduleManage scheduleManage;*/

    /*public void setPriceType(Supplier<PriceType> priceTypeSupplier) {
        this.priceType = priceTypeSupplier.get();
    }*/

    /**
     * update method
     *
     * @param dto
     * @param startDate
     * @param endDate
     * @param typeManage
     * @param period
     * @param priceType
     */
    public void updateFrom(ModifyScheduleDTO dto, String startDate, String endDate,
                           TypeManage typeManage, PeriodType period, PriceType priceType) {
        this.eventName = dto.getEventName();
        this.category = dto.getCategory();
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.isAllDay = dto.isAllDay();
        this.repeat = typeManage;
        this.period = period;
        this.priceType = dto.getPriceType();
        this.isExclude = dto.isExclude();
        this.importance = dto.getImportance();
        this.amount = dto.getAmount();
        this.isFixAmount = dto.isFixAmount();
    }
}
