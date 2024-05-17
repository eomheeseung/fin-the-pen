package project.fin_the_pen.model.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.fin_the_pen.model.schedule.entity.embedded.PeriodType;
import project.fin_the_pen.model.schedule.entity.type.PaymentType;
import project.fin_the_pen.model.schedule.entity.type.UnitedType;
import project.fin_the_pen.model.schedule.template.Template;
import project.fin_the_pen.model.schedule.type.PriceType;
import project.fin_the_pen.model.schedule.type.RegularType;

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
    @Column(name = "schedule_id")
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
    @Column(name = "repeat_kind")
    private String repeatKind;

    @Column(name = "repeat_options")
    @Embedded
    private UnitedType repeatOptions;

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
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    // 자산 설정
    @Column(name = "amount")
    private String amount;

    // 금액 고정
    @Column(name = "fix_amount")
    private boolean isFixAmount;

    // ScheduleType로 하나 만들어야 함.
    @Column(name = "regular_type")
    @Enumerated(EnumType.STRING)
    private RegularType regularType;

    @JoinColumn(name = "template_id")
    @ManyToOne
    private Template template;

    public void setTemplate(Template template) {
        if (template == null) {
            Template noneTemplate = Template.builder()
                    .categoryName("none")
                    .categoryName("none")
                    .userId("?")
                    .build();

            noneTemplate.getScheduleList().add(this);
        } else {
            this.template = template;
            template.getScheduleList().add(this);
        }


    }


    public void update(String userId, String eventName, String category, String startDate, String endDate,
                       String startTime, String endTime, boolean isAllDay, String repeatKind, UnitedType repeatOptions,
                       PeriodType period, PriceType priceType, boolean isExclude, String paymentType, String amount,
                       boolean isFixAmount, RegularType regularType) {
        this.userId = userId;
        this.eventName = eventName;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAllDay = isAllDay;
        this.repeatKind = repeatKind;
        this.repeatOptions = repeatOptions;
        this.period = period;
        this.regularType = regularType;

        if (priceType.getType().equals("+")) {
            this.priceType = PriceType.Plus;
        } else if (priceType.getType().equals("-")) {
            this.priceType = PriceType.Minus;
        }

        this.isExclude = isExclude;

        switch (paymentType) {
            case "CARD":
                this.paymentType = PaymentType.CARD;
                break;
            case "CASH":
                this.paymentType = PaymentType.CASH;
                break;
            case "ACCOUNT":
                this.paymentType = PaymentType.ACCOUNT;
                break;
        }

        this.amount = amount;
        this.isFixAmount = isFixAmount;
    }
}
