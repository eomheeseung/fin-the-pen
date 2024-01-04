package project.fin_the_pen.model.report;

import lombok.Data;

@Data
public class ConsumeReportResponseDTO {
    private String category;
    private int amount;
    private String rate;

    public ConsumeReportResponseDTO(String category, int amount, String rate) {
        this.category = category;
        this.amount = amount;
        this.rate = rate + "%";
    }
}
