package it.gov.pagopa.wispconverter.technicalsupport.repository.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class ReportNotSentReceiptStatsEntity {

    /**
     * Counter for total not sent OK receipts
     */
    @JsonProperty("receipt_ok_count")
    private Integer receiptOkCount;

    /**
     * Counter for total not sent KO receipts
     */
    @JsonProperty("receipt_ko_count")
    private Integer receiptKoCount;

    /**
     * List of identifier of not sent receipts
     */
    @JsonProperty("receipts")
    private Set<String> receipts;

    public ReportNotSentReceiptStatsEntity() {
        this.receiptOkCount = 0;
        this.receiptKoCount = 0;
        this.receipts = new HashSet<>();
    }

    public void merge(ReportNotSentReceiptStatsEntity other) {
        this.receiptOkCount += other.receiptOkCount;
        this.receiptKoCount += other.receiptKoCount;
        this.receipts.addAll(other.receipts);
    }
}
