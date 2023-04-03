package project.fin_the_pen.data.analysis;

import javax.persistence.Embeddable;

@Embeddable
public class ResTrHistory {
    private String resAccountTrDate;
    private String resAccountTrTime;
    private String resAccountOut;
    private String resAccountIn;
    private String resAccountDesc1;
    private String resAccountDesc2;
    private String resAccountDesc3;
    private String resAccountDesc4;
    private String resAfterTranBalance;
}
