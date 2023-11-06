package project.fin_the_pen.model.thirdparty.codef.entity.analysis;

import lombok.Builder;

import javax.persistence.*;

/**
 * 실적 조회
 */
@Entity
@Table(name = "result_check_lst")
public class DataResultCheck {
    public DataResultCheck() {
    }

    @Builder
    public DataResultCheck(String resCardName, String resCardNo, String resCardType, String resCardCompany, String resCardBenefitName, String resBusinessTypes, String resDetailCardBenefitName, String resDetailPerformanceConditions, String resDetailBusinessTypes, String resCurrentUseAmt, String resRequiredPerformanceAmt, String commStartDate, String commEndDate, String resCoverageCriteria, String resMeetPerformanceYN, String resStandardUseAmt) {
        this.resCardName = resCardName;
        this.resCardNo = resCardNo;
        this.resCardType = resCardType;
        this.resCardCompany = resCardCompany;
        this.resCardBenefitName = resCardBenefitName;
        this.resBusinessTypes = resBusinessTypes;
        this.resDetailCardBenefitName = resDetailCardBenefitName;
        this.resDetailPerformanceConditions = resDetailPerformanceConditions;
        this.resDetailBusinessTypes = resDetailBusinessTypes;
        this.resCurrentUseAmt = resCurrentUseAmt;
        this.resRequiredPerformanceAmt = resRequiredPerformanceAmt;
        this.commStartDate = commStartDate;
        this.commEndDate = commEndDate;
        this.resCoverageCriteria = resCoverageCriteria;
        this.resMeetPerformanceYN = resMeetPerformanceYN;
        this.resStandardUseAmt = resStandardUseAmt;
    }

    @Id
    @GeneratedValue
    @Column(name = "result_check_id")
    private Long id;

    @Column
    private String resCardName;
    @Column
    private String resCardNo;
    @Column
    private String resCardType;
    @Column
    private String resCardCompany;

    // resCardBenefitList 내부에 있는 값들
    @Column
    private String resCardBenefitName;
    @Column
    private String resBusinessTypes;

    // resCardBenefitList.resDetailList 내부에 있는 값들
    @Column
    private String resDetailCardBenefitName;
    @Column
    private String resDetailPerformanceConditions;
    @Column
    private String resDetailBusinessTypes;

    // resCardBenefitList.resCardPerformanceList 내부에 있는 값들
    @Column
    private String resCurrentUseAmt;
    @Column
    private String resRequiredPerformanceAmt;
    @Column
    private String commStartDate;
    @Column
    private String commEndDate;
    @Column
    private String resCoverageCriteria;
    @Column
    private String resMeetPerformanceYN;
    @Column
    private String resStandardUseAmt;
}
