package it.gov.pagopa.wispconverter.technicalsupport.repository.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonProperty("scheduled")
    private List<String> receipts;
}
