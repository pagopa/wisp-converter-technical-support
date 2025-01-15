package it.gov.pagopa.wispconverter.technicalsupport.repository.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ReportSentReceiptEntity {

    /**
     * Counter for total sent receipts of type OK
     */
    @JsonProperty("with_ok_receipts")
    private Integer withOkReceipts;

    /**
     * Counter for total sent receipts of type KO
     */
    @JsonProperty("with_ko_receipts")
    private Integer withKoReceipts;

    public ReportSentReceiptEntity() {
        this.withOkReceipts = 0;
        this.withKoReceipts = 0;
    }

    public void merge(ReportSentReceiptEntity other) {
        this.withOkReceipts += other.withOkReceipts;
        this.withKoReceipts += other.withKoReceipts;
    }
}
